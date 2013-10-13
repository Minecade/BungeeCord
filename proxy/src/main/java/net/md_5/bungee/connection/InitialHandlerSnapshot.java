package net.md_5.bungee.connection;

import com.google.common.base.Preconditions;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.logging.Level;
import javax.crypto.Cipher;

import lombok.Getter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.EncryptionUtil;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.http.HttpClient;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.CipherDecoder;
import net.md_5.bungee.netty.CipherEncoder;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.protocol.packet.snapshot.*;
import net.md_5.bungee.protocol.version.Snapshot;

public class InitialHandlerSnapshot extends InitialHandlerAbstract implements PendingConnection
{

    public InitialHandlerSnapshot(ProxyServer bungee, ListenerInfo listener)
    {
        super(bungee, listener);
    }

    @Getter
    private Login forgeLogin;
    @Getter
    private Handshake handshake;
    @Getter
    private LoginRequest loginRequest;
    private EncryptionRequest request;

    @Override
    public void handle(PluginMessage pluginMessage) throws Exception
    {
        // TODO: Unregister?
        if ( pluginMessage.getTag().equals( "REGISTER" ) )
        {
            registerMessages.add( pluginMessage );
        } else
        {
            loginMessages.add( pluginMessage );
        }
    }

    @Override
    public void handle(StatusRequest statusRequest) throws Exception
    {
        Preconditions.checkState( thisState == State.STATUS, "Not expecting STATUS" );

        ServerInfo forced = AbstractReconnectHandler.getForcedHost( this );
        final String motd = ( forced != null ) ? forced.getMotd() : listener.getMotd();

        Callback<ServerPing> pingBack = new Callback<ServerPing>()
        {
            @Override
            public void done(ServerPing result, Throwable error)
            {
                if ( error != null )
                {
                    result = new ServerPing();
                    result.setDescription( "Error pinging remote server: " + Util.exception( error ) );
                }
                result = bungee.getPluginManager().callEvent( new ProxyPingEvent( InitialHandlerSnapshot.this, result ) ).getResponse();

                BungeeCord.getInstance().getConnectionThrottle().unthrottle( getAddress().getAddress() );
                unsafe.sendPacket( new StatusResponse( BungeeCord.getInstance().gson.toJson( result ) ) );
            }
        };

        if ( forced != null && listener.isPingPassthrough() )
        {
            forced.ping( pingBack );
        } else
        {
            pingBack.done( new ServerPing(
                    new ServerPing.Protocol( bungee.getGameVersion(), bungee.getProtocolVersion() ),
                    new ServerPing.Players( bungee.getOnlineCount(), listener.getMaxPlayers() ),
                    motd, "" ),
                    null );
        }

        thisState = State.PING;
    }

    @Override
    public void handle(PingPacket ping) throws Exception
    {
        Preconditions.checkState( thisState == State.PING, "Not expecting PING" );
        unsafe.sendPacket( ping );
        disconnect( "" );
    }

    @Override
    public void handle(Handshake handshake) throws Exception
    {
        Preconditions.checkState( thisState == State.HANDSHAKE, "Not expecting HANDSHAKE" );
        this.handshake = handshake;
        this.vHost = new InetSocketAddress( handshake.getHost(), handshake.getPort() );
        bungee.getLogger().log( Level.INFO, "{0} has connected", this );

        bungee.getPluginManager().callEvent( new PlayerHandshakeEvent( InitialHandlerSnapshot.this, handshake ) );

        if ( handshake.getProtocolVersion() > bungee.getProtocolVersion() )
        {
            disconnect( bungee.getTranslation( "outdated_server" ) );
        } else if ( handshake.getProtocolVersion() < bungee.getProtocolVersion() )
        {
            disconnect( bungee.getTranslation( "outdated_client" ) );
        }

        switch ( handshake.getRequestedProtocol() )
        {
            case 1:
                // Ping
                thisState = State.STATUS;
                ch.setProtocol( Snapshot.STATUS );
                break;
            case 2:
                thisState = State.USERNAME;
                ch.setProtocol( Snapshot.LOGIN );
                // Login
                break;
            default:
                throw new IllegalArgumentException( "Cannot request protocol " + handshake.getRequestedProtocol() );
        }
    }

    @Override
    public void handle(LoginRequest loginRequest) throws Exception
    {
        Preconditions.checkState( thisState == State.USERNAME, "Not expecting USERNAME" );
        this.loginRequest = loginRequest;

        if ( getName().length() > 16 )
        {
            disconnect( "Cannot have username longer than 16 characters" );
            return;
        }

        int limit = BungeeCord.getInstance().config.getPlayerLimit();
        if ( limit > 0 && bungee.getOnlineCount() > limit )
        {
            disconnect( bungee.getTranslation( "proxy_full" ) );
            return;
        }

        // If offline mode and they are already on, don't allow connect
        if ( !isOnlineMode() && bungee.getPlayer( getName() ) != null )
        {
            disconnect( bungee.getTranslation( "already_connected" ) );
            return;
        }

        // TODO: Nuuuu Mojang why u do this
        // unsafe().sendPacket( PacketConstants.I_AM_BUNGEE );
        // unsafe().sendPacket( PacketConstants.FORGE_MOD_REQUEST );

        unsafe().sendPacket( request = EncryptionUtil.snapshotEncryptRequest( this.onlineMode ) );
        thisState = State.ENCRYPT;
    }

    @Override
    public void handle(final EncryptionResponse encryptResponse) throws Exception
    {
        Preconditions.checkState( thisState == State.ENCRYPT, "Not expecting ENCRYPT" );

        sharedKey = EncryptionUtil.snapshotGetSecret( encryptResponse, request );
        Cipher decrypt = EncryptionUtil.getCipher( Cipher.DECRYPT_MODE, sharedKey );
        ch.addBefore( PipelineUtils.FRAME_DECODER, PipelineUtils.DECRYPT_HANDLER, new CipherDecoder( decrypt ) );

        if ( this.onlineMode )
        {
            String encName = URLEncoder.encode( InitialHandlerSnapshot.this.getName(), "UTF-8" );

            MessageDigest sha = MessageDigest.getInstance( "SHA-1" );
            for ( byte[] bit : new byte[][]
            {
                request.getServerId().getBytes( "ISO_8859_1" ), sharedKey.getEncoded(), EncryptionUtil.keys.getPublic().getEncoded()
            } )
            {
                sha.update( bit );
            }
            String encodedHash = URLEncoder.encode( new BigInteger( sha.digest() ).toString( 16 ), "UTF-8" );

            String authURL = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=" + encName + "&serverId=" + encodedHash;

            Callback<String> handler = new Callback<String>()
            {
                @Override
                public void done(String result, Throwable error)
                {
                    if ( error == null )
                    {
                        LoginResult obj = BungeeCord.getInstance().gson.fromJson( result, LoginResult.class );
                        if ( obj != null )
                        {
                            UUID = obj.getId();
                            finish();
                            return;
                        }
                        disconnect( "Not authenticated with Minecraft.net" );
                    } else
                    {
                        disconnect( bungee.getTranslation( "mojang_fail" ) );
                        bungee.getLogger().log( Level.SEVERE, "Error authenticating " + getName() + " with minecraft.net", error );
                    }
                }
            };

            HttpClient.get( authURL, ch.getHandle().eventLoop(), handler );
        } else
        {
            finish();
        }
    }

    private void finish()
    {
        // Check for multiple connections
        ProxiedPlayer old = bungee.getPlayer( getName() );
        if ( old != null )
        {
            old.disconnect( bungee.getTranslation( "already_connected" ) );
        }

        Callback<LoginEvent> complete = new Callback<LoginEvent>()
        {
            @Override
            public void done(LoginEvent result, Throwable error)
            {
                if ( result.isCancelled() )
                {
                    disconnect( result.getCancelReason() );
                }
                if ( ch.isClosed() )
                {
                    return;
                }

                ch.getHandle().eventLoop().execute( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if ( ch.getHandle().isActive() )
                        {

                            if ( onlineMode )
                            {
                                // unsafe().sendPacket( new EncryptionResponse( new byte[ 0 ], new byte[ 0 ] ) );
                                try
                                {
                                    Cipher encrypt = EncryptionUtil.getCipher( Cipher.ENCRYPT_MODE, sharedKey );
                                    ch.addBefore( PipelineUtils.FRAME_PREPENDER, PipelineUtils.ENCRYPT_HANDLER, new CipherEncoder( encrypt ) );
                                } catch ( GeneralSecurityException ex )
                                {
                                    disconnect( "Cipher error: " + Util.exception( ex ) );
                                }
                            }

                            if ( UUID == null )
                            {
                                UUID = java.util.UUID.randomUUID().toString();
                            }
                            unsafe.sendPacket( new LoginSuccess( UUID, getName() ) );
                            ch.setProtocol( Snapshot.GAME );

                            UserConnection userCon = new UserConnection( bungee, ch, getName(), InitialHandlerSnapshot.this );
                            userCon.init();

                            bungee.getPluginManager().callEvent( new PostLoginEvent( userCon ) );

                            ch.getHandle().pipeline().get( HandlerBoss.class ).setHandler( new UpstreamBridgeSnapshot( bungee, userCon ) );

                            ServerInfo server;
                            if ( bungee.getReconnectHandler() != null )
                            {
                                server = bungee.getReconnectHandler().getServer( userCon );
                            } else
                            {
                                server = AbstractReconnectHandler.getForcedHost( InitialHandlerSnapshot.this );
                            }
                            userCon.connect( server, true );

                            thisState = State.FINISHED;
                        }
                    }
                } );
            }
        };

        // fire login event
        bungee.getPluginManager().callEvent( new LoginEvent( InitialHandlerSnapshot.this, complete ) );
    }

    @Override
    public synchronized void disconnect(String reason)
    {
        if ( !ch.isClosed() )
        {
            unsafe().sendPacket( new Kick( BungeeCord.getInstance().gson.toJson( reason ) ) );
            ch.close();
        }
    }

    @Override
    public String getName()
    {
        return ( loginRequest == null ) ? null : loginRequest.getData();
    }

    @Override
    public byte getVersion()
    {
        return (byte) (( handshake == null ) ? version : handshake.getProtocolVersion());
    }

    @Override
    public InetSocketAddress getVirtualHost()
    {
        return vHost;
    }

    @Override
    public InetSocketAddress getAddress()
    {
        return (InetSocketAddress) ch.getHandle().remoteAddress();
    }

    @Override
    public Unsafe unsafe()
    {
        return unsafe;
    }

    public void setOnlineMode(boolean onlineMode)
    {
        Preconditions.checkState( thisState == State.HANDSHAKE, "Can only set online mode status whilst handshaking" );
        this.onlineMode = onlineMode;
    }

    @Override
    public String toString()
    {
        return "[" + ( ( getName() != null ) ? getName() : getAddress() ) + "] <-> InitialHandler";
    }
}

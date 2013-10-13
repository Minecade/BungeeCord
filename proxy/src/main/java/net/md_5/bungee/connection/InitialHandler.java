package net.md_5.bungee.connection;

import com.google.common.base.Preconditions;
import io.netty.util.concurrent.ScheduledFuture;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.crypto.Cipher;
import lombok.Getter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.EncryptionUtil;
import net.md_5.bungee.PacketConstants;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
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
import net.md_5.bungee.netty.PacketDecoder;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.Forge;
import net.md_5.bungee.protocol.MinecraftInput;
import net.md_5.bungee.protocol.packet.Packet1Login;
import net.md_5.bungee.protocol.packet.Packet2Handshake;
import net.md_5.bungee.protocol.packet.PacketCDClientStatus;
import net.md_5.bungee.protocol.packet.PacketFAPluginMessage;
import net.md_5.bungee.protocol.packet.PacketFCEncryptionResponse;
import net.md_5.bungee.protocol.packet.PacketFDEncryptionRequest;
import net.md_5.bungee.protocol.packet.PacketFEPing;
import net.md_5.bungee.protocol.version.Vanilla;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;

public class InitialHandler extends InitialHandlerAbstract implements PendingConnection
{

    public InitialHandler(ProxyServer bungee, ListenerInfo listener) {
        super(bungee, listener);
    }

    @Getter
    private Packet1Login forgeLogin;
    @Getter
    private Packet2Handshake handshake;
    private PacketFDEncryptionRequest request;
    private ScheduledFuture<?> pingFuture;

    public void handle(PacketFAPluginMessage pluginMessage) throws Exception
    {
        if ( pluginMessage.getTag().equals( "MC|PingHost" ) )
        {
            if ( pingFuture.cancel( false ) )
            {
                MinecraftInput in = pluginMessage.getMCStream();
                version = in.readByte();
                String connectHost = in.readString();
                int connectPort = in.readInt();
                this.vHost = new InetSocketAddress( connectHost, connectPort );

                respondToPing();
            }

            return;
        }

        // TODO: Unregister?
        if ( pluginMessage.getTag().equals( "REGISTER" ) )
        {
            registerMessages.add( pluginMessage );
        } else
        {
            loginMessages.add( pluginMessage );
        }
    }

    private void respondToPing()
    {
        ServerInfo forced = AbstractReconnectHandler.getForcedHost( this );
        final String motd = ( forced != null ) ? forced.getMotd() : listener.getMotd();

        Callback<ServerPing> pingBack = new Callback<ServerPing>()
        {
            @Override
            public void done(ServerPing result, Throwable error)
            {
                if ( error != null )
                {
                    result = new ServerPing( (byte) -1, "-1", "Error pinging remote server: " + Util.exception( error ), -1, -1 );
                }
                result = bungee.getPluginManager().callEvent( new ProxyPingEvent( InitialHandler.this, result ) ).getResponse();

                String kickMessage = ChatColor.DARK_BLUE
                        + "\00" + result.getVersion().getProtocol()
                        + "\00" + result.getVersion().getName()
                        + "\00" + result.getDescription()
                        + "\00" + result.getPlayers().getOnline()
                        + "\00" + result.getPlayers().getMax();
                BungeeCord.getInstance().getConnectionThrottle().unthrottle( getAddress().getAddress() );
                disconnect( kickMessage );
            }
        };

        if ( forced != null && listener.isPingPassthrough() )
        {
            forced.ping( pingBack );
        } else
        {
            pingBack.done( new ServerPing( Vanilla.fromByte( getVersion() ).getProtocolVersion(), bungee.getGameVersion(), motd, bungee.getOnlineCount(), listener.getMaxPlayers() ), null );
        }
    }

    @Override
    public void handle(PacketFEPing ping) throws Exception
    {
        pingFuture = ch.getHandle().eventLoop().schedule( new Runnable()
        {
            @Override
            public void run()
            {
                respondToPing();
            }
        }, 200, TimeUnit.MILLISECONDS );
    }

    @Override
    public void handle(Packet1Login login) throws Exception
    {
        Preconditions.checkState( thisState == State.LOGIN, "Not expecting FORGE LOGIN" );
        Preconditions.checkState( forgeLogin == null, "Already received FORGE LOGIN" );
        forgeLogin = login;

        ch.getHandle().pipeline().get( PacketDecoder.class ).setProtocol( Forge.getInstance() );
    }

    @Override
    public void handle(Packet2Handshake handshake) throws Exception
    {
        Preconditions.checkState( thisState == State.HANDSHAKE, "Not expecting HANDSHAKE" );
        this.handshake = handshake;
        this.vHost = new InetSocketAddress( handshake.getHost(), handshake.getPort() );
        bungee.getLogger().log( Level.INFO, "{0} has connected", this );

        bungee.getPluginManager().callEvent( new PlayerHandshakeEvent( InitialHandler.this, handshake ) );

        byte connectingVersion = handshake.getProtocolVersion();
        Vanilla connectedVersion = Vanilla.fromByte( connectingVersion );

        // set proper protocol
        ((PacketDecoder) ch.getHandle().pipeline().get(PipelineUtils.PACKET_DECODER)).setProtocol(connectedVersion);

        if ( connectingVersion > connectedVersion.getProtocolVersion() )
        {
            disconnect( bungee.getTranslation( "outdated_server" ) );
            return;
        } else if ( connectingVersion < connectedVersion.getProtocolVersion() )
        {
            disconnect( bungee.getTranslation( "outdated_client" ) );
            return;
        }

        if ( handshake.getUsername().length() > 16 )
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
        if ( !isOnlineMode() && bungee.getPlayer( handshake.getUsername() ) != null )
        {
            disconnect( bungee.getTranslation( "already_connected" ) );
            return;
        }

        unsafe().sendPacket( PacketConstants.I_AM_BUNGEE );
        unsafe().sendPacket( PacketConstants.FORGE_MOD_REQUEST );

        unsafe().sendPacket( request = EncryptionUtil.encryptRequest( this.onlineMode ) );
        thisState = State.ENCRYPT;
    }

    @Override
    public void handle(final PacketFCEncryptionResponse encryptResponse) throws Exception
    {
        Preconditions.checkState( thisState == State.ENCRYPT, "Not expecting ENCRYPT" );

        sharedKey = EncryptionUtil.getSecret( encryptResponse, request );
        Cipher decrypt = EncryptionUtil.getCipher( Cipher.DECRYPT_MODE, sharedKey );
        ch.addBefore( PipelineUtils.PACKET_DECODER, PipelineUtils.DECRYPT_HANDLER, new CipherDecoder( decrypt ) );

        if ( this.onlineMode )
        {
            String encName = URLEncoder.encode( InitialHandler.this.getName(), "UTF-8" );

            MessageDigest sha = MessageDigest.getInstance( "SHA-1" );
            for ( byte[] bit : new byte[][]
            {
                request.getServerId().getBytes( "ISO_8859_1" ), sharedKey.getEncoded(), EncryptionUtil.keys.getPublic().getEncoded()
            } )
            {
                sha.update( bit );
            }

            String encodedHash = URLEncoder.encode( new BigInteger( sha.digest() ).toString( 16 ), "UTF-8" );
            String authURL = "http://session.minecraft.net/game/checkserver.jsp?user=" + encName + "&serverId=" + encodedHash;

            Callback<String> handler = new Callback<String>()
            {
                @Override
                public void done(String result, Throwable error)
                {
                    if ( error == null )
                    {
                        if ( "YES".equals( result ) )
                        {
                            finish();
                        } else
                        {
                            disconnect( "Not authenticated with Minecraft.net" );
                        }
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
        ProxiedPlayer old = bungee.getPlayer( handshake.getUsername() );
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
                thisState = InitialHandler.State.LOGIN;

                ch.getHandle().eventLoop().execute( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if ( ch.getHandle().isActive() )
                        {
                            unsafe().sendPacket( new PacketFCEncryptionResponse( new byte[ 0 ], new byte[ 0 ] ) );
                            try
                            {
                                Cipher encrypt = EncryptionUtil.getCipher( Cipher.ENCRYPT_MODE, sharedKey );
                                ch.addBefore( PipelineUtils.DECRYPT_HANDLER, PipelineUtils.ENCRYPT_HANDLER, new CipherEncoder( encrypt ) );
                            } catch ( GeneralSecurityException ex )
                            {
                                disconnect( "Cipher error: " + Util.exception( ex ) );
                            }
                        }
                    }
                } );
            }
        };

        // fire login event
        bungee.getPluginManager().callEvent( new LoginEvent( InitialHandler.this, complete ) );
    }

    @Override
    public void handle(PacketCDClientStatus clientStatus) throws Exception
    {
        Preconditions.checkState( thisState == State.LOGIN, "Not expecting LOGIN" );

        UserConnection userCon = new UserConnection( bungee, ch, getName(), this );
        userCon.init();

        bungee.getPluginManager().callEvent( new PostLoginEvent( userCon ) );

        ch.getHandle().pipeline().get( HandlerBoss.class ).setHandler( new UpstreamBridge( bungee, userCon ) );

        ServerInfo server;
        if ( bungee.getReconnectHandler() != null )
        {
            server = bungee.getReconnectHandler().getServer( userCon );
        } else
        {
            server = AbstractReconnectHandler.getForcedHost( this );
        }
        userCon.connect( server, true );

        thisState = State.FINISHED;
        throw new CancelSendSignal();
    }


    @Override
    public String getName()
    {
        return ( handshake == null ) ? null : handshake.getUsername();
    }

    @Override
    public byte getVersion()
    {
        return ( handshake == null ) ? version : handshake.getProtocolVersion();
    }
}

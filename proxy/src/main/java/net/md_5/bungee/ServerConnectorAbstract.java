package net.md_5.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.InitialHandlerSnapshot;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PacketHandler;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Packet2Handshake;
import net.md_5.bungee.protocol.packet.PacketFAPluginMessage;
import net.md_5.bungee.protocol.version.Snapshot.Protocol;

@RequiredArgsConstructor
public abstract class ServerConnectorAbstract extends PacketHandler
{
    protected final ProxyServer bungee;
    protected ChannelWrapper ch;
    protected final UserConnection user;
    protected final BungeeServerInfo target;
    protected State thisState = State.INITIAL;

    protected enum State
    {

        INITIAL, ENCRYPT_RESPONSE, LOGIN, FINISHED;
    }

    @Override
    public void exception(Throwable t) throws Exception
    {
        String message = "Exception Connecting:" + Util.exception( t );
        if ( user.getServer() == null )
        {
            user.disconnect( message );
        } else
        {
            user.sendMessage( ChatColor.RED + message );
        }
    }

    @Override
    public void connected(ChannelWrapper channel) throws Exception
    {
        this.ch = channel;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF( "Login" );
        out.writeUTF( user.getAddress().getHostString() );
        out.writeInt( user.getAddress().getPort() );

        //  TODO - WTF?
        if ( !channel.isSnapshot())
        {
            channel.write( new PacketFAPluginMessage( "BungeeCord", out.toByteArray() ) );
            channel.write( new Packet2Handshake( (byte) 78, user.getName(), "localhost", 25565 ) );
        } else
        {
            channel.write( user.getPendingConnection().getHandshake() );
        }

        System.out.println("Connected!");

        if ( channel.isSnapshot() )
        {
            channel.setProtocol( Protocol.LOGIN );
            channel.write( ((InitialHandlerSnapshot) user.getPendingConnection()).getLoginRequest() );
        }
        else {
            System.out.println("vanillay");
            // Skip encryption if we are not using Forge
            if ( user.getPendingConnection().getForgeLogin() == null )
            {
                channel.write( PacketConstants.getClientLogin() );
            }
        }
    }

    @Override
    public void disconnected(ChannelWrapper channel) throws Exception
    {
        user.getPendingConnects().remove( target );
    }

    @Override
    public String toString()
    {
        return "[" + user.getName() + "] <-> ServerConnector [" + target.getName() + "]";
    }

}

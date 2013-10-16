package net.md_5.bungee.connection;

import lombok.RequiredArgsConstructor;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PacketHandler;

@RequiredArgsConstructor
public class DownstreamBridgeAbstract extends PacketHandler
{
    protected final ProxyServer bungee;
    protected final UserConnection con;
    protected final ServerConnection server;

    @Override
    public void exception(Throwable t) throws Exception
    {
        ServerInfo def = bungee.getServerInfo( con.getPendingConnection().getListener().getFallbackServer() );
        if ( server.getInfo() != def )
        {
            server.setObsolete( true );
            con.connectNow( def );
            con.sendMessage( bungee.getTranslation( "server_went_down" ) );
        } else
        {
            con.disconnect( Util.exception( t ) );
        }
    }

    @Override
    public void disconnected(ChannelWrapper channel) throws Exception
    {
        // We lost connection to the server
        server.getInfo().removePlayer( con );
        if ( bungee.getReconnectHandler() != null )
        {
            bungee.getReconnectHandler().setServer( con );
        }

        if ( !server.isObsolete() )
        {
            con.disconnect( bungee.getTranslation( "lost_connection" ) );
        }
    }

    @Override
    public String toString()
    {
        return "[" + con.getName() + "] <-> DownstreamBridge" + (con.getCh().isSnapshot() ? "Snapshot" : "") + " <-> [" + server.getInfo().getName() + "]";
    }

}

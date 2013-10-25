package net.md_5.bungee.connection;

import net.md_5.bungee.EntityMap;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Packet0KeepAlive;
import net.md_5.bungee.protocol.packet.Packet3Chat;
import net.md_5.bungee.protocol.packet.PacketCBTabComplete;
import net.md_5.bungee.protocol.packet.PacketCCSettings;
import net.md_5.bungee.protocol.packet.PacketFAPluginMessage;
import net.md_5.bungee.protocol.packet.snapshot.game.ClientSettings;
import net.md_5.bungee.protocol.translations.Translations;
import net.md_5.bungee.protocol.version.Snapshot.Direction;

import java.util.ArrayList;
import java.util.List;

public class UpstreamBridge extends UpstreamBridgeAbstract
{

    public UpstreamBridge(ProxyServer bungee, UserConnection con)
    {
        super(bungee, con);
    }

    @Override
    public void handle(PacketWrapper packet) throws Exception
    {
        EntityMap.rewrite( packet, con.getClientEntityId(), con.getServerEntityId() );
        if ( con.getServer() != null )
        {
            con.getServer().sendPacket( packet );
        }
    }

    @Override
    public void handle(Packet0KeepAlive alive) throws Exception
    {
        if ( alive.getRandomId() == con.getSentPingId() )
        {
            int newPing = (int) ( System.currentTimeMillis() - con.getSentPingTime() );
            con.getTabList().onPingChange( newPing );
            con.setPing( newPing );
        }
    }

    @Override
    public void handle(Packet3Chat chat) throws Exception
    {
        ChatEvent chatEvent = new ChatEvent( con, con.getServer(), chat.getMessage() );
        if ( !bungee.getPluginManager().callEvent( chatEvent ).isCancelled() )
        {
            chat.setMessage( chatEvent.getMessage() );
            if ( !chatEvent.isCommand() || !bungee.getPluginManager().dispatchCommand( con, chat.getMessage().substring( 1 ) ) )
            {
                con.getServer().unsafe().sendPacket( chat );
            }
        }
        throw new CancelSendSignal();
    }

    @Override
    public void handle(PacketCBTabComplete tabComplete) throws Exception
    {
        if ( tabComplete.getCursor().startsWith( "/" ) )
        {
            List<String> results = new ArrayList<>();
            bungee.getPluginManager().dispatchCommand( con, tabComplete.getCursor().substring( 1 ), results );

            if ( !results.isEmpty() )
            {
                con.unsafe().sendPacket( new PacketCBTabComplete( results.toArray( new String[ results.size() ] ) ) );
                throw new CancelSendSignal();
            }
        }
    }

    @Override
    public void handle(PacketCCSettings settings) throws Exception
    {
        con.setSettings( settings );
    }

    @Override
    public void handle(PacketFAPluginMessage pluginMessage) throws Exception
    {
        if ( pluginMessage.getTag().equals( "BungeeCord" ) )
        {
            throw new CancelSendSignal();
        }
        // Hack around Forge race conditions
        if ( pluginMessage.getTag().equals( "FML" ) && pluginMessage.getStream().readUnsignedByte() == 1 )
        {
            throw new CancelSendSignal();
        }

        PluginMessageEvent event = new PluginMessageEvent( con, con.getServer(), pluginMessage.getTag(), pluginMessage.getData().clone() );
        if ( bungee.getPluginManager().callEvent( event ).isCancelled() )
        {
            throw new CancelSendSignal();
        }

        // TODO: Unregister as well?
        if ( pluginMessage.getTag().equals( "REGISTER" ) )
        {
            con.getPendingConnection().getRegisterMessages().add( pluginMessage );
        }
    }

}

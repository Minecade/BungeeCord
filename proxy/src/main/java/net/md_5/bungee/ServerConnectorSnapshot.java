package net.md_5.bungee;

import java.util.Queue;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.score.Objective;
import net.md_5.bungee.api.score.Scoreboard;
import net.md_5.bungee.api.score.Team;
import net.md_5.bungee.connection.CancelSendSignal;
import net.md_5.bungee.connection.DownstreamBridge;
import net.md_5.bungee.connection.DownstreamBridgeSnapshot;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.MinecraftOutput;
import net.md_5.bungee.protocol.packet.snapshot.*;
import net.md_5.bungee.protocol.version.Snapshot;

import com.google.common.base.Preconditions;

public class ServerConnectorSnapshot extends ServerConnectorAbstract
{
    public ServerConnectorSnapshot(ProxyServer bungee, UserConnection user, BungeeServerInfo target)
    {
        super(bungee, user, target);
    }

    public void handle(LoginSuccess loginSuccess) throws Exception
    {
        Preconditions.checkState( thisState == State.INITIAL, "Not exepcting INITIAL" );
        ch.setProtocol( Snapshot.GAME );
        thisState = State.LOGIN;

        throw new CancelSendSignal();
    }

    public void handle(Login login) throws Exception
    {
        Preconditions.checkState( thisState == State.LOGIN, "Not exepcting LOGIN" );

        ServerConnection server = new ServerConnection( ch, target );
        ServerConnectedEvent event = new ServerConnectedEvent( user, server );
        bungee.getPluginManager().callEvent( event );

        ch.write( BungeeCord.getInstance().registerChannels() );
        Queue<DefinedPacket> packetQueue = target.getPacketQueue();
        synchronized ( packetQueue )
        {
            while ( !packetQueue.isEmpty() )
            {
                ch.write( packetQueue.poll() );
            }
        }

        for ( DefinedPacket message : user.getPendingConnection().getRegisterMessages() )
        {
            ch.write( message );
        }
        // TODO - readd getLoginMessages sending?

        if ( user.getSettings() != null )
        {
            ch.write( user.getSettings() );
        }

        synchronized ( user.getSwitchMutex() )
        {
            if ( user.getServer() == null )
            {
                // Once again, first connection
                user.setClientEntityId( login.getEntityId() );
                user.setServerEntityId( login.getEntityId() );

                // Set tab list size, this sucks balls, TODO: what shall we do about packet mutability
                Login modLogin = new Login( login.getEntityId(), login.getLevelType(), login.getGameMode(), (byte) login.getDimension(), login.getDifficulty(), login.getUnused(),
                            (byte) user.getPendingConnection().getListener().getTabListSize() );
                user.unsafe().sendPacket( modLogin );

                MinecraftOutput out = new MinecraftOutput();
                out.writeStringUTF8WithoutLengthHeaderBecauseDinnerboneStuffedUpTheMCBrandPacket( ProxyServer.getInstance().getName() + " (" + ProxyServer.getInstance().getVersion() + ")" );
                user.unsafe().sendPacket( new PluginMessage( "MC|Brand", out.toArray() ) );
            } else
            {
                user.getTabList().onServerChange();

                Scoreboard serverScoreboard = user.getServerSentScoreboard();
                for ( Objective objective : serverScoreboard.getObjectives() )
                {
                    user.unsafe().sendPacket( new ScoreboardObjective( objective.getName(), objective.getValue(), (byte) 1 ) );
                }
                for ( Team team : serverScoreboard.getTeams() )
                {
                    user.unsafe().sendPacket( new net.md_5.bungee.protocol.packet.snapshot.Team( team.getName() ) );
                }
                serverScoreboard.clear();

                user.sendDimensionSwitch();

                user.setServerEntityId( login.getEntityId() );
                user.unsafe().sendPacket( new Respawn( login.getDimension(), login.getDifficulty(), login.getGameMode(), (short) 256, login.getLevelType() ) );

                // Remove from old servers
                user.getServer().setObsolete( true );
                user.getServer().disconnect( "Quitting" );
            }

            // TODO: Fix this?
            if ( !user.isActive() )
            {
                server.disconnect( "Quitting" );
                // Silly server admins see stack trace and die
                bungee.getLogger().warning( "No client connected for pending server!" );
                return;
            }

            // Add to new server
            // TODO: Move this to the connected() method of DownstreamBridge
            target.addPlayer( user );
            user.getPendingConnects().remove( target );

            user.setServer( server );
            ch.getHandle().pipeline().get( HandlerBoss.class ).setHandler( new DownstreamBridgeSnapshot( bungee, user, server ) );
        }

        bungee.getPluginManager().callEvent( new ServerSwitchEvent( user ) );

        thisState = State.FINISHED;

        throw new CancelSendSignal();
    }

}

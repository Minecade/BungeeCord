package net.md_5.bungee;

import java.net.InetSocketAddress;
import java.util.Collection;

import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.packet.*;
import net.md_5.bungee.protocol.packet.snapshot.*;

/**
 * Series of utility classes to perform various operations.
 */
public class Util
{

    private static final int DEFAULT_PORT = 25565;

    /**
     * Method to transform human readable addresses into usable address objects.
     *
     * @param hostline in the format of 'host:port'
     * @return the constructed hostname + port.
     */
    public static InetSocketAddress getAddr(String hostline)
    {
        String[] split = hostline.split( ":" );
        int port = DEFAULT_PORT;
        if ( split.length > 1 )
        {
            port = Integer.parseInt( split[1] );
        }
        return new InetSocketAddress( split[0], port );
    }

    /**
     * Formats an integer as a hex value.
     *
     * @param i the integer to format
     * @return the hex representation of the integer
     */
    public static String hex(int i)
    {
        return String.format( "0x%02X", i );
    }

    /**
     * Constructs a pretty one line version of a {@link Throwable}. Useful for
     * debugging.
     *
     * @param t the {@link Throwable} to format.
     * @return a string representing information about the {@link Throwable}
     */
    public static String exception(Throwable t)
    {
        // TODO: We should use clear manually written exceptions
        StackTraceElement[] trace = t.getStackTrace();
        return t.getClass().getSimpleName() + " : " + t.getMessage()
                + ( ( trace.length > 0 ) ? " @ " + t.getStackTrace()[0].getClassName() + ":" + t.getStackTrace()[0].getLineNumber() : "" );
    }

    public static String csv(Iterable<?> objects)
    {
        return format( objects, ", " );
    }

    public static String format(Iterable<?> objects, String separators)
    {
        StringBuilder ret = new StringBuilder();
        for ( Object o : objects )
        {
            ret.append( o );
            ret.append( separators );
        }

        return ( ret.length() == 0 ) ? "" : ret.substring( 0, ret.length() - separators.length() );
    }

    public static DefinedPacket translatePacket( ChannelWrapper channel, DefinedPacket packet )
    {
        if ( !channel.isSnapshot() )
        {
            return packet;
        }
    
        if ( packet instanceof Packet0KeepAlive )
        {
            Packet0KeepAlive old = (Packet0KeepAlive) packet;
            return new KeepAlive(old.getRandomId());
        }
        if ( packet instanceof Packet1Login )
        {
            Packet1Login old = (Packet1Login) packet;
            return new Login(old.getEntityId(), old.getLevelType(), old.getGameMode(), old.getDimension(), old.getDifficulty(), old.getUnused(), old.getMaxPlayers());
        }
        if ( packet instanceof Packet2Handshake )
        {
            // can't convert
        }
        if ( packet instanceof Packet3Chat )
        {
            Packet3Chat old = (Packet3Chat) packet;
            return new Chat(old.getMessage());
        }
        if ( packet instanceof Packet9Respawn )
        {
            Packet9Respawn old = (Packet9Respawn) packet;
            return new Respawn(old.getDimension(), old.getDifficulty(), old.getGameMode(), old.getWorldHeight(), old.getLevelType());
        }
        if ( packet instanceof PacketC9PlayerListItem )
        {
            PacketC9PlayerListItem old = (PacketC9PlayerListItem) packet;
            return new PlayerListItem(old.getUsername(), old.isOnline(), old.getPing());
    
        }
        if ( packet instanceof PacketCBTabComplete )
        {
            PacketCBTabComplete old = (PacketCBTabComplete) packet;
            return new TabComplete(old.getCommands());
        }
        if ( packet instanceof PacketCCSettings )
        {
            PacketCCSettings old = (PacketCCSettings) packet;
            return new ClientSettings(old.getLocale(), old.getViewDistance(), old.getChatFlags(), true /* TODO - unknown */, old.getDifficulty(), old.isShowCape());
        }
        if ( packet instanceof PacketCDClientStatus )
        {
            PacketCDClientStatus old = (PacketCDClientStatus) packet;
            return new ClientStatus(old.getPayload());
        }
        if ( packet instanceof PacketCEScoreboardObjective )
        {
            PacketCEScoreboardObjective old = (PacketCEScoreboardObjective) packet;
            return new ScoreboardObjective(old.getName(), old.getText(), old.getAction());
        }
        if ( packet instanceof PacketCFScoreboardScore )
        {
            PacketCFScoreboardScore old = (PacketCFScoreboardScore) packet;
            return new ScoreboardScore(old.getItemName(), old.getAction(), old.getScoreName(), old.getValue());
        }
        if ( packet instanceof PacketD0DisplayScoreboard )
        {
            PacketD0DisplayScoreboard old = (PacketD0DisplayScoreboard) packet;
            return new ScoreboardDisplay(old.getPosition(), old.getName());
        }
        if ( packet instanceof PacketD1Team )
        {
            PacketD1Team old = (PacketD1Team) packet;
            return new Team(old.getName(), old.getMode(), old.getDisplayName(), old.getPrefix(), old.getSuffix(), old.isFriendlyFire(), old.getPlayerCount(), old.getPlayers());
        }
        if ( packet instanceof PacketFAPluginMessage )
        {
            PacketFAPluginMessage old = (PacketFAPluginMessage) packet;
            return new PluginMessage(old.getTag(), old.getData());
        }
        if ( packet instanceof PacketFCEncryptionResponse )
        {
            PacketFCEncryptionResponse old = (PacketFCEncryptionResponse) packet;
            return new EncryptionResponse(old.getSharedSecret(), old.getVerifyToken());
        }
        if ( packet instanceof PacketFDEncryptionRequest )
        {
            PacketFDEncryptionRequest old = (PacketFDEncryptionRequest) packet;
            return new EncryptionRequest(old.getServerId(), old.getPublicKey(), old.getVerifyToken());
        }
        if ( packet instanceof PacketFEPing )
        {
            // can't convert
        }
        if ( packet instanceof PacketFFKick )
        {
            PacketFFKick old = (PacketFFKick) packet;
            return new Kick(old.getMessage());
        }
    
        throw new RuntimeException("unable to convert packet");
    }
}

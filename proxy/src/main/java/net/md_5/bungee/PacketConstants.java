package net.md_5.bungee;

import net.md_5.bungee.protocol.packet.PacketCDClientStatus;
import net.md_5.bungee.protocol.packet.PacketFAPluginMessage;
import net.md_5.bungee.protocol.packet.snapshot.game.Respawn;

public class PacketConstants
{

    public static final PacketFAPluginMessage FORGE_MOD_REQUEST = new PacketFAPluginMessage( "FML", new byte[]
    {
        0, 0, 0, 0, 0, 2
    } );
    public static final PacketFAPluginMessage I_AM_BUNGEE = new PacketFAPluginMessage( "BungeeCord", new byte[ 0 ] );

    public static Respawn getDimSwitch(byte i)
    {
        return new Respawn( i, (byte) 0, (byte) 0, "default" );
    }

    public static PacketCDClientStatus getClientLogin()
    {
        return new PacketCDClientStatus( (byte) 0 );
    }
}

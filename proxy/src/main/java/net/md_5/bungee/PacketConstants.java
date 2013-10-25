package net.md_5.bungee;

import net.md_5.bungee.protocol.packet.Packet9Respawn;
import net.md_5.bungee.protocol.packet.PacketCDClientStatus;
import net.md_5.bungee.protocol.packet.PacketFAPluginMessage;

public class PacketConstants
{

    public static final PacketFAPluginMessage FORGE_MOD_REQUEST = new PacketFAPluginMessage( "FML", new byte[]
    {
        0, 0, 0, 0, 0, 2
    } );
    public static final PacketFAPluginMessage I_AM_BUNGEE = new PacketFAPluginMessage( "BungeeCord", new byte[ 0 ] );

    public static Packet9Respawn getDimSwitch(byte i)
    {
        return new Packet9Respawn( i, (byte) 0, (byte) 0, (short) 256, "default" );
    }

    public static PacketCDClientStatus getClientLogin()
    {
        return new PacketCDClientStatus( (byte) 0 );
    }
}

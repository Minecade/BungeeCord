package net.md_5.bungee.protocol.version.snapshot;

import io.netty.buffer.ByteBuf;
import java.lang.reflect.Constructor;
import lombok.Getter;
import net.md_5.bungee.protocol.OpCode;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import net.md_5.bungee.protocol.skip.PacketReader;

public class Vanilla13w41a implements Protocol
{

    protected final byte PROTOCOL_VERSION;
    protected final String GAME_VERSION;
    @Getter
    protected static final Vanilla13w41a instance = new Vanilla13w41a();
    /*========================================================================*/
    @Getter
    protected PacketReader skipper;
    /*========================================================================*/

    public Vanilla13w41a()
    {
        this((byte) 0, "13w41a");
    }

    public Vanilla13w41a( final byte protocol, final String version )
    {
        PROTOCOL_VERSION = protocol;
        GAME_VERSION = version;
        
        // TODO
        // classes[0x00] = Packet0KeepAlive.class;
        skipper = new PacketReader( this );
    }

    @Override
    public DefinedPacket read(short packetId, ByteBuf buf)
    {
        // TODO
        return null;
    }

    public static DefinedPacket read(short id, ByteBuf buf, Protocol protocol)
    {
        // TODO
        return null;
    }

    public static DefinedPacket packet(short id, Protocol protocol)
    {
        // TODO
        return null;
    }

    public byte getProtocolVersion()
    {
        return PROTOCOL_VERSION;
    }
    
    public String getGameVersion()
    {
        return GAME_VERSION;
    }

    @Override
    public OpCode[][] getOpCodes() {
        // TODO
        return null;
    }

    @Override
    public Class<? extends DefinedPacket>[] getClasses() {
        // TODO
        return null;
    }

    @Override
    public Constructor<? extends DefinedPacket>[] getConstructors() {
        // TODO
        return null;
    }

    {
        // TODO
    }
}

package net.md_5.bungee.protocol.packet.serverbound.play;

import net.md_5.bungee.protocol.packet.AbstractPacketHandler;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = false)
public class Packet16ClientStatus extends DefinedPacket
{

    private byte payload;

    private Packet16ClientStatus()
    {
        super( 0x16 );
    }

    public Packet16ClientStatus(byte payload)
    {
        this();
        this.payload = payload;
    }

    @Override
    public void read(ByteBuf buf)
    {
        payload = buf.readByte();
    }

    @Override
    public void write(ByteBuf buf)
    {
        buf.writeByte( payload );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

package net.md_5.bungee.protocol.packet.snapshot.game;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;

@Data
@EqualsAndHashCode(callSuper = false)
public class PingPacket extends DefinedPacket
{

    private long time;

    public PingPacket()
    {
        setSnapshot(true);
    }

    @Override
    public void read(ByteBuf buf)
    {
        time = buf.readLong();
    }

    @Override
    public void write(ByteBuf buf)
    {
        buf.writeLong( time );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}
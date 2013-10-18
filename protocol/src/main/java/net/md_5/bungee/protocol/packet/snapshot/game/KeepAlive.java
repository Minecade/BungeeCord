package net.md_5.bungee.protocol.packet.snapshot.game;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class KeepAlive extends DefinedPacket
{

    private int randomId;

    public KeepAlive()
    {
        setSnapshot(true);
    }

    @Override
    public void read(ByteBuf buf)
    {
        randomId = buf.readInt();
    }

    @Override
    public void write(ByteBuf buf)
    {
        buf.writeInt( randomId );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

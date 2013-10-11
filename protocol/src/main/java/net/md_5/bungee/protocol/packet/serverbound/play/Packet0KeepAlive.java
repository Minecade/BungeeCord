package net.md_5.bungee.protocol.packet.serverbound.play;

import net.md_5.bungee.protocol.packet.AbstractPacketHandler;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Packet0KeepAlive extends DefinedPacket
{

    private int randomId;

    private Packet0KeepAlive()
    {
        super( 0x00 );
    }

    @Override
    public void read(ByteBuf buf)
    {
        randomId = buf.readInt();
    }

    @Override
    public void write(ByteBuf buf)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

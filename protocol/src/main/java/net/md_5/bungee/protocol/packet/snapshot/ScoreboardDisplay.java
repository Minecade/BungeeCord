package net.md_5.bungee.protocol.packet.snapshot;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ScoreboardDisplay extends DefinedPacket
{

    /**
     * 0 = list, 1 = side, 2 = below.
     */
    private byte position;
    private String name;

    @Override
    public void read(ByteBuf buf)
    {
        position = buf.readByte();
        name = readString( buf );
    }

    @Override
    public void write(ByteBuf buf)
    {
        buf.writeByte( position );
        writeString( name, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}
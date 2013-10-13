package net.md_5.bungee.protocol.packet.snapshot;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ScoreboardObjective extends DefinedPacket
{

    private String name;
    private String text;
    /**
     * 0 to create, 1 to remove.
     */
    private byte action;

    @Override
    public void read(ByteBuf buf)
    {
        name = readString( buf );
        text = readString( buf );
        action = buf.readByte();
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeString( name, buf );
        writeString( text, buf );
        buf.writeByte( action );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

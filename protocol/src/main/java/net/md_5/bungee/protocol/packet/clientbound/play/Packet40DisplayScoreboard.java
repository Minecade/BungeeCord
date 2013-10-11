package net.md_5.bungee.protocol.packet.clientbound.play;

import net.md_5.bungee.protocol.packet.AbstractPacketHandler;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Packet40DisplayScoreboard extends DefinedPacket
{

    /**
     * 0 = list, 1 = side, 2 = below.
     */
    private byte position;
    private String name;

    private Packet40DisplayScoreboard()
    {
        super( 0x40 );
    }

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

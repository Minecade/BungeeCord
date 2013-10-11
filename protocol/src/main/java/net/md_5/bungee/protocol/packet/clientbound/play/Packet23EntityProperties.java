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
public class Packet23EntityProperties extends DefinedPacket
{

    public Packet23EntityProperties()
    {
        super( 0x23 );
    }

    @Override
    public void read(ByteBuf buf)
    {
        buf.readInt();
        int recordCount = buf.readInt();
        for ( int i = 0; i < recordCount; i++ )
        {
            readString( buf );
            buf.readDouble();
            short size = buf.readShort();
            for ( short s = 0; s < size; s++ )
            {
                buf.skipBytes( 25 ); // long, long, double, byte
            }
        }
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

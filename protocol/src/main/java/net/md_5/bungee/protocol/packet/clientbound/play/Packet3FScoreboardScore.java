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
public class Packet3FScoreboardScore extends DefinedPacket
{

    private String itemName;
    /**
     * 0 = create / update, 1 = remove.
     */
    private byte action;
    private String scoreName;
    private int value;

    private Packet3FScoreboardScore()
    {
        super( 0x3F );
    }

    @Override
    public void read(ByteBuf buf)
    {
        itemName = readString( buf );
        action = buf.readByte();
        if ( action != 1 )
        {
            scoreName = readString( buf );
            value = buf.readInt();
        }
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeString( itemName, buf );
        buf.writeByte( action );
        if ( action != 1 )
        {
            writeString( scoreName, buf );
            buf.writeInt( value );
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

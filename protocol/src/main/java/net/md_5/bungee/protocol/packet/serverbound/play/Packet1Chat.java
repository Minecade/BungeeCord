package net.md_5.bungee.protocol.packet.serverbound.play;

import net.md_5.bungee.protocol.packet.AbstractPacketHandler;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Packet1Chat extends DefinedPacket
{

    private String message;

    private Packet1Chat()
    {
        super( 0x01 );
    }

    public Packet1Chat(String message)
    {
        this();
        this.message = message;
    }

    @Override
    public void read(ByteBuf buf)
    {
        message = readString( buf );
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

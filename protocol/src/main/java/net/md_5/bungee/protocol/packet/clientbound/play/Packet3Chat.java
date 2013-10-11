package net.md_5.bungee.protocol.packet.clientbound.play;

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
public class Packet3Chat extends DefinedPacket
{

    private String message;

    private Packet3Chat()
    {
        super( 0x03 );
    }

    public Packet3Chat(String message)
    {
        this();
        this.message = message;
    }

    @Override
    public void read(ByteBuf buf)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeString( message, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

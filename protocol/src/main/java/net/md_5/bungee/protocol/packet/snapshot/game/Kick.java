package net.md_5.bungee.protocol.packet.snapshot.game;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class Kick extends DefinedPacket
{

    private String message;

    public Kick()
    {
        setSnapshot(true);
    }

    public Kick(String message)
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
        writeString( message, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

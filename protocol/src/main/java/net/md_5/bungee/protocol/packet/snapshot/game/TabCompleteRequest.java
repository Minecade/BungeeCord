package net.md_5.bungee.protocol.packet.snapshot.game;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class TabCompleteRequest extends DefinedPacket
{

    private String cursor;

    public TabCompleteRequest()
    {
        setSnapshot(true);
    }

    @Override
    public void read(ByteBuf buf)
    {
        cursor = readString( buf );
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeString( cursor, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

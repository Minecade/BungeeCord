package net.md_5.bungee.protocol.packet.snapshot.game;

import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.protocol.AbstractPacketHandler;

@Data
@EqualsAndHashCode(callSuper = false)
public class TabCompleteResponse extends DefinedPacket
{

    private String[] commands;

    public TabCompleteResponse()
    {
        setSnapshot(true);
    }

    public TabCompleteResponse(String[] commands)
    {
        this();
        this.commands = commands;
    }

    @Override
    public void read(ByteBuf buf)
    {
        commands = readStringArray( buf );
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeStringArray( commands, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}
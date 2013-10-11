package net.md_5.bungee.protocol.packet.serverbound.play;

import net.md_5.bungee.protocol.packet.AbstractPacketHandler;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Packet14TabComplete extends DefinedPacket
{

    private String cursor;
    private String[] commands;

    private Packet14TabComplete()
    {
        super( 0x14 );
    }

    public Packet14TabComplete(String[] alternatives)
    {
        this();
        commands = alternatives;
    }

    @Override
    public void read(ByteBuf buf)
    {
        cursor = readString( buf );
    }

    @Override
    public void write(ByteBuf buf)
    {
        StringBuilder tab = new StringBuilder();
        for ( String alternative : commands )
        {
            tab.append( alternative );
            tab.append( "\00" );
        }
        writeString( tab.substring( 0, tab.length() - 1 ), buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

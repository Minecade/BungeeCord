package net.md_5.bungee.protocol.packet.snapshot.game;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;

import lombok.*;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.MinecraftInput;

@Data
@EqualsAndHashCode(callSuper = false)
public class PluginMessage extends DefinedPacket
{

    private String tag;
    private byte[] data;

    public PluginMessage()
    {
        setSnapshot(true);
    }

    public PluginMessage(String tag, byte[] data)
    {
        this();
        this.tag = tag;
        this.data = data;
    }

    @Override
    public void read(ByteBuf buf)
    {
        tag = readString( buf );
        data = readArray( buf );
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeString( tag, buf );
        writeArray( data, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }

    public DataInput getStream()
    {
        return new DataInputStream( new ByteArrayInputStream( data ) );
    }

    public MinecraftInput getMCStream()
    {
        return new MinecraftInput( Unpooled.wrappedBuffer( data ) );
    }
}

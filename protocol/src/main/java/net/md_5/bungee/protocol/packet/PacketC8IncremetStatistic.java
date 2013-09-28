package net.md_5.bungee.protocol.packet;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class PacketC8IncremetStatistic extends DefinedPacket
{

    private Map<String, Integer> entries;
    
    public PacketC8IncremetStatistic(Map<String, Integer> entries)
    {
        super( 0xC8 );
        this.entries = entries;
    }

    @Override
    public void read(ByteBuf buf)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(ByteBuf buf)
    {
        buf.writeInt(entries.size());

        Iterator<Entry<String, Integer>> it = entries.entrySet().iterator();
        while( it.hasNext() )
        {
            Entry<String, Integer> entry = it.next();
            writeString( entry.getKey(), buf );
            buf.writeInt( entry.getValue() );
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

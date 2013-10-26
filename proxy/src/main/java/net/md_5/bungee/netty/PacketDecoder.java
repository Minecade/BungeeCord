package net.md_5.bungee.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.skip.PacketReader;

/**
 * This class will attempt to read a packet from {@link PacketReader}, with the
 * specified {@link #protocol} before returning a new {@link ByteBuf} with the
 * copied contents of all bytes read in this frame.
 * <p/>
 * It is based on {@link ReplayingDecoder} so that packets will only be returned
 * when all needed data is present.
 */
@AllArgsConstructor
public class PacketDecoder extends ReplayingDecoder<Void>
{

    @Getter
    @Setter
    private Protocol protocol;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        // While we have enough data
        while ( true )
        {
            // Store our start index
            int startIndex = in.readerIndex();
            // Run packet through framer
            short packetId = in.readUnsignedByte();
            DefinedPacket packet = protocol.read( packetId, in );
            // If we got this far, it means we have formed a packet, so lets grab the end index
            int endIndex = in.readerIndex();
            // Allocate a buffer big enough for all bytes we have read
            ByteBuf buf = in.copy( startIndex, endIndex - startIndex );
            // Save the buf to the packet
            if ( packet != null ) packet.setBuf(buf);
            // Checkpoint our state incase we don't have enough data for another packet
            checkpoint();
            // Store our decoded message
            out.add( new PacketWrapper( packetId, packet, buf, false ) );
        }
    }
}

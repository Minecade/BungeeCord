package net.md_5.bungee.protocol.snapshot;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.version.Snapshot;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
public class MinecraftDecoder extends ByteToMessageDecoder
{

    @Setter
    private Snapshot protocol;
    private boolean server;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        Snapshot.ProtocolDirection prot = ( server ) ? protocol.TO_SERVER : protocol.TO_CLIENT;
        ByteBuf copy = in.copy(); // TODO

        int packetId = DefinedPacket.readVarInt( in );

        DefinedPacket packet = null;
        if ( prot.hasPacket( packetId ) )
        {
            packet = prot.createPacket( packetId );
            packet.read( in );
            if ( in.readableBytes() != 0 )
            {
                System.out.println( in.toString( Charsets.UTF_8 ) );
                // throw new BadPacketException( "Did not read all bytes from packet " + packet.getClass() + " " + packetId + " Protocol " + protocol + " Direction " + prot );
            }
        } else
        {
            in.skipBytes( in.readableBytes() );
        }

        out.add( new PacketWrapper( packet, copy ) );
    }
}
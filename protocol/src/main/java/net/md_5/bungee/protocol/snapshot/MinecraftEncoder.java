package net.md_5.bungee.protocol.snapshot;

import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.version.Snapshot;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
public class MinecraftEncoder extends MessageToByteEncoder<DefinedPacket>
{

    @Setter
    private Snapshot protocol;
    private boolean server;

    @Override
    protected void encode(ChannelHandlerContext ctx, DefinedPacket msg, ByteBuf out) throws Exception
    {
        Snapshot.ProtocolDirection prot = ( server ) ? protocol.TO_CLIENT : protocol.TO_SERVER;
        DefinedPacket.writeVarInt( prot.getId( msg.getClass() ), out );
        msg.write( out );
    }
}

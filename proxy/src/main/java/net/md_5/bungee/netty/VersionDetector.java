package net.md_5.bungee.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.InitialHandlerSnapshot;
import net.md_5.bungee.protocol.PacketUtil;

@NoArgsConstructor
public class VersionDetector extends ByteToMessageDecoder
{

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        in.markReaderIndex();
        int packetId = PacketUtil.readVarInt( in );
        in.resetReaderIndex();

        if ( packetId == 2 || packetId == 254 )
        {
            System.out.println("VANILLA DETECTED");
            PipelineUtils.VANILLA_INIT.initChannel(ctx.channel());

            ctx.pipeline().get( HandlerBoss.class ).setHandler( new InitialHandler( ProxyServer.getInstance(), ctx.channel().attr( PipelineUtils.LISTENER ).get() ) );
        }
        else {
            System.out.println("SNAPSHOT DETECTED");
            PipelineUtils.SNAPSHOT_INIT.initChannel(ctx.channel());

            ctx.pipeline().get( HandlerBoss.class ).setHandler( new InitialHandlerSnapshot( ProxyServer.getInstance(), ctx.channel().attr( PipelineUtils.LISTENER ).get() ) );
        }

        ctx.fireChannelActive();
        ctx.pipeline().remove( this );
    }
}

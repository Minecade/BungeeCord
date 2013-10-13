package net.md_5.bungee.netty;

import net.md_5.bungee.Util;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.snapshot.MinecraftDecoder;
import net.md_5.bungee.protocol.snapshot.MinecraftEncoder;
import net.md_5.bungee.protocol.version.Snapshot;

import com.google.common.base.Preconditions;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

public class ChannelWrapper
{

    private final Channel ch;
    @Getter
    private volatile boolean closed;
    private Boolean isSnapshot;

    public ChannelWrapper(ChannelHandlerContext ctx)
    {
        this.ch = ctx.channel();
    }

    public synchronized void write(Object packet)
    {
        if ( !closed )
        {
            if ( packet instanceof PacketWrapper )
            {
                ( (PacketWrapper) packet ).setReleased( true );
                if ( isSnapshot() )
                {
                    ch.write( ( (PacketWrapper) packet).buf, ch.voidPromise() );
                } else
                {
                    ch.write( ( (PacketWrapper) packet ).buf );
                }
            } else
            {
                if ( isSnapshot() )
                {
                    Object newPacket = packet;
                    if ( packet instanceof DefinedPacket )
                    {
                        newPacket = Util.translatePacket(this, (DefinedPacket) packet);
                    }

                    ch.write( newPacket, ch.voidPromise() );
                } else
                {
                    ch.write( packet );
                }
            }
            ch.flush();
        }
    }

    public synchronized void close()
    {
        if ( !closed )
        {
            closed = true;
            ch.flush();
            ch.close();
        }
    }

    public void addBefore(String baseName, String name, ChannelHandler handler)
    {
        Preconditions.checkState( ch.eventLoop().inEventLoop(), "cannot add handler outside of event loop" );
        ch.pipeline().flush();
        ch.pipeline().addBefore( baseName, name, handler );
    }

    public Channel getHandle()
    {
        return ch;
    }

    public boolean isSnapshot()
    {
        if ( this.isSnapshot == null )
        {
            isSnapshot = ( ch.attr( PipelineUtils.PROTOCOL ).get() instanceof Snapshot );
        }

        return isSnapshot;
    }

    public void setProtocol(Snapshot protocol) {
        ch.pipeline().get( MinecraftDecoder.class ).setProtocol( protocol );
        ch.pipeline().get( MinecraftEncoder.class ).setProtocol( protocol );
    }
}

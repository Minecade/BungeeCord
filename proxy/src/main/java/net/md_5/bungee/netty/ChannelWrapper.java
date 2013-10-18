package net.md_5.bungee.netty;

import net.md_5.bungee.Util;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Translations;
import net.md_5.bungee.protocol.snapshot.MinecraftDecoder;
import net.md_5.bungee.protocol.snapshot.MinecraftEncoder;
import net.md_5.bungee.protocol.version.Snapshot;
import net.md_5.bungee.protocol.version.Snapshot.Protocol;

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
    // server (vanilla) -> client (snapshot) - translate writing
    // client (snapshot -> server (vanilla) - translate reading
    // server (vanilla) -> client (vanilla) - no translation
    // client (vanilla) -> server (vanilla) - no translation
    private boolean translate = false;

    public ChannelWrapper(ChannelHandlerContext ctx)
    {
        this.ch = ctx.channel();
    }

    public ChannelWrapper(ChannelHandlerContext ctx, boolean translate)
    {
        this(ctx);
        this.translate = translate;
    }

    public synchronized void write(Object packet)
    {
        if ( !closed )
        {
            if ( translate )
            {
                packet = Translations.translate(packet);
            }

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
                    ch.write( packet, ch.voidPromise() );
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

    public void setProtocol(Protocol protocol) {
        ch.pipeline().get( MinecraftDecoder.class ).setProtocol( protocol );
        ch.pipeline().get( MinecraftEncoder.class ).setProtocol( protocol );
    }
}

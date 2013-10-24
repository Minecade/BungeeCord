package net.md_5.bungee.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.AttributeKey;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.InitialHandlerSnapshot;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.protocol.MinecraftProtocol;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.snapshot.MinecraftDecoder;
import net.md_5.bungee.protocol.snapshot.MinecraftEncoder;
import net.md_5.bungee.protocol.snapshot.Varint21FrameDecoder;
import net.md_5.bungee.protocol.snapshot.Varint21LengthFieldPrepender;
import net.md_5.bungee.protocol.version.Snapshot;
import net.md_5.bungee.protocol.version.Vanilla;

public class PipelineUtils
{

    public static final AttributeKey<ListenerInfo> LISTENER = new AttributeKey<>( "ListerInfo" );
    public static final AttributeKey<UserConnection> USER = new AttributeKey<>( "User" );
    public static final AttributeKey<BungeeServerInfo> TARGET = new AttributeKey<>( "Target" );
    public static final AttributeKey<MinecraftProtocol> PROTOCOL = new AttributeKey<>( "Protocol" );
    public static final ChannelInitializer<Channel> SERVER_CHILD = new ChannelInitializer<Channel>()
    {
        @Override
        protected void initChannel(Channel ch) throws Exception
        {

            System.out.println("VERSION DECODER ADDED");
            ch.pipeline().addLast( VERSION_DECODER, new VersionDetector() );
        }
    };

    // GENERAL
    public static final Base BASE = new Base();
    public static final VanillaInitializer VANILLA_INIT = new VanillaInitializer();
    public static final SnapshotInitializer SNAPSHOT_INIT = new SnapshotInitializer();
    public static String BOSS_HANDLER = "inbound-boss";
    public static String TIMEOUT_HANDLER = "timeout";
    public static String ENCRYPT_HANDLER = "encrypt";
    public static String DECRYPT_HANDLER = "decrypt";
    public static String PACKET_DECODER = "packet-decoder";
    public static String PACKET_ENCODER = "packet-encoder";
    public static String VERSION_DECODER = "version-decoder";
    // 1.6
    public static final DefinedPacketEncoder packetEncoder = new DefinedPacketEncoder();
    // SNAPSHOT
    public static final Varint21LengthFieldPrepender framePrepender = new Varint21LengthFieldPrepender();
    public static String FRAME_DECODER = "frame-decoder";
    public static String FRAME_PREPENDER = "frame-prepender";

    public final static class Base extends ChannelInitializer<Channel>
    {
        @Override
        public void initChannel(Channel ch) throws Exception
        {
            initChannel(ch, null);
        }

        public void initChannel(Channel ch, Boolean snapshot) throws Exception
        {
            try
            {
                ch.config().setOption( ChannelOption.IP_TOS, 0x18 );
            } catch ( ChannelException ex )
            {
                // IP_TOS is not supported (Windows XP / Windows Server 2003)
            }

            ch.pipeline().addLast( TIMEOUT_HANDLER, new ReadTimeoutHandler( BungeeCord.getInstance().config.getTimeout(), TimeUnit.MILLISECONDS ) );
        }
    };

    public final static class VanillaInitializer extends ChannelInitializer<Channel>
    {
        public void initChannel(Channel ch) throws Exception
        {
           BASE.initChannel(ch);
           ch.attr( PipelineUtils.PROTOCOL ).set( Vanilla.getInstance() );

           ch.pipeline().addLast( PipelineUtils.PACKET_DECODER, new PacketDecoder( net.md_5.bungee.protocol.version.Vanilla.getInstance() ) );
           ch.pipeline().addLast( PipelineUtils.PACKET_ENCODER, PipelineUtils.packetEncoder );

           ch.pipeline().addLast( PipelineUtils.BOSS_HANDLER, new HandlerBoss() );
        }
    }

    public final static class SnapshotInitializer extends ChannelInitializer<Channel>
    {
        public void initChannel(Channel ch) throws Exception
        {
            BASE.initChannel(ch);
            ch.attr( PipelineUtils.PROTOCOL ).set( Snapshot.getInstance() );

            ch.pipeline().addLast( PipelineUtils.FRAME_DECODER, new Varint21FrameDecoder() );
            ch.pipeline().addLast( PipelineUtils.FRAME_PREPENDER, PipelineUtils.framePrepender );
            ch.pipeline().addAfter( PipelineUtils.FRAME_DECODER, PipelineUtils.PACKET_DECODER, new MinecraftDecoder( Snapshot.Protocol.HANDSHAKE, true ) );
            ch.pipeline().addAfter( PipelineUtils.FRAME_PREPENDER, PipelineUtils.PACKET_ENCODER, new MinecraftEncoder( Snapshot.Protocol.HANDSHAKE, true ) );

            ch.pipeline().addLast( PipelineUtils.BOSS_HANDLER, new HandlerBoss() );
        }
    }
}

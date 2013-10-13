package net.md_5.bungee.connection;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import com.google.common.base.Preconditions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.packet.PacketFFKick;

@RequiredArgsConstructor
public abstract class InitialHandlerAbstract extends PacketHandler implements PendingConnection {

    protected final ProxyServer bungee;
    @Getter
    protected ChannelWrapper ch;
    @Getter
    protected final ListenerInfo listener;
    protected State thisState = State.HANDSHAKE;
    protected SecretKey sharedKey;
    protected final Unsafe unsafe = new Unsafe()
    {
        @Override
        public void sendPacket(DefinedPacket packet)
        {
            ch.write( packet );
        }
    };
    @Getter
    protected List<DefinedPacket> registerMessages = new ArrayList<>();
    @Getter
    protected List<DefinedPacket> loginMessages = new ArrayList<>();
    @Getter
    protected boolean onlineMode = BungeeCord.getInstance().config.isOnlineMode();
    protected InetSocketAddress vHost;
    protected byte version = -1;
    @Getter
    protected DefinedPacket forgeLogin;
    @Getter
    protected DefinedPacket handshake;
    @Getter
    protected String UUID;

    protected enum State
    {
        // SHARED
        HANDSHAKE, ENCRYPT, FINISHED,
        // 1.6
        LOGIN,
        // SNAPSHOT
        STATUS, PING, USERNAME;
    }

    @Override
    public void connected(ChannelWrapper channel) throws Exception
    {
        this.ch = channel;
    }

    @Override
    public void exception(Throwable t) throws Exception
    {
        disconnect( ChatColor.RED + Util.exception( t ) );
    }

    public synchronized void disconnect(String reason)
    {
        if ( !ch.isClosed() )
        {
            unsafe().sendPacket( new PacketFFKick( reason ) );
            ch.close();
        }
    }

    @Override
    public InetSocketAddress getVirtualHost()
    {
        return vHost;
    }

    @Override
    public InetSocketAddress getAddress()
    {
        return (InetSocketAddress) ch.getHandle().remoteAddress();
    }

    @Override
    public Unsafe unsafe()
    {
        return unsafe;
    }

    public void setOnlineMode(boolean onlineMode)
    {
        Preconditions.checkState( thisState == State.HANDSHAKE, "Can only set online mode status whilst handshaking" );
        this.onlineMode = onlineMode;
    }

    @Override
    public String toString()
    {
        return "[" + ( ( getName() != null ) ? getName() : getAddress() ) + "] <-> InitialHandler";
    }
}

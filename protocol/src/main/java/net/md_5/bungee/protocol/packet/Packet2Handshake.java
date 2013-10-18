package net.md_5.bungee.protocol.packet;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Packet2Handshake extends DefinedPacket
{

    private byte protocolVersion;
    private String username;
    private String host;
    private int port;

    private Packet2Handshake()
    {
        super( 0x02 );
    }

    public Packet2Handshake(byte protocolVersion, String username, String host, int port)
    {
        this();
        this.protocolVersion = protocolVersion;
        this.username = username;
        this.host = host;
        this.port = port;
    }
    
    @Override
    public void read(ByteBuf buf)
    {
        protocolVersion = buf.readByte();
        username = readString( buf );
        host = readString( buf );
        port = buf.readInt();
    }

    @Override
    public void write(ByteBuf buf)
    {
        buf.writeByte( protocolVersion );
        writeString( username, buf );
        writeString( host, buf );
        buf.writeInt( port );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

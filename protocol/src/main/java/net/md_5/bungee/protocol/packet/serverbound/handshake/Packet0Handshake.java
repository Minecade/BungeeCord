package net.md_5.bungee.protocol.packet.serverbound.handshake;

import net.md_5.bungee.protocol.packet.AbstractPacketHandler;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Packet0Handshake extends DefinedPacket
{

    private byte protocolVersion;
    private String username;
    private String host;
    private int port;

    private Packet0Handshake()
    {
        super( 0x00 );
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

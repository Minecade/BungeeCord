package net.md_5.bungee.protocol.packet.snapshot;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Handshake extends DefinedPacket
{

    private int protocolVersion;
    private String host;
    private int port;
    private int requestedProtocol;

    @Override
    public void read(ByteBuf buf)
    {
        protocolVersion = readVarInt( buf );
        System.out.println( protocolVersion );
        host = readString( buf );
        System.out.println( host );
        port = buf.readUnsignedShort();
        System.out.println( port );
        requestedProtocol = readVarInt( buf );
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeVarInt( protocolVersion, buf );
        writeString( host, buf );
        buf.writeShort( port );
        writeVarInt( requestedProtocol, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

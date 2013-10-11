package net.md_5.bungee.protocol.packet.clientbound.play;

import net.md_5.bungee.protocol.packet.AbstractPacketHandler;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Packet3BPlayerListItem extends DefinedPacket
{

    private String username;
    private boolean online;
    private short ping;

    private Packet3BPlayerListItem()
    {
        super( 0x3B );
    }

    public Packet3BPlayerListItem(String username, boolean online, short ping)
    {
        super( 0x3B );
        this.username = username;
        this.online = online;
        this.ping = ping;
    }

    @Override
    public void read(ByteBuf buf)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeString( username, buf );
        buf.writeBoolean( online );
        buf.writeShort( ping );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

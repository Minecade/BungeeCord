package net.md_5.bungee.protocol.packet.snapshot.game;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerListItem extends DefinedPacket
{

    private String username;
    private boolean online;
    private short ping;

    public PlayerListItem()
    {
        setSnapshot(true);
    }

    @Override
    public void read(ByteBuf buf)
    {
        username = readString( buf );
        online = buf.readBoolean();
        ping = buf.readShort();
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

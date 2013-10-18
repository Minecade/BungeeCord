package net.md_5.bungee.protocol.packet.snapshot.login;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;

@Data
@EqualsAndHashCode(callSuper = false)
public class LoginSuccess extends DefinedPacket
{

    private String uuid;
    private String username;

    public LoginSuccess()
    {
        setSnapshot(true);
    }

    public LoginSuccess(String uuid, String username)
    {
        this();
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public void read(ByteBuf buf)
    {
        uuid = readString( buf );
        username = readString( buf );
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeString( uuid, buf );
        writeString( username, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

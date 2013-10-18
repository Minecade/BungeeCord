package net.md_5.bungee.protocol.packet.snapshot.login;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;

@Data
@EqualsAndHashCode(callSuper = false)
public class LoginRequest extends DefinedPacket
{

    private String data;

    public LoginRequest()
    {
        setSnapshot(true);
    }

    @Override
    public void read(ByteBuf buf)
    {
        data = readString( buf );
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeString( data, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}
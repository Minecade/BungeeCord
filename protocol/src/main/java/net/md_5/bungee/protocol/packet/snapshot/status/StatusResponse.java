package net.md_5.bungee.protocol.packet.snapshot.status;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;

@Data
@EqualsAndHashCode(callSuper = false)
public class StatusResponse extends DefinedPacket
{

    private String response;

    public StatusResponse()
    {
        setSnapshot(true);
    }

    public StatusResponse(String response)
    {
        this();
        this.response = response;
    }

    @Override
    public void read(ByteBuf buf)
    {
        response = readString( buf );
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeString( response, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}
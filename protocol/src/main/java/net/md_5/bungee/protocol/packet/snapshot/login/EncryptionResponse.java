package net.md_5.bungee.protocol.packet.snapshot.login;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class EncryptionResponse extends DefinedPacket
{

    private byte[] sharedSecret;
    private byte[] verifyToken;

    public EncryptionResponse()
    {
        setSnapshot(true);
    }

    @Override
    public void read(ByteBuf buf)
    {
        sharedSecret = readArray( buf );
        verifyToken = readArray( buf );
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeArray( sharedSecret, buf );
        writeArray( verifyToken, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

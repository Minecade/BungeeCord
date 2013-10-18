package net.md_5.bungee.protocol.packet.snapshot.login;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;

@Data
@EqualsAndHashCode(callSuper = false)
public class EncryptionRequest extends DefinedPacket
{

    private String serverId;
    private byte[] publicKey;
    private byte[] verifyToken;

    public EncryptionRequest()
    {
        setSnapshot(true);
    }

    public EncryptionRequest(String serverId, byte[] publicKey, byte[] verifyToken)
    {
        this();
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
    }

    @Override
    public void read(ByteBuf buf)
    {
        serverId = readString( buf );
        publicKey = readArray( buf );
        verifyToken = readArray( buf );
    }

    @Override
    public void write(ByteBuf buf)
    {
        System.out.println("Writing string " + serverId);
        writeString( serverId, buf );
        System.out.println("Writing array " + publicKey);
        writeArray( publicKey, buf );
        System.out.println("Writing array" + verifyToken);
        writeArray( verifyToken, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

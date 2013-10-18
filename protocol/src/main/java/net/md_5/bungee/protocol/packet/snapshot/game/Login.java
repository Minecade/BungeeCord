package net.md_5.bungee.protocol.packet.snapshot.game;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Login extends DefinedPacket
{

    protected int entityId;
    protected short gameMode;
    protected int dimension;
    protected short difficulty;
    protected short maxPlayers;

    @Override
    public void read(ByteBuf buf)
    {
        entityId = buf.readInt();
        gameMode = buf.readUnsignedByte();
        dimension = buf.readByte();
        difficulty = buf.readByte();
        maxPlayers = buf.readUnsignedByte();
    }

    @Override
    public void write(ByteBuf buf)
    {
        buf.writeInt( entityId );
        buf.writeByte( gameMode );
        buf.writeByte( dimension );
        buf.writeByte( difficulty );
        buf.writeByte( maxPlayers );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

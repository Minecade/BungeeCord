package net.md_5.bungee.protocol.packet.snapshot;

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
    protected String levelType;
    protected byte gameMode;
    protected int dimension;
    protected byte difficulty;
    protected byte unused;
    protected byte maxPlayers;

    @Override
    public void read(ByteBuf buf)
    {
        entityId = buf.readInt();
        levelType = readString( buf );
        gameMode = buf.readByte();
        dimension = buf.readByte();
        difficulty = buf.readByte();
        unused = buf.readByte();
        maxPlayers = buf.readByte();
    }

    @Override
    public void write(ByteBuf buf)
    {
        buf.writeInt( entityId );
        writeString( levelType, buf );
        buf.writeByte( gameMode );
        buf.writeByte( dimension );
        buf.writeByte( difficulty );
        buf.writeByte( unused );
        buf.writeByte( maxPlayers );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

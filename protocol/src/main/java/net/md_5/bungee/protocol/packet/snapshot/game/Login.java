package net.md_5.bungee.protocol.packet.snapshot.game;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class Login extends DefinedPacket
{

    protected int entityId;
    protected short gameMode;
    protected int dimension;
    protected short difficulty;
    protected short maxPlayers;
    protected String levelType;

    public Login()
    {
        setSnapshot(true);
    }

    public Login(int entityId, short gameMode, int dimension, short difficulty, short maxPlayers, String levelType)
    {
        this();
        this.entityId = entityId;
        this.gameMode = gameMode;
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
        this.levelType = levelType;
    }

    @Override
    public void read(ByteBuf buf)
    {
        entityId = buf.readInt();
        gameMode = buf.readUnsignedByte();
        dimension = buf.readByte();
        difficulty = buf.readByte();
        maxPlayers = buf.readUnsignedByte();
        levelType = readString( buf );
    }

    @Override
    public void write(ByteBuf buf)
    {
        buf.writeInt( entityId );
        buf.writeByte( gameMode );
        buf.writeByte( dimension );
        buf.writeByte( difficulty );
        buf.writeByte( maxPlayers );
        writeString( levelType, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

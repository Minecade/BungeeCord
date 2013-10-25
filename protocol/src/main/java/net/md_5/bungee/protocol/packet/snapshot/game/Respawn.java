package net.md_5.bungee.protocol.packet.snapshot.game;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class Respawn extends DefinedPacket
{
    private int dimension;
    private short difficulty;
    private short gameMode;
    private String levelType;

    public Respawn()
    {
        setSnapshot(true);
    }

    public Respawn(int dimension, short difficulty, short gameMode, String levelType)
    {
        this();
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.levelType = levelType;
    }

    @Override
    public void read(ByteBuf buf)
    {
        dimension = buf.readInt();
        difficulty = buf.readUnsignedByte();
        gameMode = buf.readUnsignedByte();
        levelType = readString( buf );
    }

    @Override
    public void write(ByteBuf buf)
    {
        buf.writeInt( dimension );
        buf.writeByte( difficulty );
        buf.writeByte( gameMode );
        writeString( levelType, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

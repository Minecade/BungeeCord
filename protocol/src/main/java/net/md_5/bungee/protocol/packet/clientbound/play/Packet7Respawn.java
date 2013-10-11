package net.md_5.bungee.protocol.packet.clientbound.play;

import net.md_5.bungee.protocol.packet.AbstractPacketHandler;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = false)
public class Packet7Respawn extends DefinedPacket
{

    private int dimension;
    private byte difficulty;
    private byte gameMode;
    private short worldHeight;
    private String levelType;

    private Packet7Respawn()
    {
        super( 0x07 );
    }

    public Packet7Respawn(int dimension, byte difficulty, byte gameMode, short worldHeight, String levelType)
    {
        this();
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.worldHeight = worldHeight;
        this.levelType = levelType;
    }

    @Override
    public void read(ByteBuf buf)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(ByteBuf buf)
    {
        buf.writeInt( dimension );
        buf.writeByte( difficulty );
        buf.writeByte( gameMode );
        buf.writeShort( worldHeight );
        writeString( levelType, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

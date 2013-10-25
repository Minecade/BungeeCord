package net.md_5.bungee.protocol.packet.snapshot.game;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateHealth extends DefinedPacket
{

    private float health;
    private short food;
    private float saturation;

    public UpdateHealth()
    {
        setSnapshot(true);
    }

    public UpdateHealth(float health, short food, float saturation)
    {
        this();
        this.health = health;
        this.food = food;
        this.saturation = saturation;
    }

    @Override
    public void read(ByteBuf buf)
    {
        health = buf.readFloat();
        food = buf.readShort();
        saturation = buf.readFloat();
    }

    @Override
    public void write(ByteBuf buf)
    {
        buf.writeFloat( health );
        buf.writeShort( food );
        buf.writeFloat( saturation );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

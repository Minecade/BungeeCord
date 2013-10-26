package net.md_5.bungee.protocol.packet;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class PacketCCSettings extends DefinedPacket
{

    private String locale;
    private byte viewDistance;
    private byte chatFlags;
    private byte difficulty;
    private boolean showCape;

    private PacketCCSettings()
    {
        super( 0xCC );
    }

    public PacketCCSettings(String locale, byte viewDistance, byte chatFlags, byte difficulty, boolean showCape)
    {
        this();
        this.locale = locale;
        this.viewDistance = viewDistance;
        this.chatFlags = chatFlags;
        this.difficulty = difficulty;
        this.showCape = showCape;
    }

    @Override
    public void read(ByteBuf buf)
    {
        locale = readString( buf );
        viewDistance = buf.readByte();
        chatFlags = buf.readByte();
        difficulty = buf.readByte();
        showCape = buf.readBoolean();
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeString( locale, buf );
        buf.writeByte( viewDistance );
        buf.writeByte( chatFlags );
        buf.writeByte( difficulty );
        buf.writeBoolean( showCape );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}

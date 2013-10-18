package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class BlockChangeTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeByte(snapshot.readUnsignedByte());
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeShort(PacketUtil.readVarInt(snapshot));
        vanilla.writeByte(snapshot.readUnsignedByte());
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        snapshot.writeInt(vanilla.readInt());
        snapshot.writeByte(vanilla.readByte());
        snapshot.writeInt(vanilla.readInt());
        PacketUtil.writeVarInt(vanilla.readShort(), snapshot);
        snapshot.writeByte(vanilla.readByte());
    }
}

package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class BlockActionTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeShort(snapshot.readShort());
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeByte(snapshot.readUnsignedByte());
        vanilla.writeByte(snapshot.readUnsignedByte());
        vanilla.writeShort(PacketUtil.readVarInt(snapshot));
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        snapshot.writeInt(vanilla.readInt());
        snapshot.writeShort(vanilla.readShort());
        snapshot.writeInt(vanilla.readInt());
        snapshot.writeByte(vanilla.readByte());
        snapshot.writeByte(vanilla.readByte());
        PacketUtil.writeVarInt(vanilla.readShort(), snapshot);
    }
}

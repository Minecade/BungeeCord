package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class RespawnTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeByte(snapshot.readUnsignedByte());
        vanilla.writeByte(snapshot.readUnsignedByte());
        vanilla.writeShort(256);
        PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        snapshot.writeInt(vanilla.readInt());
        snapshot.writeByte(vanilla.readByte());
        snapshot.writeByte(vanilla.readByte());
        vanilla.readShort();
        PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
    }

}

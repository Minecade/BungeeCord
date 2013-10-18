package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class ScoreboardScoreTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
        vanilla.writeByte(snapshot.readByte());
        PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
        vanilla.writeInt(snapshot.readInt());
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
        snapshot.writeByte(vanilla.readByte());
        PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
        snapshot.writeInt(vanilla.readInt());
    }
}

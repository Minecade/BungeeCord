package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class ScoreboardObjectiveTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
        PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
        finish(snapshot, vanilla);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
        PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
        finish(vanilla, snapshot);
    }
}

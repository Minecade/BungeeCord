package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class PaintingTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeInt(PacketUtil.readVarInt(snapshot));
        PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
        finish(snapshot, vanilla);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        PacketUtil.writeVarInt(vanilla.readInt(), snapshot);
        PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
        finish(vanilla, snapshot);
    }
}

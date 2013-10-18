package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class SpawnPlayerTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeInt(PacketUtil.readVarInt(snapshot));
        PacketUtil.readSnapshotString(snapshot);
        PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
        finish(snapshot, vanilla);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        PacketUtil.writeVarInt(vanilla.readInt(), snapshot);
        PacketUtil.writeSnapshotString("fca0357066a24b68b8c28a76b4f21514", snapshot); // TODO - UUID?
        PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
        finish(vanilla, snapshot);
    }
}

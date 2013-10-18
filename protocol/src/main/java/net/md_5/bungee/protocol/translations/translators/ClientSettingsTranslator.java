package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class ClientSettingsTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
        vanilla.writeByte(snapshot.readByte());
        vanilla.writeByte(snapshot.readByte());
        snapshot.readBoolean();
        finish(snapshot, vanilla);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        PacketUtil.writeSnapshotString(PacketUtil.readSnapshotString(vanilla), snapshot);
        snapshot.writeByte(vanilla.readByte());
        snapshot.writeByte(vanilla.readByte());
        snapshot.writeBoolean(true);
        finish(vanilla, snapshot);
    }
}

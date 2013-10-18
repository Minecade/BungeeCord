package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class OpenWindowTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeByte(snapshot.readUnsignedByte());
        vanilla.writeByte(snapshot.readUnsignedByte());
        PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
        vanilla.writeByte(snapshot.readUnsignedByte());
        finish(snapshot, vanilla);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        snapshot.writeByte(vanilla.readByte());
        snapshot.writeByte(vanilla.readByte());
        PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
        finish(vanilla, snapshot);
    }
}

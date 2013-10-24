package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class SpawnPlayerTranslator extends Translator
{
    private static final MetadataTranslator metadata = new MetadataTranslator();

    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeInt(PacketUtil.readVarInt(snapshot));
        PacketUtil.readSnapshotString(snapshot);
        PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeByte(snapshot.readByte());
        vanilla.writeByte(snapshot.readByte());
        vanilla.writeShort(snapshot.readShort());
        metadata.snapshotToVanilla(snapshot, vanilla);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        PacketUtil.writeVarInt(vanilla.readInt(), snapshot);
        PacketUtil.writeSnapshotString("fca0357066a24b68b8c28a76b4f21514", snapshot); // TODO - UUID?
        PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
        snapshot.writeInt(vanilla.readInt());
        snapshot.writeInt(vanilla.readInt());
        snapshot.writeInt(vanilla.readInt());
        snapshot.writeByte(vanilla.readByte());
        snapshot.writeByte(vanilla.readByte());
        snapshot.writeShort(vanilla.readShort());
        metadata.vanillaToSnapshot(vanilla, snapshot);
    }
}

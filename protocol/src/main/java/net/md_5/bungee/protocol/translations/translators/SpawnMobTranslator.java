package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class SpawnMobTranslator extends Translator
{
    private static final MetadataTranslator metadata = new MetadataTranslator();

    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeInt(PacketUtil.readVarInt(snapshot));
        vanilla.writeByte(snapshot.readUnsignedByte());
        metadata.snapshotToVanilla(snapshot, vanilla);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        PacketUtil.writeVarInt(vanilla.readInt(), snapshot);
        snapshot.writeByte(vanilla.readByte());
        metadata.vanillaToSnapshot(vanilla, snapshot);
    }
}

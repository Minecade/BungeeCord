package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.translations.Translator;

public class EntityMetadataTranslator extends Translator
{
    private static final MetadataTranslator metadata = new MetadataTranslator();

    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeInt(snapshot.readInt());
        metadata.snapshotToVanilla(snapshot, vanilla);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        snapshot.writeInt(vanilla.readInt());
        metadata.vanillaToSnapshot(vanilla, snapshot);
    }
}

package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.translations.Translator;

public class ClientStatusTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeByte(snapshot.readByte() + 1);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        snapshot.writeByte(vanilla.readByte() - 1);
    }
}

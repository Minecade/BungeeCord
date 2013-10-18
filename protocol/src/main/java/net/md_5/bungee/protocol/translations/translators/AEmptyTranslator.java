package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.translations.Translator;

public class AEmptyTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
    }
}

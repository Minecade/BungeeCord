package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.translations.Translator;

public class PlayerPositionLookTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeDouble(snapshot.readDouble());
        vanilla.writeDouble(1d); // stance - todo - a better value?
        finish(snapshot, vanilla);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        snapshot.writeDouble(vanilla.readDouble());
        vanilla.readDouble(); // stance
        finish(vanilla, snapshot);
    }
}

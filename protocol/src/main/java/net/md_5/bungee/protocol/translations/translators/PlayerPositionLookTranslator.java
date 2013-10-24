package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.translations.Translator;

public class PlayerPositionLookTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeDouble(snapshot.readDouble());

        double y = snapshot.readDouble();
        vanilla.writeDouble(y + 1.6200000047683716D); // stance - todo - a better value?
        vanilla.writeDouble(y);

        finish(snapshot, vanilla);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        snapshot.writeDouble(vanilla.readDouble());
        snapshot.writeDouble(vanilla.readDouble());
        vanilla.readDouble(); // stance
        snapshot.writeDouble(vanilla.readDouble());

        finish(vanilla, snapshot);
    }
}

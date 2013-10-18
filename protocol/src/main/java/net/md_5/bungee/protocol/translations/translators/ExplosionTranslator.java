package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.translations.Translator;

public class ExplosionTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeDouble(snapshot.readFloat());
        vanilla.writeDouble(snapshot.readFloat());
        vanilla.writeDouble(snapshot.readFloat());
        finish(snapshot, vanilla);
    }

    @Override
    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        snapshot.writeFloat((float) vanilla.readDouble());
        snapshot.writeFloat((float) vanilla.readDouble());
        snapshot.writeFloat((float) vanilla.readDouble());
        finish(vanilla, snapshot);
    }
}

package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.translations.Translator;

public class AnimationTranslator2 extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeByte(snapshot.readUnsignedByte() + 1);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        snapshot.writeInt(vanilla.readInt());
        snapshot.writeByte(vanilla.readByte() - 1);
    }
}

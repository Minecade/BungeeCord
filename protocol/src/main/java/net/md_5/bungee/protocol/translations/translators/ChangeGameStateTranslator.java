package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.translations.Translator;

public class ChangeGameStateTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeByte(snapshot.readUnsignedByte());
        vanilla.writeByte((byte) snapshot.readFloat());
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        snapshot.writeByte(vanilla.readByte());
        snapshot.writeFloat((float) vanilla.readByte());
    }
}

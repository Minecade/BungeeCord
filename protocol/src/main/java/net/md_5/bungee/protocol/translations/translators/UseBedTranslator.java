package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.translations.Translator;

public class UseBedTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeByte(0);
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeByte(snapshot.readUnsignedByte());
        vanilla.writeInt(snapshot.readInt());
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        snapshot.writeInt(vanilla.readInt());
        vanilla.readByte();
        snapshot.writeInt(vanilla.readInt());
        snapshot.writeByte(vanilla.readByte());
        snapshot.writeInt(vanilla.readInt());
    }
}

package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class AnimationTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeInt(PacketUtil.readVarInt(snapshot));
        vanilla.writeByte(snapshot.readUnsignedByte() + 1);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        PacketUtil.writeVarInt(vanilla.readInt(), snapshot);
        snapshot.writeByte(vanilla.readByte() - 1);
    }
}

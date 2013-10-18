package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class MapsTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeShort(358); // map id
        vanilla.writeShort(PacketUtil.readVarInt(snapshot));
        finish(snapshot, vanilla);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        vanilla.readShort();
        PacketUtil.writeVarInt(vanilla.readShort(), snapshot);
        finish(vanilla, snapshot);
    }
}

package net.md_5.bungee.protocol.translations.translators;

import com.google.common.base.Joiner;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class TabCompleteResponseTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        String[] results = PacketUtil.readStringArray(snapshot);
        PacketUtil.writeVanillaString(Joiner.on("\0").join(results), vanilla);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        String[] inputs = PacketUtil.readVanillaString(vanilla).split("\0");
        PacketUtil.writeStringArray(inputs, snapshot);
    }
}

package net.md_5.bungee.protocol.translations.translators;

import lombok.RequiredArgsConstructor;

import com.google.gson.Gson;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class KickTranslator extends Translator
{
    private Gson gson = new Gson();

    @RequiredArgsConstructor
    class KickMessage {
        public final String text;
    }

    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        String fromJson = PacketUtil.readSnapshotString(snapshot);
        PacketUtil.writeVanillaString(gson.fromJson(fromJson, KickMessage.class).text, vanilla);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        String toJson = PacketUtil.readVanillaString(vanilla);
        PacketUtil.writeSnapshotString(gson.toJson(new KickMessage(toJson)), snapshot);
    }
}

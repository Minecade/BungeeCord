package net.md_5.bungee.protocol.translations.translators;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class AnimationTranslatorClientbound extends Translator
{
    private final BiMap<Integer, Integer> vanillaToSnapshot = HashBiMap.create();
    private final BiMap<Integer, Integer> snapshotToVanilla;
    private Integer action;

    public AnimationTranslatorClientbound()
    {
        vanillaToSnapshot.put(0, -1);
        vanillaToSnapshot.put(1, 0);
        vanillaToSnapshot.put(2, 1);
        vanillaToSnapshot.put(3, 2);
        vanillaToSnapshot.put(5, 3);
        vanillaToSnapshot.put(6, 4);
        vanillaToSnapshot.put(7, 5);
        snapshotToVanilla = vanillaToSnapshot.inverse();
    }

    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeInt(PacketUtil.readVarInt(snapshot));
        vanilla.writeByte(snapshotToVanilla.get(snapshot.readUnsignedByte()));
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        PacketUtil.writeVarInt(vanilla.readInt(), snapshot);
        snapshot.writeByte(vanillaToSnapshot.get(vanilla.readByte()));
    }
}

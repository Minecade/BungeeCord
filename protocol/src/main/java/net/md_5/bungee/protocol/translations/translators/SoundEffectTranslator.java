package net.md_5.bungee.protocol.translations.translators;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class SoundEffectTranslator extends Translator
{
    private final BiMap<String, String> vanillaToSnapshot = HashBiMap.create();
    private final BiMap<String, String> snapshotToVanilla;

    public SoundEffectTranslator()
    {
        vanillaToSnapshot.put("random.fuse", "game.tnt.primed");
        vanillaToSnapshot.put("damage.hit", "game.player.hurt");
        snapshotToVanilla = vanillaToSnapshot.inverse();
    }

    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        PacketUtil.writeVanillaString(snapshotToVanillaSound(snapshot), vanilla);
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeFloat(snapshot.readFloat());
        vanilla.writeByte(snapshot.readUnsignedByte());
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        PacketUtil.writeSnapshotString(vanillaToSnapshotSound(vanilla), snapshot);
        finish(vanilla, snapshot);
    }

    private String snapshotToVanillaSound(ByteBuf snapshot)
    {
        String original = PacketUtil.readSnapshotString(snapshot);
        String translation = snapshotToVanilla.get(original);
        System.out.println("S2V original: " + original);
        System.out.println("S2V translation: " + translation);
        return translation == null ? original : translation;
    }

    private String vanillaToSnapshotSound(ByteBuf vanilla)
    {
        String original = PacketUtil.readVanillaString(vanilla);
        String translation = vanillaToSnapshot.get(original);
        System.out.println("V2S original: " + original);
        System.out.println("V2S translation: " + translation);
        return translation == null ? original : translation;
    }
}

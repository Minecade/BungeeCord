package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class UpdateSignTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeShort(snapshot.readShort());
        vanilla.writeInt(snapshot.readInt());
        for(int i = 0; i < 4; i++)
        {
            PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
        }
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        snapshot.writeInt(vanilla.readInt());
        snapshot.writeShort(vanilla.readShort());
        snapshot.writeInt(vanilla.readInt());
        for(int i = 0; i < 4; i++)
        {
            PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
        }
    }
}

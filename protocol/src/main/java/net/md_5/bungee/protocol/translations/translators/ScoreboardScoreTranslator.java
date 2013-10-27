package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class ScoreboardScoreTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
        int action = snapshot.readByte();
        String scoreName = PacketUtil.readSnapshotString(snapshot);
        int value = snapshot.readInt();

        vanilla.writeByte(action);
        if ( action != 1 )
        {
            PacketUtil.writeVanillaString(scoreName, vanilla);
            vanilla.writeInt(value);
        }
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
        int action = vanilla.readByte();
        String scoreName;
        int value;

        snapshot.writeByte(action);
        if ( action != 1 )
        {
            scoreName = PacketUtil.readVanillaString(vanilla);
            value = vanilla.readInt();
            PacketUtil.writeSnapshotString(scoreName, snapshot);
            snapshot.writeInt(value);
        }
    }
}

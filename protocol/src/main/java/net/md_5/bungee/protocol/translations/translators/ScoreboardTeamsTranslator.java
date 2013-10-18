package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class ScoreboardTeamsTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
        int mode = snapshot.readByte();
        vanilla.writeByte(mode);
        if ( mode == 0 || mode == 2 )
        {
            PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
            PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
            PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
            vanilla.writeBoolean(snapshot.readBoolean());
        }
        if ( mode == 0 || mode == 3 || mode == 4 )
        {
            int count = snapshot.readShort();
            vanilla.writeShort(count);
            for ( int i = 0; i < count; i++ )
            {
                PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
            }
        }
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        PacketUtil.writeSnapshotString(PacketUtil.readSnapshotString(vanilla), snapshot);
        int mode = vanilla.readByte();
        snapshot.writeByte(mode);
        if ( mode == 0 || mode == 2 )
        {
            PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
            PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
            PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
            vanilla.writeBoolean(snapshot.readBoolean());
        }
        if ( mode == 0 || mode == 3 || mode == 4 )
        {
            int count = vanilla.readShort();
            snapshot.writeShort(count);
            for ( int i = 0; i < count; i++ )
            {
                PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
            }
        }
    }
}

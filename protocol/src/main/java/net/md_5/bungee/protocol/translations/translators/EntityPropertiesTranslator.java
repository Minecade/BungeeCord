package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class EntityPropertiesTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeInt(snapshot.readInt());
        int records = vanilla.readInt();
        snapshot.writeInt(records);
        
        for ( int i = 0; i < records; i++ )
        {
            PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
            vanilla.writeDouble(snapshot.readDouble());
            short size = snapshot.readShort();
            vanilla.writeShort(size);
            
            for ( short s = 0; s < size; s++ )
            {
                vanilla.writeBytes(snapshot.readBytes(25)); // long, long, double, byte
            }
        }
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        snapshot.writeInt(vanilla.readInt());
        int records = vanilla.readInt();
        snapshot.writeInt(records);
        
        for ( int i = 0; i < records; i++ )
        {
            PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
            snapshot.writeDouble(vanilla.readDouble());
            short size = vanilla.readShort();
            snapshot.writeShort(size);
            
            for ( short s = 0; s < size; s++ )
            {
                snapshot.writeBytes(vanilla.readBytes(25)); // long, long, double, byte
            }
        }
    }
}

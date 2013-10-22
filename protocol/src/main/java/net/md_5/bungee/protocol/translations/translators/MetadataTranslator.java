package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class MetadataTranslator extends Translator
{
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        for( byte index = snapshot.readByte(); index != 127; index = snapshot.readByte() )
        {
            vanilla.writeByte(index);
            int id = (index & 244 ) >> 5;

            switch ( id )
            {
                case 0:
                    vanilla.writeByte(snapshot.readByte());
                    break;

                case 1:
                    vanilla.writeShort(snapshot.readShort());
                    break;

                case 2:
                    vanilla.writeInt(snapshot.readInt());
                    break;

                case 3:
                    vanilla.writeFloat(snapshot.readFloat());
                    break;

                case 4:
                    PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
                    break;

                case 5:
                    short value = snapshot.readShort();
                    vanilla.writeShort(value);
                    if ( value >= 0 )
                    {
                        vanilla.writeByte(snapshot.readByte());
                        vanilla.writeShort(snapshot.readShort());

                        short length = snapshot.readShort();
                        vanilla.writeShort(length);
                        if ( length >= 0 )
                        {
                            vanilla.writeBytes(snapshot.readBytes(length));
                        }
                    }
                    break;

                case 6:
                    vanilla.writeInt(snapshot.readInt());
                    vanilla.writeInt(snapshot.readInt());
                    vanilla.writeInt(snapshot.readInt());
                    break;
            }
        }

        vanilla.writeByte(127);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        for( byte index = vanilla.readByte(); index != 127; index = vanilla.readByte() )
        {
            snapshot.writeByte(index);
            int id = (index & 244 ) >> 5;

            switch ( id )
            {
                case 0:
                    snapshot.writeByte(vanilla.readByte());
                    break;

                case 1:
                    snapshot.writeShort(vanilla.readShort());
                    break;

                case 2:
                    snapshot.writeInt(vanilla.readInt());
                    break;

                case 3:
                    snapshot.writeFloat(vanilla.readFloat());
                    break;

                case 4:
                    PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
                    break;

                case 5:
                    short value = vanilla.readShort();
                    snapshot.writeShort(value);
                    if ( value >= 0 )
                    {
                        snapshot.writeByte(vanilla.readByte());
                        snapshot.writeShort(vanilla.readShort());

                        short length = vanilla.readShort();
                        snapshot.writeShort(length);
                        if ( length >= 0 )
                        {
                            snapshot.writeBytes(vanilla.readBytes(length));
                        }
                    }
                    break;

                case 6:
                    snapshot.writeInt(vanilla.readInt());
                    snapshot.writeInt(vanilla.readInt());
                    snapshot.writeInt(vanilla.readInt());
                    break;
            }
        }

        snapshot.writeByte(127);
    }
}

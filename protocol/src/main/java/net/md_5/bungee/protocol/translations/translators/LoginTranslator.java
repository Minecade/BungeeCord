package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.translations.Translator;

public class LoginTranslator extends Translator {
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        int entity = snapshot.readInt();
        short gamemode = snapshot.readUnsignedByte();
        byte dimension = snapshot.readByte();
        short difficulty = snapshot.readUnsignedByte();
        short maxPlayers = snapshot.readUnsignedByte();
        String level = PacketUtil.readSnapshotString(snapshot);
        
        vanilla.writeInt(entity);
        PacketUtil.writeVanillaString(level, vanilla);
        vanilla.writeByte(gamemode);
        vanilla.writeByte(dimension);
        vanilla.writeByte(difficulty);
        vanilla.writeByte(0);
        vanilla.writeByte(maxPlayers);
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        snapshot.writeInt(vanilla.readInt());

        String level = PacketUtil.readVanillaString(vanilla);
        
        snapshot.writeByte(vanilla.readByte());
        snapshot.writeByte(vanilla.readByte());
        snapshot.writeByte(vanilla.readByte());
        vanilla.readByte();
        snapshot.writeByte(vanilla.readByte());
        PacketUtil.writeSnapshotString(level, snapshot);
    }
}

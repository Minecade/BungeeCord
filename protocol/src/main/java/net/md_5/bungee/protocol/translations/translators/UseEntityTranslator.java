package net.md_5.bungee.protocol.translations.translators;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.translations.Translator;

public class UseEntityTranslator extends Translator {
    public void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
    {
        vanilla.writeInt(0);
        vanilla.writeInt(snapshot.readInt());
        vanilla.writeBoolean(snapshot.readBoolean());
    }

    public void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
    {
        vanilla.readInt();
        snapshot.writeInt(vanilla.readInt());
        snapshot.writeBoolean(vanilla.readBoolean());
    }

}

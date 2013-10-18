package net.md_5.bungee.protocol.translations;

import io.netty.buffer.ByteBuf;

public abstract class Translator
{
    public abstract void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla);
    
    public abstract void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot);
    
    protected void finish(ByteBuf in, ByteBuf out)
    {
        out.writeBytes(in.readBytes(in.readableBytes()));
    }
}
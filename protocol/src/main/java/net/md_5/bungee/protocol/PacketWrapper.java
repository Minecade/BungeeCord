package net.md_5.bungee.protocol;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class PacketWrapper
{

    @Getter
    private final int packetId;
    @Getter
    private final DefinedPacket packet;
    private final ByteBuf buf;
    @Getter
    private final boolean isSnapshot;
    @Setter
    private boolean released;

    public ByteBuf getBufRaw()
    {
        return this.buf;
    }
    
    public ByteBuf getBufCopy()
    {
        return this.buf.copy();
    }
    
    public void trySingleRelease()
    {
        if ( !released )
        {
            buf.release();
            released = true;
        }
    }
}

package net.md_5.bungee.protocol;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MinecraftInput
{

    private final ByteBuf buf;

    public byte readByte()
    {
        return buf.readByte();
    }

    public short readUnisgnedByte()
    {
        return buf.readUnsignedByte();
    }

    public int readInt()
    {
        return buf.readInt();
    }

    public String readString()
    {
        short len = buf.readShort();
        char[] c = new char[ len ];
        for ( int i = 0; i < c.length; i++ )
        {
            c[i] = buf.readChar();
        }

        return new String( c );
    }
    
    // TODO
    public int readVarInt()
    {
        int var1 = 0;
        int var2 = 0;

        byte var3;

        do {
            var3 = this.readByte();
            var1 |= (var3 & 127) << var2++ * 7;
            if (var2 > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((var3 & 128) == 128);

        return var1;
    }
}

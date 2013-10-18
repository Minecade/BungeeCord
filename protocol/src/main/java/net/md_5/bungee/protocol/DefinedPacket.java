package net.md_5.bungee.protocol;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public abstract class DefinedPacket
{

    @Getter
    private Integer id;

    public void writeString(String s, ByteBuf buf)
    {
        if ( id == null )
        {
            PacketUtil.writeSnapshotString(s, buf);
        } else
        {
            PacketUtil.writeVanillaString(s, buf);
        }
    }

    public String readString(ByteBuf buf)
    {
        if ( id == null )
        {
            return PacketUtil.readSnapshotString(buf);
        } else
        {
            return PacketUtil.readVanillaString(buf);
        }
    }

    public void writeArray(byte[] b, ByteBuf buf)
    {
        PacketUtil.writeArray(b, buf);
    }

    public byte[] readArray(ByteBuf buf)
    {
        return PacketUtil.readArray(buf);
    }

    public void writeStringArray(String[] s, ByteBuf buf)
    {
        PacketUtil.writeStringArray(s, buf);
    }

    public String[] readStringArray(ByteBuf buf)
    {
        return PacketUtil.readStringArray(buf);
    }

    public static int readVarInt(ByteBuf input)
    {
        return PacketUtil.readVarInt(input);
    }

    public static void writeVarInt(int value, ByteBuf output)
    {
        PacketUtil.writeVarInt(value, output);
    }

    public abstract void read(ByteBuf buf);

    public abstract void write(ByteBuf buf);

    public abstract void handle(AbstractPacketHandler handler) throws Exception;

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}

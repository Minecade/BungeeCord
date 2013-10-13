package net.md_5.bungee.protocol;

import com.google.common.base.Charsets;

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
        // TODO: Check len - use Guava?
        if ( id == null )
        {
            byte[] b = s.getBytes( Charsets.UTF_8 );
            writeVarInt( b.length, buf );
            buf.writeBytes( b );
        } else
        {
            buf.writeShort( s.length() );
            for ( char c : s.toCharArray() )
            {
                buf.writeChar( c );
            }
        }
    }

    public String readString(ByteBuf buf)
    {
        // TODO: Check len - use Guava?
        if ( id == null )
        {
            int len = readVarInt( buf );
            byte[] b = new byte[ len ];
            buf.readBytes( b );

            return new String( b, Charsets.UTF_8 );
        } else
        {
            short len = buf.readShort();
            char[] chars = new char[ len ];
            for ( int i = 0; i < len; i++ )
            {
                chars[i] = buf.readChar();
            }
            return new String( chars );
        }
    }

    public void writeArray(byte[] b, ByteBuf buf)
    {
        // TODO: Check len - use Guava?
        buf.writeShort( b.length );
        buf.writeBytes( b );
    }

    public byte[] readArray(ByteBuf buf)
    {
        // TODO: Check len - use Guava?
        short len = buf.readShort();
        byte[] ret = new byte[ len ];
        buf.readBytes( ret );
        return ret;
    }

    public static int readVarInt(ByteBuf input)
    {
        int out = 0;
        int bytes = 0;
        byte in;
        while ( true )
        {
            in = input.readByte();

            out |= ( in & 0x7F ) << ( bytes++ * 7 );

            if ( bytes > 32 )
            {
                throw new RuntimeException( "VarInt too big" );
            }

            if ( ( in & 0x80 ) != 0x80 )
            {
                break;
            }
        }

        return out;
    }

    public static void writeVarInt(int value, ByteBuf output)
    {
        int part;
        while ( true )
        {
            part = value & 0x7F;

            value >>>= 7;
            if ( value != 0 )
            {
                part |= 0x80;
            }

            output.writeByte( part );

            if ( value == 0 )
            {
                break;
            }
        }
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

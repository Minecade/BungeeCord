package net.md_5.bungee.protocol;

import io.netty.buffer.ByteBuf;

import com.google.common.base.Charsets;

public class PacketUtil {
    // VANILLA
    public static void writeVanillaString(String s, ByteBuf buf) {
        buf.writeShort( s.length() );
        for ( char c : s.toCharArray() )
        {
            buf.writeChar( c );
        }
    }

    public static String readVanillaString(ByteBuf buf) {
        short len = buf.readShort();
        char[] chars = new char[ len ];
        for ( int i = 0; i < len; i++ )
        {
            chars[i] = buf.readChar();
        }
        return new String( chars );
    }

    // SNAPSHOT
    public static void writeSnapshotString(String s, ByteBuf buf) {
        byte[] b = s.getBytes( Charsets.UTF_8 );
        writeVarInt( b.length, buf );
        buf.writeBytes( b );
    }

    public static String readSnapshotString(ByteBuf buf) {
        int len = readVarInt( buf );
        byte[] b = new byte[ len ];
        buf.readBytes( b );

        return new String( b, Charsets.UTF_8 );
    }

    public static void writeArray(byte[] b, ByteBuf buf) {
        buf.writeShort( b.length );
        buf.writeBytes( b );
    }

    public static byte[] readArray(ByteBuf buf) {
        short len = buf.readShort();
        byte[] ret = new byte[ len ];
        buf.readBytes( ret );
        return ret;
    }

    public static void writeStringArray(String[] s, ByteBuf buf) {
        writeVarInt( s.length, buf );
        for ( String str : s )
        {
            writeSnapshotString( str, buf );
        }
    }

    public static String[] readStringArray(ByteBuf buf) {
        int len = readVarInt( buf );
        String[] ret = new String[ len ];
        for ( int i = 0; i < ret.length; i++ )
        {
            ret[i] = readSnapshotString( buf );
        }
        return ret;
    }

    public static int readVarInt(ByteBuf buf) {
        int out = 0;
        int bytes = 0;
        byte in;
        while ( true ) {
            in = buf.readByte();
            out |= ( in & 0x7F ) << ( bytes++ * 7 );

            if ( bytes > 32 ) {
                throw new RuntimeException( "VarInt too big" );
            }

            if ( ( in & 0x80 ) != 0x80 ) {
                break;
            }
        }

        return out;
    }

    public static void writeVarInt(int value, ByteBuf buf) {
        int part;
        while ( true ) {
            part = value & 0x7F;

            value >>>= 7;
            if ( value != 0 ) {
                part |= 0x80;
            }

            buf.writeByte( part );

            if ( value == 0 ) {
                break;
            }
        }
    }
}

package net.md_5.bungee;

import net.md_5.bungee.protocol.PacketWrapper;
import io.netty.buffer.ByteBuf;

/**
 * Class to rewrite integers within packets.
 */
public class EntityMap
{

    public final static int[][] entityIds = new int[ 256 ][];

    static
    {
        entityIds[0x05] = new int[]
        {
            1
        };
        entityIds[0x07] = new int[]
        {
            1, 5
        };
        entityIds[0x11] = new int[]
        {
            1
        };
        entityIds[0x12] = new int[]
        {
            1
        };
        entityIds[0x13] = new int[]
        {
            1
        };
        entityIds[0x14] = new int[]
        {
            1
        };
        entityIds[0x16] = new int[]
        {
            1, 5
        };
        entityIds[0x17] = new int[]
        {
            1 //, 20
        };
        entityIds[0x18] = new int[]
        {
            1
        };
        entityIds[0x19] = new int[]
        {
            1
        };
        entityIds[0x1A] = new int[]
        {
            1
        };
        entityIds[0x1C] = new int[]
        {
            1
        };
        entityIds[0x1E] = new int[]
        {
            1
        };
        entityIds[0x1F] = new int[]
        {
            1
        };
        entityIds[0x20] = new int[]
        {
            1
        };
        entityIds[0x21] = new int[]
        {
            1
        };
        entityIds[0x22] = new int[]
        {
            1
        };
        entityIds[0x23] = new int[]
        {
            1
        };
        entityIds[0x26] = new int[]
        {
            1
        };
        entityIds[0x27] = new int[]
        {
            1, 5
        };
        entityIds[0x28] = new int[]
        {
            1
        };
        entityIds[0x29] = new int[]
        {
            1
        };
        entityIds[0x2A] = new int[]
        {
            1
        };
        entityIds[0x2C] = new int[]
        {
            1
        };
        entityIds[0x37] = new int[]
        {
            1
        };

        entityIds[0x47] = new int[]
        {
            1
        };
    }

    public static void rewrite(PacketWrapper packet, int oldId, int newId)
    {
        if ( packet.isSnapshot() ) return;
        ByteBuf buf = packet.getBufRaw();

        if ( packet.getPacketId() == 0x1D )
        { // bulk entity
            for ( int pos = 2; pos < buf.readableBytes(); pos += 4 )
            {
                int readId = buf.getInt( pos );
                if ( readId == oldId )
                {
                    buf.setInt( pos, newId );
                } else if ( readId == newId )
                {
                    buf.setInt( pos, oldId );
                }
            }
        } else
        {
            int[] idArray = entityIds[packet.getPacketId()];
            if ( idArray != null )
            {
                for ( int pos : idArray )
                {
                    int readId = buf.getInt( pos );
                    if ( readId == oldId )
                    {
                        buf.setInt( pos, newId );
                    } else if ( readId == newId )
                    {
                        buf.setInt( pos, oldId );
                    }
                }
            }
        }
        if ( packet.getPacketId() == 0x17 )
        {
            int type = buf.getUnsignedByte( 5 );
            if ( type == 60 || type == 90 )
            {
                int index20 = buf.getInt( 20 );
                if ( buf.readableBytes() > 24 && index20 == oldId )
                {
                    buf.setInt( 20, newId );
                }
            }
        }
    }
}

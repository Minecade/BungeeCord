package net.md_5.bungee;

import net.md_5.bungee.protocol.PacketUtil;
import net.md_5.bungee.protocol.PacketWrapper;
import io.netty.buffer.ByteBuf;

/**
 * Class to rewrite integers within packets.
 */
public class EntityMapSnapshot
{

    public final static int[][] entityIds = new int[ 256 ][];

    static
    {
        entityIds[0x0A] = new int[]
        {
            0
        };
        entityIds[0x0D] = new int[]
        {
            4
        };
        entityIds[0x12] = new int[]
        {
            0
        };
        entityIds[0x1B] = new int[]
        {
            0, 4
        };
        entityIds[0x1C] = new int[]
        {
            0 // TODO: Meta
        };
        entityIds[0x1D] = new int[]
        {
            0
        };
        entityIds[0x1E] = new int[]
        {
            0
        };
        entityIds[0x20] = new int[]
        {
            0
        };
    }

    public static void rewrite(PacketWrapper packet, int oldId, int newId)
    {
        ByteBuf buf = packet.getBufRaw();

        int[] idArray = entityIds[packet.getPacketId()];

        if ( idArray != null )
        {
            for ( int pos : idArray )
            {
                try {
                    int readId = buf.getInt( pos );
                    if ( readId == oldId )
                    {
                        buf.setInt( pos, newId );
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    System.out.println();
                }
            }
        }
    }
}

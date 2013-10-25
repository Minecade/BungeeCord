package net.md_5.bungee.protocol.version;

import com.google.common.base.Preconditions;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.protocol.*;
import net.md_5.bungee.protocol.packet.snapshot.game.*;
import net.md_5.bungee.protocol.packet.snapshot.handshake.Handshake;
import net.md_5.bungee.protocol.packet.snapshot.login.EncryptionRequest;
import net.md_5.bungee.protocol.packet.snapshot.login.EncryptionResponse;
import net.md_5.bungee.protocol.packet.snapshot.login.LoginRequest;
import net.md_5.bungee.protocol.packet.snapshot.login.LoginSuccess;
import net.md_5.bungee.protocol.packet.snapshot.status.StatusRequest;
import net.md_5.bungee.protocol.packet.snapshot.status.StatusResponse;

public class Snapshot implements MinecraftProtocol
{
    @Getter
    protected static final Snapshot instance = new Snapshot();

    @Override
    public byte getProtocolVersion() {
        return PROTOCOL_VERSION;
    }

    public enum Direction {
        TO_SERVER,
        TO_CLIENT;
    }

    public static final int MAX_PACKET_ID = 0xFF;
    public static final int PROTOCOL_VERSION = 0x00;
    public static final String MINECRAFT_VERSION = "13w41b";

    @RequiredArgsConstructor
    public static class ProtocolDirection
    {
        @Getter
        private final Direction direction;
        private final TObjectIntMap<Class<? extends DefinedPacket>> packetMap = new TObjectIntHashMap<>( MAX_PACKET_ID );
        private final Class<? extends DefinedPacket>[] packetClasses = new Class[ MAX_PACKET_ID ];
        private final Constructor<? extends DefinedPacket>[] packetConstructors = new Constructor[ MAX_PACKET_ID ];

        public boolean hasPacket(int id)
        {
            return id < MAX_PACKET_ID && packetConstructors[id] != null;
        }

        @Override
        public String toString()
        {
            return direction.toString();
        }

        public final DefinedPacket createPacket(int id, ByteBuf buf)
        {
            if ( id > MAX_PACKET_ID )
            {
                throw new BadPacketException( "Packet with id " + id + " outside of range " );
            }
            if ( packetConstructors[id] == null )
            {
                throw new BadPacketException( "No packet with id " + id );
            }

            try
            {
                DefinedPacket newPacket = packetClasses[id].newInstance();
                newPacket.setId(id);
                newPacket.setDirection(direction);
                newPacket.setBuf(buf);
                newPacket.setSnapshot(true);

                return newPacket;
            } catch ( ReflectiveOperationException ex )
            {
                throw new BadPacketException( "Could not construct packet with id " + id, ex );
            }
        }

        protected final void registerPacket(int id, Class<? extends DefinedPacket> packetClass)
        {
            try
            {
                packetConstructors[id] = packetClass.getDeclaredConstructor();
            } catch ( NoSuchMethodException ex )
            {
                throw new BadPacketException( "No NoArgsConstructor for packet class " + packetClass );
            }
            packetClasses[id] = packetClass;
            packetMap.put( packetClass, id );
        }

        protected final void registerPacket(int id, OpCode[] codes)
        {

        }

        protected final void unregisterPacket(int id)
        {
            packetMap.remove( packetClasses[id] );
            packetClasses[id] = null;
            packetConstructors[id] = null;
        }

        public final int getId(Class<? extends DefinedPacket> packet)
        {
            Preconditions.checkArgument( packetMap.containsKey( packet ), "Cannot get ID for packet " + packet );

            return packetMap.get( packet );
        }
    }

    public enum Protocol
    {

        // -1
        HANDSHAKE
        {

            {
                TO_SERVER.registerPacket( 0x00, Handshake.class );
            }
        },
        // 0
        GAME
        {

            {
                TO_CLIENT.registerPacket( 0x00, KeepAlive.class );
                TO_CLIENT.registerPacket( 0x01, Login.class );
                TO_CLIENT.registerPacket( 0x02, Chat.class );
                TO_CLIENT.registerPacket( 0x07, Respawn.class );
                TO_CLIENT.registerPacket( 0x38, PlayerListItem.class );
                TO_CLIENT.registerPacket( 0x3A, TabCompleteResponse.class );
                TO_CLIENT.registerPacket( 0x3B, ScoreboardObjective.class );
                TO_CLIENT.registerPacket( 0x3C, ScoreboardScore.class );
                TO_CLIENT.registerPacket( 0x3D, ScoreboardDisplay.class );
                TO_CLIENT.registerPacket( 0x3E, Team.class );
                TO_CLIENT.registerPacket( 0x3F, PluginMessage.class );
                TO_CLIENT.registerPacket( 0x40, Kick.class );

                TO_SERVER.registerPacket( 0x00, KeepAlive.class );
                TO_SERVER.registerPacket( 0x01, Chat.class );
                TO_SERVER.registerPacket( 0x14, TabCompleteRequest.class );
                TO_SERVER.registerPacket( 0x15, ClientSettings.class );
                TO_SERVER.registerPacket( 0x16, ClientStatus.class );
                TO_SERVER.registerPacket( 0x17, PluginMessage.class );
            }
        },
        // 1
        STATUS
        {

            {
                TO_CLIENT.registerPacket( 0x00, StatusResponse.class );
                TO_CLIENT.registerPacket( 0x01, PingPacket.class );

                TO_SERVER.registerPacket( 0x00, StatusRequest.class );
                TO_SERVER.registerPacket( 0x01, PingPacket.class );
            }
        },
        //2
        LOGIN
        {

            {
                TO_CLIENT.registerPacket( 0x00, Kick.class );
                TO_CLIENT.registerPacket( 0x01, EncryptionRequest.class );
                TO_CLIENT.registerPacket( 0x02, LoginSuccess.class );

                TO_SERVER.registerPacket( 0x00, LoginRequest.class );
                TO_SERVER.registerPacket( 0x01, EncryptionResponse.class );
            }
        };

        public final ProtocolDirection TO_SERVER = new ProtocolDirection( Direction.TO_SERVER );
        public final ProtocolDirection TO_CLIENT = new ProtocolDirection( Direction.TO_CLIENT );

        public ProtocolDirection getProtocolDirection(Direction direction)
        {
            if ( direction == Direction.TO_SERVER )
            {
                return TO_SERVER;
            } else if ( direction == Direction.TO_CLIENT )
            {
                return TO_CLIENT;
            } else
            {
                throw new RuntimeException("Invalid protocol direction " + direction.toString());
            }
        }
    }
}
package net.md_5.bungee.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Map;
import java.util.Set;

import lombok.Data;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Translations {
    private static final Set<Translation> translations = Sets.newHashSet();
    private static final Map<Integer, Translation> vanilla = Maps.newHashMap();
    private static final Map<Integer, Translation> serverBound = Maps.newHashMap();
    private static final Map<Integer, Translation> clientBound = Maps.newHashMap();

    private static void addTranslation(Translation translation)
    {
        Translation existing = vanilla.get(translation.getVanillaPacketId());
        if ( existing != null && existing.getDirection() == translation.getDirection() )
        {
            throw new RuntimeException("Duplicate vanilla packet going the same way " + translation.toString());
        }
        if ( translation.getDirection() == Direction.TO_CLIENT )
        {
            existing = clientBound.get(translation.getSnapshotPacketId());
            if ( existing != null )
            {
                throw new RuntimeException("Duplicate client bound packet " + translation.toString());
            }
            
            clientBound.put(translation.getSnapshotPacketId(), translation);
        } else if ( translation.getDirection() == Direction.TO_SERVER )
        {
            existing = serverBound.get(translation.getSnapshotPacketId());
            if ( existing != null )
            {
                throw new RuntimeException("Duplicate server bound packet " + translation.toString());
            }
            
            serverBound.put(translation.getSnapshotPacketId(), translation);
        }
        
        translations.add(translation);
        vanilla.put(translation.getVanillaPacketId(), translation);
    }
    
    public static Object translate(Object packet) {
        if ( packet instanceof DefinedPacket )
        {
            
        } else if ( packet instanceof PacketWrapper )
        {
            
        }

        throw new BadPacketException("Failed to translate packet");
    }
    
    {
        addTranslation(new Translation(0x00, State.GAME, Direction.TO_CLIENT, 0x00));
        addTranslation(new Translation(0x00, State.GAME, Direction.TO_SERVER, 0x00));
        addTranslation(new Translation(0x01, State.GAME, Direction.TO_CLIENT, 0x01, new Translator()
        {
            void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
            {
                int entity = snapshot.readInt();
                short gamemode = snapshot.readUnsignedByte();
                byte dimension = snapshot.readByte();
                short difficulty = snapshot.readUnsignedByte();
                short maxPlayers = snapshot.readUnsignedByte();
                String level = PacketUtil.readSnapshotString(snapshot);
                
                vanilla.writeInt(entity);
                PacketUtil.writeVanillaString(level, vanilla);
                vanilla.writeByte(gamemode);
                vanilla.writeByte(dimension);
                vanilla.writeByte(difficulty);
                vanilla.writeByte(0);
                vanilla.writeByte(maxPlayers);
            }

            void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
            {
                snapshot.writeInt(vanilla.readInt());

                String level = PacketUtil.readVanillaString(vanilla);
                
                snapshot.writeByte(vanilla.readByte());
                snapshot.writeByte(vanilla.readByte());
                snapshot.writeByte(vanilla.readByte());
                vanilla.readByte();
                snapshot.writeByte(vanilla.readByte());
                PacketUtil.writeSnapshotString(level, snapshot);
            }
        }));
        // 0x02 - ignore?
        addTranslation(new Translation(0x03, State.GAME, Direction.TO_CLIENT, 0x02));
        addTranslation(new Translation(0x03, State.GAME, Direction.TO_SERVER, 0x01));
        addTranslation(new Translation(0x04, State.GAME, Direction.TO_CLIENT, 0x03));
        addTranslation(new Translation(0x05, State.GAME, Direction.TO_CLIENT, 0x04));
        addTranslation(new Translation(0x06, State.GAME, Direction.TO_CLIENT, 0x05));
        addTranslation(new Translation(0x07, State.GAME, Direction.TO_SERVER, 0x02, new Translator()
        {
            void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
            {
                vanilla.writeInt(0);
                vanilla.writeInt(snapshot.readInt());
                vanilla.writeBoolean(snapshot.readBoolean());
            }

            void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
            {
                vanilla.readInt();
                snapshot.writeInt(vanilla.readInt());
                snapshot.writeBoolean(vanilla.readBoolean());
            }
        }));
        addTranslation(new Translation(0x08, State.GAME, Direction.TO_CLIENT, 0x06));
        addTranslation(new Translation(0x09, State.GAME, Direction.TO_CLIENT, 0x07, new Translator()
        {
            void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
            {
                vanilla.writeInt(snapshot.readInt());
                vanilla.writeByte(snapshot.readUnsignedByte());
                vanilla.writeByte(snapshot.readUnsignedByte());
                vanilla.writeShort(256);
                PacketUtil.writeVanillaString(PacketUtil.readSnapshotString(snapshot), vanilla);
            }

            void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
            {
                snapshot.writeInt(vanilla.readInt());
                snapshot.writeByte(vanilla.readByte());
                snapshot.writeByte(vanilla.readByte());
                vanilla.readShort();
                PacketUtil.writeSnapshotString(PacketUtil.readVanillaString(vanilla), snapshot);
            }
        }));
        addTranslation(new Translation(0x0A, State.GAME, Direction.TO_SERVER, 0x03));
        addTranslation(new Translation(0x0B, State.GAME, Direction.TO_SERVER, 0x04));
        addTranslation(new Translation(0x0C, State.GAME, Direction.TO_SERVER, 0x05));
        addTranslation(new Translation(0x0D, State.GAME, Direction.TO_SERVER, 0x06));
        addTranslation(new Translation(0x0D, State.GAME, Direction.TO_CLIENT, 0x08));
        addTranslation(new Translation(0x0E, State.GAME, Direction.TO_SERVER, 0x07));
        addTranslation(new Translation(0x0F, State.GAME, Direction.TO_SERVER, 0x08));
        addTranslation(new Translation(0x10, State.GAME, Direction.TO_SERVER, 0x09));
        addTranslation(new Translation(0x10, State.GAME, Direction.TO_CLIENT, 0x09));
        addTranslation(new Translation(0x11, State.GAME, Direction.TO_CLIENT, 0x0A, new Translator()
        {
            void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla)
            {
                vanilla.writeInt(snapshot.readInt());
                vanilla.writeByte(0);
                vanilla.writeInt(snapshot.readInt());
                vanilla.writeByte(snapshot.readUnsignedByte());
                vanilla.writeInt(snapshot.readInt());
            }

            void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot)
            {
                snapshot.writeInt(vanilla.readInt());
                vanilla.readByte();
                snapshot.writeInt(vanilla.readInt());
                snapshot.writeByte(vanilla.readByte());
                snapshot.writeInt(vanilla.readInt());
            }
        }));
        addTranslation(new Translation(0x12, State.GAME, Direction.TO_SERVER, 0x0A));
        addTranslation(new Translation(0x12, State.GAME, Direction.TO_CLIENT, 0x0B));
        addTranslation(new Translation(0x13, State.GAME, Direction.TO_SERVER, 0x0B));
        // 0x14 - varint, add uuid
        addTranslation(new Translation(0x16, State.GAME, Direction.TO_CLIENT, 0x0D));
        // 0x17 - varint
        // 0x18 - varint
        // 0x19 - varint
        // 0x1A - varint
        addTranslation(new Translation(0x1B, State.GAME, Direction.TO_SERVER, 0x0C));
        addTranslation(new Translation(0x1C, State.GAME, Direction.TO_CLIENT, 0x12)); // varint?
        addTranslation(new Translation(0x1D, State.GAME, Direction.TO_CLIENT, 0x13));
        addTranslation(new Translation(0x1E, State.GAME, Direction.TO_CLIENT, 0x14)); // varint?
        addTranslation(new Translation(0x1F, State.GAME, Direction.TO_CLIENT, 0x15)); // varint?
        addTranslation(new Translation(0x20, State.GAME, Direction.TO_CLIENT, 0x16)); // varint?
        addTranslation(new Translation(0x21, State.GAME, Direction.TO_CLIENT, 0x17)); // varint?
        addTranslation(new Translation(0x22, State.GAME, Direction.TO_CLIENT, 0x18)); // varint?
        addTranslation(new Translation(0x23, State.GAME, Direction.TO_CLIENT, 0x19)); // varint?
        addTranslation(new Translation(0x26, State.GAME, Direction.TO_CLIENT, 0x1A)); // varint?
        addTranslation(new Translation(0x27, State.GAME, Direction.TO_CLIENT, 0x1B));
        addTranslation(new Translation(0x28, State.GAME, Direction.TO_CLIENT, 0x1C));
        addTranslation(new Translation(0x29, State.GAME, Direction.TO_CLIENT, 0x1D));
        addTranslation(new Translation(0x2A, State.GAME, Direction.TO_CLIENT, 0x1E));
        addTranslation(new Translation(0x1F, State.GAME, Direction.TO_CLIENT, 0x2B));
        addTranslation(new Translation(0x2C, State.GAME, Direction.TO_CLIENT, 0x20)); // varint?
        addTranslation(new Translation(0x33, State.GAME, Direction.TO_CLIENT, 0x21));
        // 0x34 to 0x22 - chunk coordinates to varints
        // 0x35 to 0x23 - unsinged byte y & metadata, varint block type
        // 0x36 to 0x24 - unsignged bytes and varint
        // 0x37 to 0x25 - first int to varint
        addTranslation(new Translation(0x38, State.GAME, Direction.TO_CLIENT, 0x26));
        // 0x3C to 0x27 - doubles are now floats
        // 0x3D - ??
        // 0x3E - ??
        addTranslation(new Translation(0x3F, State.GAME, Direction.TO_CLIENT, 0x2A));
        // 0x46 to 0x2B - new data types
        // 0x47 to 0x2C - varint
        // 0x64 to 0x2D - bytes -> unsigned
        addTranslation(new Translation(0x65, State.GAME, Direction.TO_SERVER, 0x0D));
        // 0x65 to 2E - unsigned byte when client bound?
        addTranslation(new Translation(0x66, State.GAME, Direction.TO_SERVER, 0x0E));
        // 0x67 to 0x2F - unsigned byte
        // 0x68 to 0x30 - unsigned byte
        // 0x69 to 0x31 - unsigned byte
        addTranslation(new Translation(0x6A, State.GAME, Direction.TO_SERVER, 0x0F));
        // 0x6A to 0x32 - unsigned byte
        addTranslation(new Translation(0x6B, State.GAME, Direction.TO_SERVER, 0x10));
        addTranslation(new Translation(0x6C, State.GAME, Direction.TO_SERVER, 0x11));
        addTranslation(new Translation(0x82, State.GAME, Direction.TO_CLIENT, 0x33));
        addTranslation(new Translation(0x82, State.GAME, Direction.TO_SERVER, 0x12));
        // 0x83 to 0x34 - removed first short and converted second short to varint
        // 0x84 to 0x35 - unsigned byte
        // 0x85 to 0x35 - remove first argument
        // 0xc8 to 0x37 - array
        addTranslation(new Translation(0xC9, State.GAME, Direction.TO_CLIENT, 0x38));
        addTranslation(new Translation(0xCA, State.GAME, Direction.TO_CLIENT, 0x39));
        addTranslation(new Translation(0xCA, State.GAME, Direction.TO_SERVER, 0x13));
        addTranslation(new Translation(0xCB, State.GAME, Direction.TO_SERVER, 0x14));
        // 0xCB to 0x3A - array of strings and var int
        // 0xCC to 0x15 - mystery boolean
        addTranslation(new Translation(0xCD, State.GAME, Direction.TO_SERVER, 0x16));
        addTranslation(new Translation(0xCE, State.GAME, Direction.TO_CLIENT, 0x3B));
        addTranslation(new Translation(0x3F, State.GAME, Direction.TO_CLIENT, 0x2A));
        addTranslation(new Translation(0xCF, State.GAME, Direction.TO_CLIENT, 0x3C));
        addTranslation(new Translation(0xD0, State.GAME, Direction.TO_CLIENT, 0x3D));
        addTranslation(new Translation(0xD1, State.GAME, Direction.TO_CLIENT, 0x3E));
        addTranslation(new Translation(0x3F, State.GAME, Direction.TO_CLIENT, 0x2A));
        addTranslation(new Translation(0xFA, State.GAME, Direction.TO_CLIENT, 0x3F));
        addTranslation(new Translation(0xFA, State.GAME, Direction.TO_SERVER, 0x17));
        // 0xFF - JSON
    }
    
    enum State {
        HANDSHAKING,
        GAME,
        STATUS,
        LOGIN;
    }
    
    enum Direction {
        TO_CLIENT,
        TO_SERVER;
    }
    
    abstract class Translator {
        abstract void snapshotToVanilla(ByteBuf snapshot, ByteBuf vanilla);
        
        abstract void vanillaToSnapshot(ByteBuf vanilla, ByteBuf snapshot);
    }
    
    @Data
    class Translation {
        private int vanillaPacketId;
        private OpCode[] vanillaOpCodes;
        private State state;
        private Direction direction;
        private int snapshotPacketId;
        private OpCode[] snapshotOpCodes;
        
        private DefinedPacket vanillaPacket;
        private DefinedPacket snapshotPacket;
        
        private Translator translator;
        
        public Translation(int vanillaPacketId, State state, Direction direction, int snapshotPacketId) {
            this.vanillaPacketId = vanillaPacketId;
            this.state = state;
            this.direction = direction;
            this.snapshotPacketId = snapshotPacketId;
        }
        
        public Translation(int vanillaPacketId, State state, Direction direction, int snapshotPacketId, Translator translator) {
            this(vanillaPacketId, state, direction, snapshotPacketId);
            this.translator = translator;
        }
        
        public ByteBuf translateToVanilla(ByteBuf in) {
            ByteBuf out = Unpooled.buffer(in.readableBytes());

            int snapshotPacketId = DefinedPacket.readVarInt(in);
            Preconditions.checkState(snapshotPacketId == this.snapshotPacketId, "Snapshot packet ids to not match (" + snapshotPacketId + " does not match expected " + this.snapshotPacketId + ")");

            if (translator == null) {
                out.writeByte(vanillaPacketId);
                out.writeBytes(in.readBytes(in.readableBytes()));
            } else {
                translator.snapshotToVanilla(in, out);
            }
                
            return out;
        }
        
        public ByteBuf translateToSnapshot(ByteBuf in) {
            ByteBuf out = Unpooled.buffer(in.readableBytes());
            
            int vanillaPacketId = in.readByte();
            Preconditions.checkState(vanillaPacketId == this.vanillaPacketId, "Vanilla packet ids to not match (" + vanillaPacketId + " does not match expected " + this.vanillaPacketId + ")");
            
            if (translator == null) {
                DefinedPacket.writeVarInt(this.snapshotPacketId, out);
                out.writeBytes(in.readBytes(in.readableBytes()));
            } else {
                translator.vanillaToSnapshot(in, out);
            }
            
            return out;
        }
    }
}

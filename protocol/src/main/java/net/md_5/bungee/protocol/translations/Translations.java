package net.md_5.bungee.protocol.translations;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Map;
import java.util.Set;

import net.md_5.bungee.protocol.*;
import net.md_5.bungee.protocol.translations.translators.*;

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
        addTranslation(new Translation(0x01, State.GAME, Direction.TO_CLIENT, 0x01, new LoginTranslator()));
        // 0x02 - ignore?
        addTranslation(new Translation(0x03, State.GAME, Direction.TO_CLIENT, 0x02, new ChatTranslator()));
        addTranslation(new Translation(0x03, State.GAME, Direction.TO_SERVER, 0x01));
        addTranslation(new Translation(0x04, State.GAME, Direction.TO_CLIENT, 0x03));
        addTranslation(new Translation(0x05, State.GAME, Direction.TO_CLIENT, 0x04));
        addTranslation(new Translation(0x06, State.GAME, Direction.TO_CLIENT, 0x05));
        addTranslation(new Translation(0x07, State.GAME, Direction.TO_SERVER, 0x02, new UseEntityTranslator()));
        addTranslation(new Translation(0x08, State.GAME, Direction.TO_CLIENT, 0x06));
        addTranslation(new Translation(0x09, State.GAME, Direction.TO_CLIENT, 0x07, new RespawnTranslator()));
        addTranslation(new Translation(0x0A, State.GAME, Direction.TO_SERVER, 0x03));
        addTranslation(new Translation(0x0B, State.GAME, Direction.TO_SERVER, 0x04));
        addTranslation(new Translation(0x0C, State.GAME, Direction.TO_SERVER, 0x05));
        addTranslation(new Translation(0x0D, State.GAME, Direction.TO_SERVER, 0x06));
        addTranslation(new Translation(0x0D, State.GAME, Direction.TO_CLIENT, 0x08));
        addTranslation(new Translation(0x0E, State.GAME, Direction.TO_SERVER, 0x07));
        addTranslation(new Translation(0x0F, State.GAME, Direction.TO_SERVER, 0x08));
        addTranslation(new Translation(0x10, State.GAME, Direction.TO_SERVER, 0x09));
        addTranslation(new Translation(0x10, State.GAME, Direction.TO_CLIENT, 0x09));
        addTranslation(new Translation(0x11, State.GAME, Direction.TO_CLIENT, 0x0A, new UseBedTranslator()));
        addTranslation(new Translation(0x12, State.GAME, Direction.TO_SERVER, 0x0A));
        addTranslation(new Translation(0x12, State.GAME, Direction.TO_CLIENT, 0x0B));
        addTranslation(new Translation(0x13, State.GAME, Direction.TO_SERVER, 0x0B));
        addTranslation(new Translation(0x14, State.GAME, Direction.TO_CLIENT, 0x0C, new SpawnPlayerTranslator()));
        addTranslation(new Translation(0x16, State.GAME, Direction.TO_CLIENT, 0x0D));
        addTranslation(new Translation(0x17, State.GAME, Direction.TO_CLIENT, 0x0E, new FirstVarIntTranslator()));
        addTranslation(new Translation(0x18, State.GAME, Direction.TO_CLIENT, 0x0F, new FirstVarIntTranslator()));
        addTranslation(new Translation(0x19, State.GAME, Direction.TO_CLIENT, 0x10, new PaintingTranslator()));
        addTranslation(new Translation(0x1A, State.GAME, Direction.TO_CLIENT, 0x11, new FirstVarIntTranslator()));
        addTranslation(new Translation(0x1B, State.GAME, Direction.TO_SERVER, 0x0C));
        addTranslation(new Translation(0x1C, State.GAME, Direction.TO_CLIENT, 0x12));
        addTranslation(new Translation(0x1D, State.GAME, Direction.TO_CLIENT, 0x13));
        addTranslation(new Translation(0x1E, State.GAME, Direction.TO_CLIENT, 0x14));
        addTranslation(new Translation(0x1F, State.GAME, Direction.TO_CLIENT, 0x15));
        addTranslation(new Translation(0x20, State.GAME, Direction.TO_CLIENT, 0x16));
        addTranslation(new Translation(0x21, State.GAME, Direction.TO_CLIENT, 0x17));
        addTranslation(new Translation(0x22, State.GAME, Direction.TO_CLIENT, 0x18));
        addTranslation(new Translation(0x23, State.GAME, Direction.TO_CLIENT, 0x19));
        addTranslation(new Translation(0x26, State.GAME, Direction.TO_CLIENT, 0x1A));
        addTranslation(new Translation(0x27, State.GAME, Direction.TO_CLIENT, 0x1B));
        addTranslation(new Translation(0x28, State.GAME, Direction.TO_CLIENT, 0x1C));
        addTranslation(new Translation(0x29, State.GAME, Direction.TO_CLIENT, 0x1D));
        addTranslation(new Translation(0x2A, State.GAME, Direction.TO_CLIENT, 0x1E));
        addTranslation(new Translation(0x2B, State.GAME, Direction.TO_CLIENT, 0x1F));
        addTranslation(new Translation(0x2C, State.GAME, Direction.TO_CLIENT, 0x20, new EntityPropertiesTranslator()));
        addTranslation(new Translation(0x33, State.GAME, Direction.TO_CLIENT, 0x21));
        addTranslation(new Translation(0x34, State.GAME, Direction.TO_CLIENT, 0x22, new MultiBlockChangeTranslator()));
        addTranslation(new Translation(0x35, State.GAME, Direction.TO_CLIENT, 0x23, new BlockChangeTranslator()));
        addTranslation(new Translation(0x36, State.GAME, Direction.TO_CLIENT, 0x24, new BlockActionTranslator()));
        addTranslation(new Translation(0x37, State.GAME, Direction.TO_CLIENT, 0x25, new FirstVarIntTranslator()));
        addTranslation(new Translation(0x38, State.GAME, Direction.TO_CLIENT, 0x26));
        addTranslation(new Translation(0x3C, State.GAME, Direction.TO_CLIENT, 0x27, new ExplosionTranslator()));
        addTranslation(new Translation(0x3D, State.GAME, Direction.TO_CLIENT, 0x28));
        addTranslation(new Translation(0x3E, State.GAME, Direction.TO_CLIENT, 0x29, new SoundEffectTranslator()));
        addTranslation(new Translation(0x3F, State.GAME, Direction.TO_CLIENT, 0x2A));
        addTranslation(new Translation(0x46, State.GAME, Direction.TO_CLIENT, 0x2B, new ChangeGameStateTranslator()));
        addTranslation(new Translation(0x47, State.GAME, Direction.TO_CLIENT, 0x2C, new FirstVarIntTranslator()));
        addTranslation(new Translation(0x64, State.GAME, Direction.TO_CLIENT, 0x2D, new OpenWindowTranslator()));
        addTranslation(new Translation(0x65, State.GAME, Direction.TO_CLIENT, 0x2E, new CloseWindowTranslator()));
        addTranslation(new Translation(0x65, State.GAME, Direction.TO_SERVER, 0x0D, new CloseWindowTranslator()));
        addTranslation(new Translation(0x66, State.GAME, Direction.TO_SERVER, 0x0E));
        addTranslation(new Translation(0x67, State.GAME, Direction.TO_CLIENT, 0x2F, new FirstUnsignedByteTranslator()));
        addTranslation(new Translation(0x68, State.GAME, Direction.TO_CLIENT, 0x30, new FirstUnsignedByteTranslator()));
        addTranslation(new Translation(0x69, State.GAME, Direction.TO_CLIENT, 0x31, new FirstUnsignedByteTranslator()));
        addTranslation(new Translation(0x6A, State.GAME, Direction.TO_CLIENT, 0x32, new FirstUnsignedByteTranslator()));
        addTranslation(new Translation(0x6A, State.GAME, Direction.TO_SERVER, 0x0F, new FirstUnsignedByteTranslator()));        
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

package net.md_5.bungee.protocol.translations;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.md_5.bungee.protocol.*;
import net.md_5.bungee.protocol.translations.translators.*;
import net.md_5.bungee.protocol.version.Snapshot;
import net.md_5.bungee.protocol.version.Vanilla;
import net.md_5.bungee.protocol.version.Snapshot.Direction;
import net.md_5.bungee.protocol.version.Snapshot.ProtocolDirection;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Translations {
    private static boolean init = false;
    private static final Set<Translation> translations = Sets.newHashSet();
    private static final Map<Short, List<Translation>> vanilla = Maps.newHashMap();
    private static final Map<Integer, Translation> serverBound = Maps.newHashMap();
    private static final Map<Integer, Translation> clientBound = Maps.newHashMap();

    private static void addTranslation(Translation translation)
    {
        List<Translation> existingVanilla = vanilla.get(translation.getVanillaPacketId());

        if ( existingVanilla == null)
        {
            existingVanilla = Lists.newArrayList();
            vanilla.put(translation.getVanillaPacketId(), existingVanilla);
        } else if ( existingVanilla.size() > 1 || (existingVanilla.size() == 1 && existingVanilla.get(0).getDirection() == translation.getDirection()) )
        {
            throw new RuntimeException("Duplicate vanilla packet going the same way " + translation.toString());
        }

        Translation existingSnapshot;
        if ( translation.getDirection() == Direction.TO_CLIENT )
        {
            existingSnapshot = clientBound.get(translation.getSnapshotPacketId());
            if ( existingSnapshot != null )
            {
                throw new RuntimeException("Duplicate client bound packet " + translation.toString());
            }

            clientBound.put(translation.getSnapshotPacketId(), translation);
        } else if ( translation.getDirection() == Direction.TO_SERVER )
        {
            existingSnapshot = serverBound.get(translation.getSnapshotPacketId());
            if ( existingSnapshot != null )
            {
                throw new RuntimeException("Duplicate server bound packet " + translation.toString());
            }

            serverBound.put(translation.getSnapshotPacketId(), translation);
        }

        translations.add(translation);
        existingVanilla.add(translation);
    }

    public static Object translate(Object packet)
    {
        return translate(packet, null);
    }

    public static Object translate(Object packet, Direction direction)
    {
        setupTranslations();

        Translation translation;
        ByteBuf translated;

        if ( packet instanceof DefinedPacket )
        {
            DefinedPacket definedPacket = (DefinedPacket) packet;
            DefinedPacket translatedPacket = null;
            ByteBuf translatedData;
            int newPacketId;

            if ( definedPacket.isSnapshot() )
            {
                System.out.println("Translating a snapshot packet to vanilla (" + definedPacket.getDirection() + ")");

                // create a vanilla packet from a snapshot packet
                if ( definedPacket.getDirection() == Direction.TO_CLIENT)
                {
                    translation = clientBound.get(definedPacket.getId());
                } else if ( definedPacket.getDirection() == Direction.TO_SERVER )
                {
                    translation = serverBound.get(definedPacket.getId());
                } else
                {
                    throw new RuntimeException("Invalid direction " + definedPacket.getDirection());
                }

                // make a buf of the packet's data if the packet was instantiated via api
                if ( definedPacket.getBuf() == null )
                {
                    ByteBuf newBuf = Unpooled.buffer();
                    PacketUtil.writeVarInt(translation.getSnapshotPacketId(), newBuf);
                    definedPacket.write(newBuf);
                    definedPacket.setBuf(newBuf);
                }

                // get the full translated packet bytebuf and just the data bytebuf
                translated = translation.translateToVanilla(definedPacket.getBufCopy());
                translatedData = translated.copy();
                translatedData.readUnsignedByte();

                // form a vanilla packet
                newPacketId = translation.getVanillaPacketId();
                translatedPacket = Vanilla.getInstance().read(translation.getVanillaPacketId(), translatedData);
            } else
            {
                System.out.println("Translating a vanilla packet to snapshot (" + direction + ")");
                // create a snapshot packet from a vanilla packet
                Snapshot.Protocol protocol = Snapshot.Protocol.GAME; // FIX ME?
                ProtocolDirection protocolDirection = protocol.getProtocolDirection(direction);
                translation = vanilla.get((short) (int) definedPacket.getId()).get(0);
                if ( translation.getDirection() != direction )
                {
                    translation = vanilla.get((short) (int) definedPacket.getId()).get(1);
                }

                // make a buf of the packet's data if the packet was instantiated via api
                if ( definedPacket.getBuf() == null )
                {
                    ByteBuf newBuf = Unpooled.buffer();
                    newBuf.writeByte(translation.getVanillaPacketId());
                    definedPacket.write(newBuf);
                    definedPacket.setBuf(newBuf);
                }

                // get the full translated packet bytebuf and just the data bytebuf
                translated = translation.translateToSnapshot(definedPacket.getBuf());
                translatedData = translated.copy();
                PacketUtil.readVarInt(translatedData);

                // form a snapshot packet (we have to read in this one but not in the vanilla one)
                newPacketId = translation.getSnapshotPacketId();
                try {
                    translatedPacket = protocolDirection.createPacket(translation.getSnapshotPacketId(), translatedData);
                    translatedPacket.read( translatedData );
                } catch( BadPacketException e) {
                    System.out.println("WARNING: No packet for this type");
                }
            }

            System.out.println("SUCESS TRANSLATION: " + translation.getTranslator() + " VANILLA: " + translation.getVanillaPacketId() + " SNAPSHOT: " + translation.getSnapshotPacketId() + " PACKET: " + (translatedPacket != null ? translatedPacket.toString() : "") + " TRANSLATOR: " + translation.getTranslator());

            // if there isn't a defined packet for the translation, return a packet wrapper
            if ( translatedPacket == null )
            {
                return new PacketWrapper(newPacketId, null, translated, !definedPacket.isSnapshot() );
            } else
            {
                return translatedPacket;
            }
        } else if ( packet instanceof PacketWrapper )
        {
            PacketWrapper definedWrapper = (PacketWrapper) packet;
            PacketWrapper translatedWrapper;
            DefinedPacket translatedPacket = null;

            if ( definedWrapper.getPacket() != null )
            {
                Object result = translate( definedWrapper.getPacket(), direction );
                if ( result instanceof DefinedPacket )
                {
                    translatedPacket = (DefinedPacket) result;
                }
            }

            if ( definedWrapper.isSnapshot() )
            {
                if ( definedWrapper.getDirection() == Direction.TO_CLIENT )
                {
                    translation = clientBound.get(definedWrapper.getPacketId());
                } else if ( definedWrapper.getDirection() == Direction.TO_SERVER )
                {
                    translation = serverBound.get(definedWrapper.getPacketId());
                } else
                {
                    throw new RuntimeException("Invalid direction " + definedWrapper.getDirection());
                }

                translated = translation.translateToVanilla(definedWrapper.getBufCopy());
                translatedWrapper = new PacketWrapper(translation.getVanillaPacketId(), translatedPacket, translated, false);
            } else
            {
                Preconditions.checkState(direction != null, "direction can not be null when translating a vanilla packet");
                translation = vanilla.get((short) (int) definedWrapper.getPacketId()).get(0);
                if ( translation.getDirection() != direction )
                {
                    translation = vanilla.get((short) (int) definedWrapper.getPacketId()).get(1);
                }

                translated = translation.translateToSnapshot(definedWrapper.getBufCopy());
                translatedWrapper = new PacketWrapper(translation.getSnapshotPacketId(), translatedPacket, translated, true);
                translatedWrapper.setDirection(direction);
            }

            System.out.println("SUCESS PACKET WRAPPER: " + translation.getTranslator() + " VANILLA: " + translation.getVanillaPacketId() + " SNAPSHOT: " + translation.getSnapshotPacketId() + " PACKET: " + (translatedPacket != null ? translatedPacket.toString() : "") + " TRANSLATOR: " + translation.getTranslator());
            return translatedWrapper;
        }

        throw new BadPacketException("Failed to translate packet " + packet.getClass() + " " + packet.toString());
    }

    private static void setupTranslations()
    {
        if(init) return;
        init = true;

        addTranslation(new Translation(0x00, Direction.TO_CLIENT, 0x00));
        addTranslation(new Translation(0x00, Direction.TO_SERVER, 0x00));

        addTranslation(new Translation(0x01, Direction.TO_CLIENT, 0x01, new LoginTranslator()));

        addTranslation(new Translation(0x03, Direction.TO_CLIENT, 0x02, new FirstStringTranslator()));
        addTranslation(new Translation(0x03, Direction.TO_SERVER, 0x01, new FirstStringTranslator()));

        addTranslation(new Translation(0x04, Direction.TO_CLIENT, 0x03));

        addTranslation(new Translation(0x05, Direction.TO_CLIENT, 0x04));

        addTranslation(new Translation(0x06, Direction.TO_CLIENT, 0x05));

        addTranslation(new Translation(0x07, Direction.TO_SERVER, 0x02, new UseEntityTranslator()));

        addTranslation(new Translation(0x08, Direction.TO_CLIENT, 0x06));

        addTranslation(new Translation(0x09, Direction.TO_CLIENT, 0x07, new RespawnTranslator()));

        addTranslation(new Translation(0x0A, Direction.TO_SERVER, 0x03));

        addTranslation(new Translation(0x0B, Direction.TO_SERVER, 0x04));

        addTranslation(new Translation(0x0C, Direction.TO_SERVER, 0x05));

        addTranslation(new Translation(0x0D, Direction.TO_CLIENT, 0x08, new PlayerPositionLookTranslator()));
        addTranslation(new Translation(0x0D, Direction.TO_SERVER, 0x06));

        addTranslation(new Translation(0x0E, Direction.TO_SERVER, 0x07));

        addTranslation(new Translation(0x0F, Direction.TO_SERVER, 0x08));

        addTranslation(new Translation(0x10, Direction.TO_CLIENT, 0x09, new HeldItemTranslator()));
        addTranslation(new Translation(0x10, Direction.TO_SERVER, 0x09));

        addTranslation(new Translation(0x11, Direction.TO_CLIENT, 0x0A, new UseBedTranslator()));

        addTranslation(new Translation(0x12, Direction.TO_CLIENT, 0x0B, new AnimationTranslator()));
        addTranslation(new Translation(0x12, Direction.TO_SERVER, 0x0A, new AnimationTranslator2()));

        addTranslation(new Translation(0x13, Direction.TO_SERVER, 0x0B));

        addTranslation(new Translation(0x14, Direction.TO_CLIENT, 0x0C, new SpawnPlayerTranslator()));

        addTranslation(new Translation(0x16, Direction.TO_CLIENT, 0x0D));

        addTranslation(new Translation(0x17, Direction.TO_CLIENT, 0x0E, new FirstVarIntTranslator()));

        addTranslation(new Translation(0x18, Direction.TO_CLIENT, 0x0F, new SpawnMobTranslator()));

        addTranslation(new Translation(0x19, Direction.TO_CLIENT, 0x10, new PaintingTranslator()));

        addTranslation(new Translation(0x1A, Direction.TO_CLIENT, 0x11, new FirstVarIntTranslator()));

        addTranslation(new Translation(0x1B, Direction.TO_SERVER, 0x0C));

        addTranslation(new Translation(0x1C, Direction.TO_CLIENT, 0x12));

        addTranslation(new Translation(0x1D, Direction.TO_CLIENT, 0x13));

        addTranslation(new Translation(0x1E, Direction.TO_CLIENT, 0x14));

        addTranslation(new Translation(0x1F, Direction.TO_CLIENT, 0x15));

        addTranslation(new Translation(0x20, Direction.TO_CLIENT, 0x16));

        addTranslation(new Translation(0x21, Direction.TO_CLIENT, 0x17));

        addTranslation(new Translation(0x22, Direction.TO_CLIENT, 0x18));

        addTranslation(new Translation(0x23, Direction.TO_CLIENT, 0x19));

        addTranslation(new Translation(0x26, Direction.TO_CLIENT, 0x1A));

        addTranslation(new Translation(0x27, Direction.TO_CLIENT, 0x1B));

        addTranslation(new Translation(0x28, Direction.TO_CLIENT, 0x1C, new EntityMetadataTranslator()));

        addTranslation(new Translation(0x29, Direction.TO_CLIENT, 0x1D));

        addTranslation(new Translation(0x2A, Direction.TO_CLIENT, 0x1E));

        addTranslation(new Translation(0x2B, Direction.TO_CLIENT, 0x1F));

        addTranslation(new Translation(0x2C, Direction.TO_CLIENT, 0x20, new EntityPropertiesTranslator()));

        addTranslation(new Translation(0x33, Direction.TO_CLIENT, 0x21));

        addTranslation(new Translation(0x34, Direction.TO_CLIENT, 0x22, new MultiBlockChangeTranslator()));

        addTranslation(new Translation(0x35, Direction.TO_CLIENT, 0x23, new BlockChangeTranslator()));

        addTranslation(new Translation(0x36, Direction.TO_CLIENT, 0x24, new BlockActionTranslator()));

        addTranslation(new Translation(0x37, Direction.TO_CLIENT, 0x25, new FirstVarIntTranslator()));

        addTranslation(new Translation(0x38, Direction.TO_CLIENT, 0x26));

        addTranslation(new Translation(0x3C, Direction.TO_CLIENT, 0x27, new ExplosionTranslator()));

        addTranslation(new Translation(0x3D, Direction.TO_CLIENT, 0x28));

        addTranslation(new Translation(0x3E, Direction.TO_CLIENT, 0x29, new SoundEffectTranslator()));

        addTranslation(new Translation(0x3F, Direction.TO_CLIENT, 0x2A, new FirstStringTranslator()));

        addTranslation(new Translation(0x46, Direction.TO_CLIENT, 0x2B, new ChangeGameStateTranslator()));

        addTranslation(new Translation(0x47, Direction.TO_CLIENT, 0x2C, new FirstVarIntTranslator()));

        addTranslation(new Translation(0x64, Direction.TO_CLIENT, 0x2D, new OpenWindowTranslator()));

        addTranslation(new Translation(0x65, Direction.TO_CLIENT, 0x2E, new CloseWindowTranslator()));
        addTranslation(new Translation(0x65, Direction.TO_SERVER, 0x0D, new CloseWindowTranslator()));

        addTranslation(new Translation(0x66, Direction.TO_SERVER, 0x0E));

        addTranslation(new Translation(0x67, Direction.TO_CLIENT, 0x2F, new FirstUnsignedByteTranslator()));

        addTranslation(new Translation(0x68, Direction.TO_CLIENT, 0x30, new FirstUnsignedByteTranslator()));

        addTranslation(new Translation(0x69, Direction.TO_CLIENT, 0x31, new FirstUnsignedByteTranslator()));

        addTranslation(new Translation(0x6A, Direction.TO_CLIENT, 0x32, new FirstUnsignedByteTranslator()));
        addTranslation(new Translation(0x6A, Direction.TO_SERVER, 0x0F, new FirstUnsignedByteTranslator()));

        addTranslation(new Translation(0x6B, Direction.TO_SERVER, 0x10));

        addTranslation(new Translation(0x6C, Direction.TO_SERVER, 0x11));

        addTranslation(new Translation(0x82, Direction.TO_CLIENT, 0x33, new UpdateSignTranslator()));
        addTranslation(new Translation(0x82, Direction.TO_SERVER, 0x12, new UpdateSignTranslator()));

        addTranslation(new Translation(0x83, Direction.TO_CLIENT, 0x34, new MapsTranslator()));

        addTranslation(new Translation(0x84, Direction.TO_CLIENT, 0x35, new UpdateTileEntityTranslator()));

        addTranslation(new Translation(0x85, Direction.TO_CLIENT, 0x36, new SignEditorOpen()));

        addTranslation(new Translation(0xC8, Direction.TO_CLIENT, 0x37, new StatisticsTranslator()));

        addTranslation(new Translation(0xC9, Direction.TO_CLIENT, 0x38, new FirstStringTranslator()));

        addTranslation(new Translation(0xCA, Direction.TO_CLIENT, 0x39));
        addTranslation(new Translation(0xCA, Direction.TO_SERVER, 0x13));

        addTranslation(new Translation(0xCB, Direction.TO_CLIENT, 0x3A, new TabCompleteResponseTranslator()));
        addTranslation(new Translation(0xCB, Direction.TO_SERVER, 0x14, new FirstStringTranslator()));

        addTranslation(new Translation(0xCC, Direction.TO_SERVER, 0x15, new ClientSettingsTranslator()));

        addTranslation(new Translation(0xCD, Direction.TO_SERVER, 0x16, new ClientStatusTranslator()));

        addTranslation(new Translation(0xCE, Direction.TO_CLIENT, 0x3B, new ScoreboardObjectiveTranslator()));

        addTranslation(new Translation(0xCF, Direction.TO_CLIENT, 0x3C, new ScoreboardScoreTranslator()));

        addTranslation(new Translation(0xD0, Direction.TO_CLIENT, 0x3D, new ScoreboardDisplayTranslator()));

        addTranslation(new Translation(0xD1, Direction.TO_CLIENT, 0x3E, new ScoreboardTeamsTranslator()));

        addTranslation(new Translation(0xFA, Direction.TO_CLIENT, 0x3F, new FirstStringTranslator()));
        addTranslation(new Translation(0xFA, Direction.TO_SERVER, 0x17, new FirstStringTranslator()));

        addTranslation(new Translation(0xFF, Direction.TO_CLIENT, 0x40, new KickTranslator())); // TODO - JSON?
    }
}

package net.md_5.bungee.protocol.translations;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.OpCode;
import net.md_5.bungee.protocol.version.Snapshot.Direction;

import com.google.common.base.Preconditions;

@Data
public class Translation {
    private short vanillaPacketId;
    private OpCode[] vanillaOpCodes;
    private Direction direction;
    private int snapshotPacketId;
    private OpCode[] snapshotOpCodes;

    private DefinedPacket vanillaPacket;
    private DefinedPacket snapshotPacket;

    private Translator translator;

    public Translation(int vanillaPacketId, Direction direction, int snapshotPacketId) {
        this.vanillaPacketId = (short) vanillaPacketId;
        this.direction = direction;
        this.snapshotPacketId = snapshotPacketId;
    }

    public Translation(int vanillaPacketId, Direction direction, int snapshotPacketId, Translator translator) {
        this(vanillaPacketId, direction, snapshotPacketId);
        this.translator = translator;
    }

    public ByteBuf translateToVanilla(ByteBuf in) {
        ByteBuf out = Unpooled.buffer(in.readableBytes());

        int snapshotPacketId = DefinedPacket.readVarInt(in);
        Preconditions.checkState(snapshotPacketId == this.snapshotPacketId, "Snapshot packet ids to not match (" + snapshotPacketId + " does not match expected " + this.snapshotPacketId + ")");

        out.writeByte(vanillaPacketId);
        if (translator == null) {
            out.writeBytes(in.readBytes(in.readableBytes()));
        } else {
            translator.snapshotToVanilla(in, out);
        }

        return out;
    }

    public ByteBuf translateToSnapshot(ByteBuf in) {
        ByteBuf out = Unpooled.buffer(in.readableBytes());

        int vanillaPacketId = in.readUnsignedByte();
        Preconditions.checkState(vanillaPacketId == this.vanillaPacketId, "Vanilla packet ids to not match (" + vanillaPacketId + " does not match expected " + this.vanillaPacketId + ")");

        DefinedPacket.writeVarInt(this.snapshotPacketId, out);
        if (translator == null) {
            out.writeBytes(in.readBytes(in.readableBytes()));
        } else {
            translator.vanillaToSnapshot(in, out);
        }

        return out;
    }
}

package net.md_5.bungee.protocol.version.snapshot;

import static net.md_5.bungee.protocol.OpCode.BYTE;
import static net.md_5.bungee.protocol.OpCode.FLOAT;
import static net.md_5.bungee.protocol.OpCode.INT;
import static net.md_5.bungee.protocol.OpCode.STRING;
import net.md_5.bungee.protocol.OpCode;
import net.md_5.bungee.protocol.packet.PacketC8IncremetStatistic;
import net.md_5.bungee.protocol.skip.PacketReader;
import net.md_5.bungee.protocol.version.Vanilla;
import lombok.Getter;

public class Vanilla13w36a extends Vanilla
{
    @Getter
    protected static final Vanilla13w36a instance = new Vanilla13w36a((byte) 75, "13w36a");
    
    public Vanilla13w36a(byte protocol, String version)
    {
        super(protocol, version);
        classes[0xC8] = PacketC8IncremetStatistic.class;
        skipper = new PacketReader( this ); // FIXME
    }
    
    {
        opCodes[0xC8] = null;
        opCodes[0x3E] = new OpCode[]
        {
            STRING, INT, INT, INT, FLOAT, BYTE, BYTE
        };
    }
}

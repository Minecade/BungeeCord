package net.md_5.bungee.protocol.version;

import static net.md_5.bungee.protocol.OpCode.BYTE;
import static net.md_5.bungee.protocol.OpCode.FLOAT;
import static net.md_5.bungee.protocol.OpCode.INT;
import static net.md_5.bungee.protocol.OpCode.STRING;
import net.md_5.bungee.protocol.OpCode;
import net.md_5.bungee.protocol.packet.PacketC8IncremetStatistic;
import lombok.Getter;

public class Vanilla13w36a extends Vanilla
{
    public static final byte PROTOCOL_VERSION = 75;
    public static final String GAME_VERSION = "13w36a";
    @Getter
    private static final Vanilla instance = new Vanilla13w36a();
    
    public Vanilla13w36a()
    {
        super();
        classes[0xC8] = PacketC8IncremetStatistic.class;
    }
    
    {
        opCodes[0x3E] = new OpCode[]
        {
            STRING, INT, INT, INT, FLOAT, BYTE, BYTE
        };
    }
}

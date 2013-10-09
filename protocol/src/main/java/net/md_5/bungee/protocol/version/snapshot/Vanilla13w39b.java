package net.md_5.bungee.protocol.version.snapshot;

import static net.md_5.bungee.protocol.OpCode.*;
import net.md_5.bungee.protocol.OpCode;
import net.md_5.bungee.protocol.skip.PacketReader;
import lombok.Getter;

public class Vanilla13w39b extends Vanilla13w36a
{
    @Getter
    protected static final Vanilla13w39b instance = new Vanilla13w39b((byte) 80, "13w39b");

    public Vanilla13w39b( final byte protocol, final String version ) {
        super(protocol, version);
        skipper = new PacketReader( this ); // FIXME
    }
    
    {
        opCodes[0x46] = new OpCode[]
        {
            BYTE, FLOAT
        };
        opCodes[0x14] = new OpCode[]
        {
            INT, STRING, STRING, INT, INT, INT, BYTE, BYTE, SHORT, METADATA
        };
    }
}

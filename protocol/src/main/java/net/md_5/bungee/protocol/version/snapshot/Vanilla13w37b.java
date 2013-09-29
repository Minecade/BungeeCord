package net.md_5.bungee.protocol.version.snapshot;

import lombok.Getter;

public class Vanilla13w37b extends Vanilla13w36a
{
    @Getter
    protected static final Vanilla13w37b instance = new Vanilla13w37b((byte) 76, "13w37b");

    public Vanilla13w37b( final byte protocol, final String version ) {
        super(protocol, version);
    }
}

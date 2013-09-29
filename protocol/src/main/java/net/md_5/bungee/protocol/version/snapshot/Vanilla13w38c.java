package net.md_5.bungee.protocol.version.snapshot;

import lombok.Getter;

public class Vanilla13w38c extends Vanilla13w36a
{
    @Getter
    protected static final Vanilla13w38c instance = new Vanilla13w38c((byte) 79, "13w38c");

    public Vanilla13w38c( final byte protocol, final String version ) {
        super(protocol, version);
    }
}

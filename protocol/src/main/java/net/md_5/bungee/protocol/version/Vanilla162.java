package net.md_5.bungee.protocol.version;

import lombok.Getter;

public class Vanilla162 extends Vanilla
{
    @Getter
    protected static final Vanilla162 instance = new Vanilla162((byte) 74, "1.6.2");

    public Vanilla162( final byte protocol, final String version ) {
        super(protocol, version);
    }
}

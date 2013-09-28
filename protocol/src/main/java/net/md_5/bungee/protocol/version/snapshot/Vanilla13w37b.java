package net.md_5.bungee.protocol.version.snapshot;

import net.md_5.bungee.protocol.version.Vanilla;
import lombok.Getter;

public class Vanilla13w37b extends Vanilla13w36a
{
    public static final byte PROTOCOL_VERSION = 76;
    public static final String GAME_VERSION = "13w37b";
    @Getter
    private static final Vanilla instance = new Vanilla13w37b();
}

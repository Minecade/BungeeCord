package net.md_5.bungee.protocol.versions;

import lombok.Getter;

public class Vanilla163 extends Vanilla
{
    public static final byte PROTOCOL_VERSION = 77;
    public static final String GAME_VERSION = "1.6.3";
    @Getter
    private static final Vanilla instance = new Vanilla163();
}

package net.md_5.bungee.protocol.versions;

import lombok.Getter;

public class Vanilla162 extends Vanilla
{
    public static final byte PROTOCOL_VERSION = 74;
    public static final String GAME_VERSION = "1.6.2";
    @Getter
    private static final Vanilla instance = new Vanilla162();
}

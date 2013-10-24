package net.md_5.bungee.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the standard list data returned by opening a server in the
 * Minecraft client server list, or hitting it with a packet 0xFE.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerPing
{
    // backwards compat
    public ServerPing(byte protocolVersion, String gameVersion, String motd, int currentPlayers, int maxPlayers)
    {
        this.version = new Protocol(gameVersion, protocolVersion);
        this.players = new Players(maxPlayers, currentPlayers);
        this.description = motd;
    }

    private Protocol version;
    
    @Data
    @AllArgsConstructor
    public static class Protocol
    {
        private String name;
        private int protocol;
    }
    
    private Players players;
    
    @Data
    @AllArgsConstructor
    public static class Players
    {
        private int max;
        private int online;
    }
    
    private String description;
    private String favicon;
}

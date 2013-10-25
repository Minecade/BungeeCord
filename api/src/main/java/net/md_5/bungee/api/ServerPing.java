package net.md_5.bungee.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Getter;

/**
 * Represents the standard list data returned by opening a server in the
 * Minecraft client server list, or hitting it with a packet 0xFE.
 */
@Data
@RequiredArgsConstructor
public class ServerPing
{
    private final Protocol version;
    
    @Data
    @AllArgsConstructor
    public static class Protocol
    {
        private String name;
        private int protocol;
    }
    
    private final Players players;
    
    @Data
    @AllArgsConstructor
    public static class Players
    {
        private int max;
        private int online;
    }
    
    private final String description;
    private final String favicon;

    @Getter
    public boolean snapshot;

    // backwards compat
    public ServerPing(byte protocolVersion, String gameVersion, String motd, int currentPlayers, int maxPlayers)
    {
        this.version = new Protocol(gameVersion, protocolVersion);
        this.players = new Players(maxPlayers, currentPlayers);
        this.description = motd;
        this.favicon = "";
    }

    public byte getProtocolVersion()
    {
        return (byte) version.protocol;
    }
    
    public String getGameVersion()
    {
        return version.name;
    }
    
    public String getMotd()
    {
        return description;
    }
    
    public int getCurrentPlayers()
    {
        return players.online;
    }
    
    public int getMaxPlayers()
    {
        return players.max;
    }
}

package net.md_5.bungee.api.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.api.plugin.Event;

/**
 * Event called to represent a player first making their presence and username
 * known.
 */
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class PlayerHandshakeEvent extends Event
{

    /**
     * Connection attempting to login.
     */
    private final PendingConnection connection;
    /**
     * The handshake.
     */
    private final DefinedPacket handshake;

    public PlayerHandshakeEvent(PendingConnection connection, DefinedPacket handshake)
    {
        this.connection = connection;
        this.handshake = handshake;
    }
}

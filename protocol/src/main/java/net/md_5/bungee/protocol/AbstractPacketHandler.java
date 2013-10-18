package net.md_5.bungee.protocol;

import net.md_5.bungee.protocol.packet.*;
import net.md_5.bungee.protocol.packet.snapshot.*;
import net.md_5.bungee.protocol.packet.snapshot.game.*;
import net.md_5.bungee.protocol.packet.snapshot.handshake.Handshake;
import net.md_5.bungee.protocol.packet.snapshot.login.EncryptionRequest;
import net.md_5.bungee.protocol.packet.snapshot.login.EncryptionResponse;
import net.md_5.bungee.protocol.packet.snapshot.login.LoginRequest;
import net.md_5.bungee.protocol.packet.snapshot.login.LoginSuccess;
import net.md_5.bungee.protocol.packet.snapshot.status.StatusRequest;
import net.md_5.bungee.protocol.packet.snapshot.status.StatusResponse;

public abstract class AbstractPacketHandler
{

    public void handle(Packet0KeepAlive alive) throws Exception
    {
    }

    public void handle(Packet1Login login) throws Exception
    {
    }

    public void handle(Packet2Handshake handshake) throws Exception
    {
    }

    public void handle(Packet3Chat chat) throws Exception
    {
    }

    public void handle(Packet9Respawn respawn) throws Exception
    {
    }

    public void handle(Packet2CEntityProperties properties) throws Exception
    {
    }

    public void handle(PacketC9PlayerListItem playerList) throws Exception
    {
    }

    public void handle(PacketCCSettings settings) throws Exception
    {
    }

    public void handle(PacketCDClientStatus clientStatus) throws Exception
    {
    }

    public void handle(PacketCEScoreboardObjective objective) throws Exception
    {
    }

    public void handle(PacketCFScoreboardScore score) throws Exception
    {
    }

    public void handle(PacketD0DisplayScoreboard displayScoreboard) throws Exception
    {
    }

    public void handle(PacketD1Team team) throws Exception
    {
    }

    public void handle(PacketFAPluginMessage pluginMessage) throws Exception
    {
    }

    public void handle(PacketFCEncryptionResponse encryptResponse) throws Exception
    {
    }

    public void handle(PacketFDEncryptionRequest encryptRequest) throws Exception
    {
    }

    public void handle(PacketFEPing ping) throws Exception
    {
    }

    public void handle(PacketFFKick kick) throws Exception
    {
    }

    public void handle(PacketCBTabComplete tabComplete) throws Exception
    {
    }
    
   
    // HANDSHAKE
    public void handle(Handshake handshake) throws Exception
    {
    }

    // PLAY
    public void handle(Login login) throws Exception
    {
    }

    public void handle(KeepAlive keepAlive) throws Exception
    {
    }

    public void handle(Kick kick) throws Exception
    {
    }

    public void handle(Chat chat) throws Exception
    {
    }

    public void handle(Respawn respawn) throws Exception
    {
    }

    public void handle(PlayerListItem playerListItem) throws Exception
    {
    }

    public void handle(TabCompleteRequest tabComplete) throws Exception
    {
    }

    public void handle(TabCompleteResponse tabCompleteResponse) throws Exception
    {    
    }

    public void handle(ClientSettings clientSettings) throws Exception
    {
    }

    public void handle(PluginMessage pluginMessage) throws Exception
    {
    }

    public void handle(Team team) throws Exception
    {
    }

    public void handle(ScoreboardDisplay scoreboardDisplay) throws Exception
    {
    }

    public void handle(ScoreboardScore scoreboardScore) throws Exception
    {
    }

    public void handle(ScoreboardObjective scoreboardObjective) throws Exception
    {
    }

    public void handle(ClientStatus clientStatus) throws Exception
    {
    }

    public void handle(PingPacket pingPacket) throws Exception
    {
    }
    
    // LOGIN
    public void handle(LoginRequest loginRequest) throws Exception
    {
    }

    public void handle(EncryptionResponse loginSuccess) throws Exception
    {
    }

    public void handle(LoginSuccess loginSuccess) throws Exception
    {
    }

    public void handle(EncryptionRequest encryptionRequest) throws Exception
    {        
    }

    // STATUS
    public void handle(StatusRequest statusRequest) throws Exception
    {        
    }

    public void handle(StatusResponse statusResponse) throws Exception
    {
    }
}

package net.md_5.bungee.protocol.packet;

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
    
    // 13w36a and up
    public void handle(PacketC8IncremetStatistic incrementStatistic) throws Exception
    {
    }

    // 13w41a and up
    
    // HANDSHAKE - SERVERBOUND (ONLY)
    public void handle(net.md_5.bungee.protocol.packet.serverbound.handshake.Packet0Handshake packet0Handshake)
    {        
    }

    // PLAY - SERVERBOUND
    public void handle(net.md_5.bungee.protocol.packet.serverbound.play.Packet0KeepAlive packet0KeepAlive)
    {
    }

    public void handle(net.md_5.bungee.protocol.packet.serverbound.play.Packet1Chat packet1Chat)
    {
    }

    public void handle(net.md_5.bungee.protocol.packet.serverbound.play.Packet14TabComplete packet14TabComplete)
    {
    }

    public void handle(net.md_5.bungee.protocol.packet.serverbound.play.Packet15Settings packet15Settings)
    {
    }

    public void handle(net.md_5.bungee.protocol.packet.serverbound.play.Packet16ClientStatus packet16ClientStatus)
    {        
    }

    public void handle(net.md_5.bungee.protocol.packet.serverbound.play.Packet17PluginMessage packet17PluginMessage)
    {        
    }

    // PLAY - CLIENTBOUND
    public void handle(net.md_5.bungee.protocol.packet.clientbound.play.Packet0KeepAlive packet0KeepAlive)
    {
    }

    public void handle(net.md_5.bungee.protocol.packet.clientbound.play.Packet1Login packet1Login)
    {
    }

    public void handle(net.md_5.bungee.protocol.packet.clientbound.play.Packet3Chat packet3Chat)
    {
    }

    public void handle(net.md_5.bungee.protocol.packet.clientbound.play.Packet7Respawn packet7Respawn)
    {    
    }
    
    public void handle(net.md_5.bungee.protocol.packet.clientbound.play.Packet23EntityProperties packet23EntityProperties)
    {        
    }

    public void handle(net.md_5.bungee.protocol.packet.clientbound.play.Packet3AIncremetStatistic packet3aIncremetStatistic)
    {
    }

    public void handle(net.md_5.bungee.protocol.packet.clientbound.play.Packet3BPlayerListItem packet3bPlayerListItem)
    {
    }

    public void handle(net.md_5.bungee.protocol.packet.clientbound.play.Packet3BTabComplete packet3bTabComplete)
    {
    }

    public void handle(net.md_5.bungee.protocol.packet.clientbound.play.Packet3EScoreboardObjective packet3eScoreboardObjective)
    {
    }

    public void handle(net.md_5.bungee.protocol.packet.clientbound.play.Packet3FScoreboardScore packet3fScoreboardScore)
    {
    }

    public void handle(net.md_5.bungee.protocol.packet.clientbound.play.Packet40DisplayScoreboard packet40DisplayScoreboard)
    {
    }

    public void handle(net.md_5.bungee.protocol.packet.clientbound.play.Packet41Team packet41Team)
    {
    }

    public void handle(net.md_5.bungee.protocol.packet.clientbound.play.Packet42PluginMessage packet42PluginMessage)
    {
    }
    
    // STATUS - SERVERBOUND - TODO
    
    // STATUS - CLIENTBOUND - TODO
    
    // LOGIN - SERVERBOUND - TODO
    
    // LOGIN - CLIENTBOUND - TODO
}

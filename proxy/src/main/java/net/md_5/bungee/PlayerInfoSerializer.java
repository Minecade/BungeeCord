package net.md_5.bungee;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.md_5.bungee.api.ServerPing;

import java.lang.reflect.Type;
import java.util.UUID;

public class PlayerInfoSerializer implements JsonSerializer<ServerPing.PlayerInfo>, JsonDeserializer<ServerPing.PlayerInfo>
{

    private final int protocol;

    public PlayerInfoSerializer(int protocol)
    {
        this.protocol = protocol;
    }

    @Override
    public ServerPing.PlayerInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject js = json.getAsJsonObject();
        ServerPing.PlayerInfo info = new ServerPing.PlayerInfo( js.get( "name" ).getAsString(), (UUID) null );
        if ( protocol == 4 )
        {
            info.setId( js.get( "id" ).getAsString() );
        } else
        {
			// This throws IllegalArguementException because we're spoofing UUID's to support 1.7.2 - 1.7.8!
            //info.setUniqueId( UUID.fromString( js.get( "id" ).getAsString() ) );
			
			// Instead, we will just set the ID to raw string since we don't care about breaking skins.
			info.setId( js.get( "id" ).getAsString() );
        }
        return null;
    }

    @Override
    public JsonElement serialize(ServerPing.PlayerInfo src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject out = new JsonObject();
        out.addProperty( "name", src.getName() );
        if ( protocol == 4 )
        {
            out.addProperty( "id", src.getId() );
        } else
        {
			// getUniqueId() will likely return null.
            //out.addProperty( "id", src.getUniqueId().toString() );
			
			out.addProperty( "id", src.getId() );
        }
        return out;
    }
}

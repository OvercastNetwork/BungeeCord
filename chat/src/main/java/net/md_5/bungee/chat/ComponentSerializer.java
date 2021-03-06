package net.md_5.bungee.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ScoreComponent;
import net.md_5.bungee.api.chat.SelectorComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;

import java.lang.reflect.Type;

public class ComponentSerializer implements JsonDeserializer<BaseComponent>
{

    private final static Gson gson = new GsonBuilder().
            registerTypeHierarchyAdapter( BaseComponent.class, new ComponentSerializer() ).
            registerTypeHierarchyAdapter( TextComponent.class, new TextComponentSerializer() ).
            registerTypeHierarchyAdapter( TranslatableComponent.class, new TranslatableComponentSerializer() ).
            registerTypeHierarchyAdapter( SelectorComponent.class, new SelectorComponentSerializer() ).
            registerTypeHierarchyAdapter( ScoreComponent.class, new ScoreComponentSerializer() ).
            create();

    public static BaseComponent[] parse(String json)
    {
        if ( json.startsWith( "[" ) )
        { //Array
            return gson.fromJson( json, BaseComponent[].class );
        }
        return new BaseComponent[]
        {
            gson.fromJson( json, BaseComponent.class )
        };
    }

    public static String toString(BaseComponent component)
    {
        return gson.toJson( component );
    }

    public static String toString(BaseComponent... components)
    {
        return gson.toJson( new TextComponent( components ) );
    }

    @Override
    public BaseComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        if ( json.isJsonPrimitive() )
        {
            return new TextComponent( json.getAsString() );
        }
        JsonObject object = json.getAsJsonObject();
        if ( object.has( "translate" ) )
        {
            return context.deserialize( json, TranslatableComponent.class );
        }
        if ( object.has( "selector" ) )
        {
            return context.deserialize( json, SelectorComponent.class );
        }
        if ( object.has( "score" ) )
        {
            return context.deserialize( json, ScoreComponent.class );
        }
        return context.deserialize( json, TextComponent.class );
    }
}

package net.md_5.bungee.chat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;

import java.lang.reflect.Type;
import java.util.Arrays;

public class TranslatableComponentSerializer extends BaseComponentSerializer implements JsonSerializer<TranslatableComponent>, JsonDeserializer<TranslatableComponent>
{

    @Override
    public TranslatableComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject object = json.getAsJsonObject();
        TranslatableComponent component = new TranslatableComponent(object.get( "translate" ).getAsString());
        deserialize( object, component, context );
        if ( object.has( "with" ) )
        {
            component.setWith( Arrays.asList( (BaseComponent[]) context.deserialize( object.get( "with" ), BaseComponent[].class ) ) );
        }
        return component;
    }

    @Override
    public JsonElement serialize(TranslatableComponent src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject object = new JsonObject();
        serialize( object, src, context );
        object.addProperty( "translate", src.getTranslate() );
        if ( src.getWith() != null )
        {
            object.add( "with", context.serialize( src.getWith() ) );
        }
        return object;
    }
}

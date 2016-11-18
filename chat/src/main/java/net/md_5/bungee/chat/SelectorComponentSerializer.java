package net.md_5.bungee.chat;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.md_5.bungee.api.chat.SelectorComponent;

public class SelectorComponentSerializer extends BaseComponentSerializer implements JsonSerializer<SelectorComponent>, JsonDeserializer<SelectorComponent> {

    @Override
    public SelectorComponent deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject json = jsonElement.getAsJsonObject();
        final SelectorComponent component = new SelectorComponent(json.get("selector").getAsString());
        deserialize(json, component, context);
        return component;
    }

    @Override
    public JsonElement serialize(SelectorComponent src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject json = new JsonObject();
        serialize(json, src, context);
        json.addProperty("selector", src.getSelector());
        return json;
    }
}

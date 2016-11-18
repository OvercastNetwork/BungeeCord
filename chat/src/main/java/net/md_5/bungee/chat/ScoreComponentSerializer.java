package net.md_5.bungee.chat;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.md_5.bungee.api.chat.ScoreComponent;

public class ScoreComponentSerializer extends BaseComponentSerializer implements JsonSerializer<ScoreComponent>, JsonDeserializer<ScoreComponent> {

    @Override
    public ScoreComponent deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject json = jsonElement.getAsJsonObject();
        final JsonObject score = json.getAsJsonObject("score");
        final ScoreComponent component = new ScoreComponent(score.get("name").getAsString(),
                                                            score.get("objective").getAsString());
        deserialize(json, component, context);
        return component;
    }

    @Override
    public JsonElement serialize(ScoreComponent src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject score = new JsonObject();
        score.addProperty("name", src.getName());
        score.addProperty("objective", src.getObjective());

        final JsonObject json = new JsonObject();
        serialize(json, src, context);
        json.add("score", score);

        return json;
    }
}

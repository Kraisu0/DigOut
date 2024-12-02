package com.kraisu.digout.loaders;

import com.google.gson.*;
import com.kraisu.digout.rooms.Coordinate;

import java.lang.reflect.Type;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

public class CoordinateAdapter implements JsonSerializer<Coordinate>, JsonDeserializer<Coordinate> {
    // Serializacja - obiekt Coordinate do JSON
    @Override
    public JsonElement serialize(Coordinate src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("x", src.getX());
        obj.addProperty("y", src.getY());
        return obj;
    }

    // Deserializacja - JSON do obiektu Coordinate
    @Override
    public Coordinate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            int x = jsonObject.get("x").getAsInt();
            int y = jsonObject.get("y").getAsInt();
            return new Coordinate(x, y);
        } else {
            throw new JsonParseException("Expected a JSON object for Coordinate");
        }
    }
}

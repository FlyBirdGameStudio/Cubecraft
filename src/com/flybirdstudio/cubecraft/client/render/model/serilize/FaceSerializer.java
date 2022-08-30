package com.flybirdstudio.cubecraft.client.render.model.serilize;

import com.google.gson.*;
import com.flybirdstudio.cubecraft.client.render.model.object.Face;

import java.lang.reflect.Type;

public class FaceSerializer implements JsonDeserializer<Face>{
    @Override
    public Face deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    return null;
    }
}

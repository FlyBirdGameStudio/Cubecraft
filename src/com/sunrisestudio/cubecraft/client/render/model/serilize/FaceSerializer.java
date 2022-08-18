package com.sunrisestudio.cubecraft.client.render.model.serilize;

import com.google.gson.*;
import com.sunrisestudio.cubecraft.client.render.model.FaceCullingMethod;
import com.sunrisestudio.cubecraft.client.render.model.object.Face;
import org.joml.Vector2d;

import java.lang.reflect.Type;

public class FaceSerializer implements JsonDeserializer<Face>{
    @Override
    public Face deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    return null;
    }
}

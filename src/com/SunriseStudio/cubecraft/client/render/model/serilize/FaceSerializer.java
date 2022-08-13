package com.sunrisestudio.cubecraft.client.render.model.serilize;

import com.google.gson.*;
import com.sunrisestudio.cubecraft.client.render.model.FaceCullingMethod;
import com.sunrisestudio.cubecraft.client.render.model.object.Face;
import org.joml.Vector2d;

import java.lang.reflect.Type;

public class FaceSerializer implements JsonDeserializer<Face>{
    @Override
    public Face deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject face=jsonElement.getAsJsonObject();
        return new Face(
                new Vector2d(
                    face.get("uv_start").getAsJsonArray().get(0).getAsDouble(),
                    face.get("uv_start").getAsJsonArray().get(1).getAsDouble()
                ),
                new Vector2d(
                    face.get("uv_end").getAsJsonArray().get(0).getAsDouble(),
                    face.get("uv_end").getAsJsonArray().get(1).getAsDouble()
                ),
                face.get("texture").getAsString(),
                FaceCullingMethod.from(face.get("culling").getAsString())
        );
    }
}

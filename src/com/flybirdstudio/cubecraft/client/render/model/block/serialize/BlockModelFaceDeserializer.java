package com.flybirdstudio.cubecraft.client.render.model.block.serialize;

import com.flybirdstudio.cubecraft.client.render.model.block.BlockModelFace;
import com.google.gson.*;

import java.lang.reflect.Type;

public class BlockModelFaceDeserializer implements JsonDeserializer<BlockModelFace> {
    @Override
    public BlockModelFace deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject face=jsonElement.getAsJsonObject();
        return new BlockModelFace(
                face.get("texture").getAsString(),
                face.get("uv").getAsJsonArray().get(0).getAsFloat(),
                face.get("uv").getAsJsonArray().get(2).getAsFloat(),
                face.get("uv").getAsJsonArray().get(3).getAsFloat(),
                face.get("uv").getAsJsonArray().get(4).getAsFloat()
        );
    }
}

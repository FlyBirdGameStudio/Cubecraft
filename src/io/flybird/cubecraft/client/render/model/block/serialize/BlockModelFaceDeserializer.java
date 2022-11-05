package io.flybird.cubecraft.client.render.model.block.serialize;

import io.flybird.cubecraft.client.render.model.block.BlockModelFace;
import io.flybird.cubecraft.client.render.model.block.CullingMethod;
import com.google.gson.*;

import java.lang.reflect.Type;

public class BlockModelFaceDeserializer implements JsonDeserializer<BlockModelFace> {
    @Override
    public BlockModelFace deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject face=jsonElement.getAsJsonObject();
        return new BlockModelFace(
                face.get("texture").getAsString(),
                face.get("uv").getAsJsonArray().get(0).getAsFloat(),
                face.get("uv").getAsJsonArray().get(1).getAsFloat(),
                face.get("uv").getAsJsonArray().get(2).getAsFloat(),
                face.get("uv").getAsJsonArray().get(3).getAsFloat(),
                face.has("color")?face.get("color").getAsString():"cubecraft:default",
                CullingMethod.from(face.has("culling")?face.get("culling").getAsString():"solid")
        );
    }
}

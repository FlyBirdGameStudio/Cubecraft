package io.flybird.cubecraft.client.render.model.block;

import com.google.gson.*;
import io.flybird.cubecraft.client.resources.ResourceLocation;

import java.lang.reflect.Type;

public record BlockModelFace(String tex, float u0, float u1, float v0, float v1, String color, CullingMethod culling) {
    public String getTexture(){
        return ResourceLocation.blockTexture(tex).format();
    }

    public static class JDeserializer implements JsonDeserializer<BlockModelFace> {
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
}

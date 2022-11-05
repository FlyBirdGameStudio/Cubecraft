package io.flybird.cubecraft.client.render.model.block.serialize;

import io.flybird.cubecraft.client.render.model.RenderType;
import io.flybird.cubecraft.client.render.model.block.BlockModelComponentCube;
import io.flybird.cubecraft.client.render.model.block.BlockModelFace;
import io.flybird.cubecraft.client.render.model.block.IBlockModelComponent;
import com.google.gson.*;
import org.joml.Vector3d;

import java.lang.reflect.Type;
import java.util.Objects;

public class BlockModelCompDeserializer implements JsonDeserializer<IBlockModelComponent> {
    @Override
    public IBlockModelComponent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject comp=jsonElement.getAsJsonObject();

        RenderType renderType=RenderType.from(comp.get("render_type").getAsString());
        String modelType=comp.get("type").getAsString();

        Vector3d start=new Vector3d(
                comp.get("start").getAsJsonArray().get(0).getAsDouble(),
                comp.get("start").getAsJsonArray().get(1).getAsDouble(),
                comp.get("start").getAsJsonArray().get(2).getAsDouble()
        );

        Vector3d end=new Vector3d(
                comp.get("end").getAsJsonArray().get(0).getAsDouble(),
                comp.get("end").getAsJsonArray().get(1).getAsDouble(),
                comp.get("end").getAsJsonArray().get(2).getAsDouble()
        );

        if(Objects.equals(modelType, "cube")){
            return new BlockModelComponentCube(
                    renderType,start,end,
                    jsonDeserializationContext.deserialize(comp.get("faces").getAsJsonObject().get("top"), BlockModelFace.class),
                    jsonDeserializationContext.deserialize(comp.get("faces").getAsJsonObject().get("bottom"), BlockModelFace.class),
                    jsonDeserializationContext.deserialize(comp.get("faces").getAsJsonObject().get("left"), BlockModelFace.class),
                    jsonDeserializationContext.deserialize(comp.get("faces").getAsJsonObject().get("right"), BlockModelFace.class),
                    jsonDeserializationContext.deserialize(comp.get("faces").getAsJsonObject().get("front"), BlockModelFace.class),
                    jsonDeserializationContext.deserialize(comp.get("faces").getAsJsonObject().get("back"), BlockModelFace.class)
            );
        }
        return null;
    }
}

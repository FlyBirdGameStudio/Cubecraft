package io.flybird.cubecraft.client.render.model.block;

import io.flybird.cubecraft.client.render.model.RenderType;
import com.google.gson.*;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.quantum3d.draw.VertexArrayBuilder;
import org.joml.Vector3d;

import java.lang.reflect.Type;
import java.util.Objects;

public abstract class BlockModelComponent {
    protected final RenderType type;
    protected final Vector3d start,end;

    protected BlockModelComponent(RenderType type, Vector3d start, Vector3d end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public abstract void render(VertexArrayBuilder builder, RenderType currentType, IWorld world, BlockState bs, long x, long y, long z, double renderX, double rendery, double renderz);

    public abstract void renderAsItem(VertexArrayBuilder builder, double renderX, double renderY, double renderZ);

    public static class JDeserializer implements JsonDeserializer<BlockModelComponent> {
        @Override
        public BlockModelComponent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
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
}

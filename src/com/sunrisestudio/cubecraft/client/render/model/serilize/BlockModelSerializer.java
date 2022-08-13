package com.sunrisestudio.cubecraft.client.render.model.serilize;

import com.sunrisestudio.cubecraft.client.render.model.object.Cube;
import com.sunrisestudio.cubecraft.client.render.model.object.BlockModel;
import com.google.gson.*;
import com.sunrisestudio.cubecraft.client.render.model.RenderType;
import com.sunrisestudio.cubecraft.client.render.model.object.Face;
import org.joml.Vector3d;

import java.lang.reflect.Type;

public class BlockModelSerializer implements JsonDeserializer<BlockModel>{
    @Override
    public BlockModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj=jsonElement.getAsJsonObject();
        JsonObject meta=obj.get("meta").getAsJsonObject();
        JsonArray model=obj.get("model").getAsJsonArray();
        String id=meta.get("id").getAsString();
        String namespace=meta.get("namespace").getAsString();
        RenderType renderType=RenderType.from(meta.get("render_type").getAsString());
        Cube[] models=new Cube[model.size()];
        for (int i = 0; i < model.size(); i++) {
            JsonObject m=model.get(i).getAsJsonObject();
            models[i]=new Cube(
                new Vector3d(
                    m.get("start").getAsJsonArray().get(0).getAsDouble(),
                    m.get("start").getAsJsonArray().get(1).getAsDouble(),
                    m.get("start").getAsJsonArray().get(2).getAsDouble()
                ),
                new Vector3d(
                    m.get("end").getAsJsonArray().get(0).getAsDouble(),
                    m.get("end").getAsJsonArray().get(1).getAsDouble(),
                    m.get("end").getAsJsonArray().get(2).getAsDouble()
                ),
                jsonDeserializationContext.deserialize(m.get("top"), Face.class),
                jsonDeserializationContext.deserialize(m.get("bottom"), Face.class),
                jsonDeserializationContext.deserialize(m.get("left"), Face.class),
                jsonDeserializationContext.deserialize(m.get("right"), Face.class),
                jsonDeserializationContext.deserialize(m.get("front"), Face.class),
                jsonDeserializationContext.deserialize(m.get("back"), Face.class)
            );
        }
        return new BlockModel(id,namespace,renderType,models);
    }
}

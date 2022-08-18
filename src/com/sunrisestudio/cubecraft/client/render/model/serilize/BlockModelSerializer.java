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
        return null;
    }
}

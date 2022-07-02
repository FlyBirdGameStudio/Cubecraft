package com.SunriseStudio.cubecraft.resources.model.serilize;

import com.SunriseStudio.cubecraft.render.character.Cube;
import com.SunriseStudio.cubecraft.resources.model.BlockModel;
import com.google.gson.*;

import java.lang.reflect.Type;

public class BlockModelSerilizer implements JsonSerializer<BlockModel>,JsonDeserializer<BlockModel>{
    @Override
    public BlockModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        BlockModel model;
        JsonArray objects=jsonElement.getAsJsonObject().get("objects").getAsJsonArray();
        for (JsonElement jsonObject:objects){
            JsonObject cube=jsonObject.getAsJsonObject();
            model=new BlockModel(1,new Cube[0]);
        }
        return null;
    }

    @Override
    public JsonElement serialize(BlockModel model, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }
}

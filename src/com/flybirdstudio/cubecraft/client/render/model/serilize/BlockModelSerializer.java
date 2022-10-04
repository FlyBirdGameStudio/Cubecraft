package com.flybirdstudio.cubecraft.client.render.model.serilize;

import com.flybirdstudio.cubecraft.client.render.model.object.BlockModel;
import com.google.gson.*;
import com.flybirdstudio.cubecraft.client.render.model.object.ModelObject;

import java.lang.reflect.Type;

public class BlockModelSerializer implements JsonDeserializer<BlockModel>{
    @Override
    public BlockModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject root=jsonElement.getAsJsonObject();

        JsonArray used_textures=root.get("used_textures").getAsJsonArray();
        for (int i=0;i< used_textures.size();i++){

        }


        JsonArray models=root.get("used_textures").getAsJsonArray();
        ModelObject[] obj =new ModelObject[models.size()];
        for (int i=0;i< models.size();i++){
            obj[i]=jsonDeserializationContext.deserialize(models.get(i),ModelObject.class);
        }


        return new BlockModel(obj);
    }
}

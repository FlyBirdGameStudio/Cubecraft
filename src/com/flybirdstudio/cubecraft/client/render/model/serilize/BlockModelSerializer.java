package com.flybirdstudio.cubecraft.client.render.model.serilize;

import com.flybirdstudio.cubecraft.client.render.model.object.BlockModel;
import com.google.gson.*;
import com.flybirdstudio.cubecraft.client.render.model.object.ModelObject;
import com.flybirdstudio.cubecraft.registery.Registery;
import com.flybirdstudio.starfish3d.render.textures.Texture2DArray;

import java.lang.reflect.Type;

public class BlockModelSerializer implements JsonDeserializer<BlockModel>{
    @Override
    public BlockModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject root=jsonElement.getAsJsonObject();

        JsonArray used_textures=root.get("used_textures").getAsJsonArray();
        for (int i=0;i< used_textures.size();i++){
            Texture2DArray tex= Registery.getTextureManager().get2DArrayTexture("cubecraft:terrain");
            if(!tex.contains(used_textures.get(i).getAsString())){
                tex.load(used_textures.get(i).getAsString());
            }
        }


        JsonArray models=root.get("used_textures").getAsJsonArray();
        ModelObject[] obj =new ModelObject[models.size()];
        for (int i=0;i< models.size();i++){
            obj[i]=jsonDeserializationContext.deserialize(models.get(i),ModelObject.class);
        }


        return new BlockModel(obj);
    }
}

package io.flybird.cubecraft.client.render.model.block.serialize;

import io.flybird.cubecraft.client.render.model.block.BlockModel;
import io.flybird.cubecraft.client.render.model.block.IBlockModelComponent;
import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.resources.ResourceLocation;
import io.flybird.cubecraft.resources.ResourceManager;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BlockModelDeserializer implements JsonDeserializer<BlockModel> {
    @Override
    public BlockModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if(jsonElement.getAsJsonObject().has("cover_json")){
            JsonObject root=jsonElement.getAsJsonObject().get("cover_json").getAsJsonObject();
            String src= ResourceManager.instance.getResource(ResourceLocation.blockModel(root.get("import").getAsString())).getAsText();
            JsonArray arr=root.get("replacement").getAsJsonArray();
            for (JsonElement n:arr){
                src=src.replace(
                        n.getAsJsonObject().get("from").getAsString(),
                        n.getAsJsonObject().get("to").getAsString()
                );
            }
            BlockModel m= Registry.getJsonReader().fromJson(src,BlockModel.class);
            return m;
        }




        JsonArray comp=jsonElement.getAsJsonObject().get("components").getAsJsonArray();
        ArrayList<IBlockModelComponent> component=new ArrayList<>();
        for (int i = 0; i < comp.size(); i++) {
            component.add(jsonDeserializationContext.deserialize(comp.get(i),IBlockModelComponent.class));
        }

        ArrayList<String>usedTextures=new ArrayList<>();
        JsonArray texture=jsonElement.getAsJsonObject().get("used_textures").getAsJsonArray();
        for (int i = 0; i < texture.size(); i++) {
            usedTextures.add(texture.get(i).getAsString());
        }
        return new BlockModel(component, usedTextures);
    }
}

package io.flybird.cubecraft.client.render.model.block;

import io.flybird.cubecraft.client.render.model.RenderType;
import com.google.gson.*;
import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.client.render.model.object.Model;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.quantum3d.draw.VertexArrayBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class BlockModel implements Model {
    private final ArrayList<BlockModelComponent> components;
    private final ArrayList<String> usedTextures;

    public BlockModel(ArrayList<BlockModelComponent> components, ArrayList<String> usedTextures) {
        this.components = components;
        this.usedTextures = usedTextures;
    }

    public void render(VertexArrayBuilder builder, RenderType currentType, IWorld world, BlockState bs, long x, long y, long z, double renderX, double rendery, double renderz){
        for (BlockModelComponent model:components){
            if(currentType==model.type) {
                model.render(builder, currentType, world, bs, x, y, z, renderX, rendery, renderz);
            }
        }
    }

    public void renderAsItem(VertexArrayBuilder builder, double renderX, double renderY, double renderZ){
        for (BlockModelComponent model:components){
            model.renderAsItem(builder, renderX, renderY, renderZ);
        }
    }

    @Override
    public void initializeModel(List<ResourceLocation> textureList) {
        for (String s:this.usedTextures){
            ResourceLocation loc=ResourceLocation.blockTexture(s);
            if(!textureList.contains(loc)) {
                textureList.add(loc);
            }
        }
    }


    public static class JDeserializer implements JsonDeserializer<BlockModel> {
        @Override
        public BlockModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (jsonElement.getAsJsonObject().has("cover_json")) {
                JsonObject root = jsonElement.getAsJsonObject().get("cover_json").getAsJsonObject();
                String src = ClientRegistries.RESOURCE_MANAGER.getResource(ResourceLocation.blockModel(root.get("import").getAsString())).getAsText();
                JsonArray arr = root.get("replacement").getAsJsonArray();
                for (JsonElement n : arr) {
                    src = src.replace(n.getAsJsonObject().get("from").getAsString(), n.getAsJsonObject().get("to").getAsString());
                }
                return Registries.createJsonReader().fromJson(src, BlockModel.class);
            }

            JsonArray comp = jsonElement.getAsJsonObject().get("components").getAsJsonArray();
            ArrayList<BlockModelComponent> component = new ArrayList<>();
            for (int i = 0; i < comp.size(); i++) {
                component.add(jsonDeserializationContext.deserialize(comp.get(i), BlockModelComponent.class));
            }

            ArrayList<String> usedTextures = new ArrayList<>();
            JsonArray texture = jsonElement.getAsJsonObject().get("used_textures").getAsJsonArray();
            for (int i = 0; i < texture.size(); i++) {
                usedTextures.add(texture.get(i).getAsString());
            }
            return new BlockModel(component, usedTextures);
        }
    }
}

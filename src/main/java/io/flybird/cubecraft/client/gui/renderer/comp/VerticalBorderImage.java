package io.flybird.cubecraft.client.gui.renderer.comp;

import com.google.gson.*;
import io.flybird.cubecraft.client.gui.Node;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.register.RenderRegistry;
import io.flybird.starfish3d.render.textures.Texture2D;

import java.lang.reflect.Type;
import java.util.List;

public record VerticalBorderImage(double x0,double x1,double y0,double y1,int boarder,String loc) implements ComponentPartRenderer {
    @Override
    public void render(Node node) {
        Texture2D tex= RenderRegistry.getTextureManager().getTexture2DContainer().get(ResourceLocation.uiTexture(this.loc.split(":")[0],this.loc.split(":")[1]).format());

        //todo:add render
    }

    @Override
    public void initializeRenderer(List<ResourceLocation> loc) {
        loc.add(ResourceLocation.uiTexture(this.loc.split(":")[0],this.loc.split(":")[1]));
    }

    public static class JDeserializer implements JsonDeserializer<VerticalBorderImage> {
        @Override
        public VerticalBorderImage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject root=jsonElement.getAsJsonObject();
            return new VerticalBorderImage(
                    root.get("pos").getAsJsonArray().get(0).getAsDouble(),
                    root.get("pos").getAsJsonArray().get(1).getAsDouble(),
                    root.get("pos").getAsJsonArray().get(2).getAsDouble(),
                    root.get("pos").getAsJsonArray().get(3).getAsDouble(),
                    root.get("boarder").getAsInt(),
                    root.get("loc").getAsString());
        }
    }
}

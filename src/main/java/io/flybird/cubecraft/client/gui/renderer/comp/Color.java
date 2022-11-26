package io.flybird.cubecraft.client.gui.renderer.comp;

import com.google.gson.*;
import io.flybird.cubecraft.client.gui.Node;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.starfish3d.render.GLUtil;
import io.flybird.starfish3d.render.ShapeRenderer;

import java.lang.reflect.Type;
import java.util.List;

public record Color(double x0, double x1, double y0, double y1, int r, int g, int b, int a)implements ComponentPartRenderer {
    @Override
    public void render(Node node) {
        int x= (int) (node.getLayoutManager().ax+x0*node.getLayoutManager().aWidth);
        int y= (int) (node.getLayoutManager().ay+y0*node.getLayoutManager().aHeight);
        int z=node.getLayoutManager().layer;
        int w= (int) (node.getLayoutManager().aWidth*(x1-x0));
        int h= (int) (node.getLayoutManager().aHeight*(y1-y0));


        ShapeRenderer.setColor(r,g,b,a);
        ShapeRenderer.drawRect(x,x+w,y,y+h,z,z);
        ShapeRenderer.setColor(256,256,256,256);

        GLUtil.enableBlend();
        GLUtil.enableDepthTest();
    }

    @Override
    public void initializeRenderer(List<ResourceLocation> loc) {
        //nope
    }

    public static class JDeserializer implements JsonDeserializer<Color>{
        @Override
        public Color deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject root=jsonElement.getAsJsonObject();
            return new Color(
                    root.get("pos").getAsJsonArray().get(0).getAsDouble(),
                    root.get("pos").getAsJsonArray().get(1).getAsDouble(),
                    root.get("pos").getAsJsonArray().get(2).getAsDouble(),
                    root.get("pos").getAsJsonArray().get(3).getAsDouble(),
                    root.get("color").getAsJsonArray().get(0).getAsInt(),
                    root.get("color").getAsJsonArray().get(1).getAsInt(),
                    root.get("color").getAsJsonArray().get(2).getAsInt(),
                    root.get("color").getAsJsonArray().get(3).getAsInt()
            );
        }
    }
}

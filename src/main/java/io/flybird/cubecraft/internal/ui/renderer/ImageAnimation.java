package io.flybird.cubecraft.internal.ui.renderer;

import com.google.gson.*;
import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.gui.base.ComponentPartRenderer;
import io.flybird.cubecraft.client.gui.component.Node;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.draw.VertexArrayUploader;
import io.flybird.starfish3d.render.textures.Texture2D;

import java.lang.reflect.Type;
import java.util.List;

public record ImageAnimation(double x0,double x1,double y0,double y1,int interval,int frames,String loc) implements ComponentPartRenderer {

    @Override
    public void render(Node node) {
        int frame= (int) ((System.currentTimeMillis()/interval)%frames);
        int x= (int) (node.getLayoutManager().ax+x0*node.getLayoutManager().aWidth);
        int y= (int) (node.getLayoutManager().ay+y0*node.getLayoutManager().aHeight);
        int z=node.getLayoutManager().layer;
        int w= (int) (node.getLayoutManager().aWidth*(x1-x0));
        int h= (int) (node.getLayoutManager().aHeight*(y1-y0));

        Texture2D tex= ClientRegistries.TEXTURE.getTexture2DContainer().get(ResourceLocation.uiTexture(this.loc.split(":")[0],this.loc.split(":")[1]).format());

        //corner
        tex.bind();
        VertexArrayBuilder builder=new VertexArrayBuilder(36);
        builder.begin();

        ShapeRenderer.drawRectUV(builder,x,x+w,y,y+h,z, 0,1,frame/(float)frames,frame/(float)frames+1/(float)frames);

        builder.end();
        VertexArrayUploader.uploadPointer(builder);
    }

    @Override
    public void initializeRenderer(List<ResourceLocation> loc) {
        loc.add(ResourceLocation.uiTexture(this.loc.split(":")[0],this.loc.split(":")[1]));
    }

    public static class JDeserializer implements JsonDeserializer<ImageAnimation> {
        @Override
        public ImageAnimation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject root=jsonElement.getAsJsonObject();
            return new ImageAnimation(
                    root.get("pos").getAsJsonArray().get(0).getAsDouble(),
                    root.get("pos").getAsJsonArray().get(1).getAsDouble(),
                    root.get("pos").getAsJsonArray().get(2).getAsDouble(),
                    root.get("pos").getAsJsonArray().get(3).getAsDouble(),
                    root.get("interval").getAsInt(),
                    root.get("frames").getAsInt(),
                    root.get("loc").getAsString());
        }
    }
}

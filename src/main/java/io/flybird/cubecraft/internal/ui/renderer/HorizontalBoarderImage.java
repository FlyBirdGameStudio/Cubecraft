package io.flybird.cubecraft.internal.ui.renderer;

import com.google.gson.*;
import io.flybird.cubecraft.client.gui.component.Node;
import io.flybird.cubecraft.client.gui.base.ComponentPartRenderer;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.register.Registries;
import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.draw.VertexArrayUploader;
import io.flybird.starfish3d.render.textures.Texture2D;

import java.lang.reflect.Type;
import java.util.List;

public record HorizontalBoarderImage(double x0,double x1,double y0,double y1,int boarder,String loc) implements ComponentPartRenderer {

    @Override
    public void render(Node node) {
        int x= (int) (node.getLayoutManager().ax+x0*node.getLayoutManager().aWidth);
        int y= (int) (node.getLayoutManager().ay+y0*node.getLayoutManager().aHeight);
        int z=node.getLayoutManager().layer;
        int w= (int) (node.getLayoutManager().aWidth*(x1-x0));
        int h= (int) (node.getLayoutManager().aHeight*(y1-y0));

        Texture2D tex= Registries.TEXTURE.getTexture2DContainer().get(ResourceLocation.uiTexture(this.loc.split(":")[0],this.loc.split(":")[1]).format());
        double tbh=(double) boarder/ tex.getWidth();

        int x0In=x+boarder,x1In=x+w-boarder,x1Out=x+w;
        VertexArrayBuilder builder=new VertexArrayBuilder(12);
        builder.begin();
        ShapeRenderer.drawRectUV(builder, x,x0In,y,y+h,z, 0,tbh,0,1);
        ShapeRenderer.drawRectUV(builder,x0In,x1In,y,y+h,z, tbh,1-tbh,0,1);
        ShapeRenderer.drawRectUV(builder,x1In,x1Out,y,y+h,z, 1-tbh,1,0,1);
        builder.end();
        VertexArrayUploader.uploadPointer(builder);
    }

    @Override
    public void initializeRenderer(List<ResourceLocation> loc) {
        loc.add(ResourceLocation.uiTexture(this.loc.split(":")[0],this.loc.split(":")[1]));
    }

    public static class JDeserializer implements JsonDeserializer<HorizontalBoarderImage>{
        @Override
        public HorizontalBoarderImage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject root=jsonElement.getAsJsonObject();
            return new HorizontalBoarderImage(
                    root.get("pos").getAsJsonArray().get(0).getAsDouble(),
                    root.get("pos").getAsJsonArray().get(1).getAsDouble(),
                    root.get("pos").getAsJsonArray().get(2).getAsDouble(),
                    root.get("pos").getAsJsonArray().get(3).getAsDouble(),
                    root.get("boarder").getAsInt(),
                    root.get("loc").getAsString());
        }
    }
}

package io.flybird.cubecraft.internal.ui.renderer;

import com.google.gson.*;
import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.gui.component.Node;
import io.flybird.cubecraft.client.resources.ResourceLocation;

import io.flybird.cubecraft.client.gui.base.ComponentPartRenderer;
import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.draw.VertexArrayUploader;
import io.flybird.starfish3d.render.textures.Texture2D;

import java.lang.reflect.Type;
import java.util.List;


public record AllBoarderImage(double x0,double x1,double y0,double y1,int boarderH,int boarderV,String loc) implements ComponentPartRenderer {


    @Override
    public void render(Node node) {
        int x= (int) (node.getLayoutManager().ax+x0*node.getLayoutManager().aWidth);
        int y= (int) (node.getLayoutManager().ay+y0*node.getLayoutManager().aHeight);
        int z=node.getLayoutManager().layer;
        int w= (int) (node.getLayoutManager().aWidth*(x1-x0));
        int h= (int) (node.getLayoutManager().aHeight*(y1-y0));

        Texture2D tex= ClientRegistries.TEXTURE.getTexture2DContainer().get(ResourceLocation.uiTexture(this.loc.split(":")[0],this.loc.split(":")[1]).format());

        double tbh=(double) boarderH/ tex.getWidth();
        double tbv=(double) boarderV/ tex.getHeight();

        int x0In=x+boarderH,x1In=x+w-boarderH,x1Out=x+w;
        int y0In=y+boarderV,y1In=y+h-boarderV,y1Out=y+h;

        //corner
        tex.bind();
        VertexArrayBuilder builder=new VertexArrayBuilder(36);
        builder.begin();

        ShapeRenderer.drawRectUV(builder, x,x0In, y,y0In,z, 0,tbh,0,tbv);
        ShapeRenderer.drawRectUV(builder, x,x0In,y1In,y1Out,z, 0,tbh,1-tbv,1);
        ShapeRenderer.drawRectUV(builder,x1In,x1Out, y,y0In,z, 1-tbh,1,0,tbv);
        ShapeRenderer.drawRectUV(builder,x1In,x1Out,y1In,y1Out,z, 1-tbh,1,1-tbv,1);

        ShapeRenderer.drawRectUV(builder,x0In,x1In, y,y0In,z, tbh,1-tbh,0,tbv);
        ShapeRenderer.drawRectUV(builder,x0In,x1In,y1In,y1Out,z, tbh,1-tbh,1-tbv,1);

        ShapeRenderer.drawRectUV(builder, x,x0In,y0In,y1In,z, 0,tbh,tbv,1-tbv);
        ShapeRenderer.drawRectUV(builder,x1In,x1Out,y0In,y1In,z, 1-tbh,1,tbv,1-tbv);

        ShapeRenderer.drawRectUV(builder,x0In,x1In,y0In,y1In,z, tbh,1-tbh,tbv,1-tbv);

        builder.end();
        VertexArrayUploader.uploadPointer(builder);
    }

    @Override
    public void initializeRenderer(List<ResourceLocation> loc) {
        loc.add(ResourceLocation.uiTexture(this.loc.split(":")[0],this.loc.split(":")[1]));
    }

    public static class JDeserializer implements JsonDeserializer<AllBoarderImage>{
        @Override
        public AllBoarderImage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject root=jsonElement.getAsJsonObject();
            return new AllBoarderImage(
                    root.get("pos").getAsJsonArray().get(0).getAsDouble(),
                    root.get("pos").getAsJsonArray().get(1).getAsDouble(),
                    root.get("pos").getAsJsonArray().get(2).getAsDouble(),
                    root.get("pos").getAsJsonArray().get(3).getAsDouble(),
                    root.get("boarderH").getAsInt(),
                    root.get("boarderV").getAsInt(),
                    root.get("loc").getAsString());
        }
    }
}
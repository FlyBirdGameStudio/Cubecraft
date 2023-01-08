package io.flybird.cubecraft.internal.ui.renderer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.gui.base.ComponentPartRenderer;
import io.flybird.cubecraft.client.gui.base.Text;
import io.flybird.cubecraft.client.gui.component.Node;
import io.flybird.cubecraft.client.resources.ResourceLocation;

import java.lang.reflect.Type;
import java.util.List;

public record Font(double x, double y, int size, int col, int yOffset, String query,float iconModifier) implements ComponentPartRenderer {
    @Override
    public void render(Node node) {
        int x= (int) (node.getLayoutManager().ax+this.x*node.getLayoutManager().aWidth);
        int y= (int) (node.getLayoutManager().ay+this.y*node.getLayoutManager().aHeight);
        Text text=node.queryText(query);
        if(text.isIcon()){
            ClientRegistries.ICON_FONT_RENDERER.render(text.getText(),x, (int) (y+yOffset-size*((iconModifier-1)/2)), text.getColor()!=0xFFFFFF?text.getColor():col, (int) (size*(iconModifier)),text.getAlignment());
        }else{
            ClientRegistries.SMOOTH_FONT_RENDERER.render(text.getText(),x,y+yOffset, text.getColor()!=0xFFFFFF?text.getColor():col, size,text.getAlignment());
        }
    }

    @Override
    public void initializeRenderer(List<ResourceLocation> loc) {
        //nope
    }

    public static class JDeserializer implements JsonDeserializer<Font>{
        @Override
        public Font deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            int col=jsonElement.getAsJsonObject().has("color")?jsonElement.getAsJsonObject().get("color").getAsInt():0xFFFFFF;
            return new Font(
                    jsonElement.getAsJsonObject().get("pos").getAsJsonArray().get(0).getAsDouble(),
                    jsonElement.getAsJsonObject().get("pos").getAsJsonArray().get(1).getAsDouble(),
                    jsonElement.getAsJsonObject().get("size").getAsInt(),
                    col,
                    jsonElement.getAsJsonObject().get("line_offset").getAsInt(),
                    jsonElement.getAsJsonObject().get("binding").getAsString(),
                    jsonElement.getAsJsonObject().has("icon_offset")?jsonElement.getAsJsonObject().get("icon_offset").getAsFloat():1.0f
            );
        }
    }
}

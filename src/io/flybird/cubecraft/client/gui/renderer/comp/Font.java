package io.flybird.cubecraft.client.gui.renderer.comp;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.flybird.cubecraft.client.gui.FontAlignment;
import io.flybird.cubecraft.client.gui.FontRenderer;
import io.flybird.cubecraft.client.gui.Node;
import io.flybird.cubecraft.client.gui.Text;
import io.flybird.cubecraft.resources.ResourceLocation;

import java.lang.reflect.Type;
import java.util.List;

public record Font(double x, double y, int size, int col, int yOffset, String query) implements ComponentPartRenderer{
    @Override
    public void render(Node node) {
        int x= (int) (node.getLayoutManager().ax+this.x*node.getLayoutManager().aWidth);
        int y= (int) (node.getLayoutManager().ay+this.y*node.getLayoutManager().aHeight);
        Text text=node.queryText(query);
        FontRenderer.render(text.getText(),x,y+yOffset,text.getColor()!=0xFFFFFF?text.getColor():col,size,text.getAlignment());
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
                    jsonElement.getAsJsonObject().get("binding").getAsString()
            );
        }
    }
}

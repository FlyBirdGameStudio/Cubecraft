package io.flybird.cubecraft.client.gui.renderer.comp;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.flybird.cubecraft.client.gui.Node;
import io.flybird.cubecraft.resources.ResourceLocation;

import java.lang.reflect.Type;
import java.util.List;

public interface ComponentPartRenderer{
    void render(Node node);
    void initializeRenderer(List<ResourceLocation> loc);

    class JDeserializer implements JsonDeserializer<ComponentPartRenderer>{
        @Override
        public ComponentPartRenderer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return switch (jsonElement.getAsJsonObject().get("type").getAsString()){
                case "image_all_boarder"->jsonDeserializationContext.deserialize(jsonElement,AllBoarderImage.class);
                case "image_horizontal_boarder"->jsonDeserializationContext.deserialize(jsonElement,HorizontalBoarderImage.class);
                case "image_vertical_boarder"->jsonDeserializationContext.deserialize(jsonElement,VerticalBorderImage.class);
                case "font"->jsonDeserializationContext.deserialize(jsonElement,Font.class);
                case "color"->jsonDeserializationContext.deserialize(jsonElement,Color.class);
                default -> throw new IllegalStateException("no matched name:"+jsonElement.getAsJsonObject().get("type").getAsString());
            };
        }
    }
}

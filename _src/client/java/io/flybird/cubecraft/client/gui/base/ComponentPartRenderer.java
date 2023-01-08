package io.flybird.cubecraft.client.gui.base;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.gui.component.Node;
import io.flybird.cubecraft.client.resources.ResourceLocation;

import java.lang.reflect.Type;
import java.util.List;

public interface ComponentPartRenderer{
    void render(Node node);
    void initializeRenderer(List<ResourceLocation> loc);

    class JDeserializer implements JsonDeserializer<ComponentPartRenderer>{
        @Override
        public ComponentPartRenderer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String id= jsonElement.getAsJsonObject().get("type").getAsString();
            Class<?> clazz= ClientRegistries.GUI_MANAGER.getRendererPartClass(id);
            return jsonDeserializationContext.deserialize(jsonElement,clazz);
        }
    }
}

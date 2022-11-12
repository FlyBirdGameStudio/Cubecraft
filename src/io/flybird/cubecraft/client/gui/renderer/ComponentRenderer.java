package io.flybird.cubecraft.client.gui.renderer;

import com.google.gson.*;
import io.flybird.cubecraft.client.gui.Node;
import io.flybird.cubecraft.client.gui.renderer.comp.ComponentPartRenderer;
import io.flybird.cubecraft.resources.ResourceLocation;
import io.flybird.util.container.CollectionUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComponentRenderer{
    private final HashMap<String, ComponentPartRenderer[]> renderers;

    public ComponentRenderer(HashMap<String, ComponentPartRenderer[]> renderers) {
        this.renderers = renderers;
    }

    public void render(Node node){
        for (ComponentPartRenderer componentPartRenderer:this.renderers.get(node.getStatement())){
            componentPartRenderer.render(node);
        }
    }

    public void initializeModel(List<ResourceLocation> loc){
        CollectionUtil.iterateMap(this.renderers,((key, item) -> {
            for (ComponentPartRenderer renderer:item){
                renderer.initializeRenderer(loc);
            }
        }));
    }

    public static class JDeserializer implements JsonDeserializer<ComponentRenderer>{
        @Override
        public ComponentRenderer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            HashMap<String,ComponentPartRenderer[]> renderers=new HashMap<>();
            for (JsonElement e:jsonElement.getAsJsonArray()) {
                ArrayList<ComponentPartRenderer> rendererList=new ArrayList<>();
                JsonObject obj=e.getAsJsonObject();
                for (JsonElement ele : obj.get("components").getAsJsonArray()) {
                    rendererList.add(jsonDeserializationContext.deserialize(ele.getAsJsonObject(),ComponentPartRenderer.class));
                }
                renderers.put(obj.get("state").getAsString(),rendererList.toArray(new ComponentPartRenderer[0]));
            }
            return new ComponentRenderer(renderers);
        }
    }
}

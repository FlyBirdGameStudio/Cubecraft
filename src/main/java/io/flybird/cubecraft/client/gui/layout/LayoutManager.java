package io.flybird.cubecraft.client.gui.layout;

import io.flybird.cubecraft.internal.ui.layout.FlowLayout;
import io.flybird.cubecraft.internal.ui.layout.OriginLayout;
import io.flybird.cubecraft.internal.ui.layout.ViewportLayout;
import io.flybird.cubecraft.register.Registries;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import com.google.gson.*;
import org.w3c.dom.Element;

import java.lang.reflect.Type;

public abstract class LayoutManager {
    public int ax,ay;
    public int width,height;
    public int layer;
    public int aWidth;
    public int aHeight;
    public boolean[] scaleEnabled;

    public abstract void resize(int x,int y,int scrWidth,int scrHeight);
    protected Border border=new Border(0,0,0,0);

    public Border getBorder() {
        return border;
    }

    public void setBorder(Border border) {
        this.border = border;
    }

    public static class XMLDeserializer implements FAMLDeserializer<LayoutManager> {
        @Override
        public LayoutManager deserialize(Element element, XmlReader famlLoadingContext) {
            return Registries.FAML_READER.deserialize(element,Registries.SCREEN_LOADER.getLayoutClass(element.getAttribute("type")));
        }
    }

    public static class JDeserializer implements JsonDeserializer<LayoutManager>{
        @Override
        public LayoutManager deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject layout=jsonElement.getAsJsonObject();
            JsonArray arr = layout.get("layout").getAsJsonArray();

            LayoutManager layoutManager= switch (layout.get("type").getAsString()) {
                case "origin" -> new OriginLayout(
                        arr.get(0).getAsInt(),
                        arr.get(1).getAsInt(),
                        arr.get(2).getAsInt(),
                        arr.get(3).getAsInt(),
                        OriginLayout.Origin.from(layout.get("meta").getAsString()),
                        layout.get("layer").getAsInt()
                );
                case "viewport" -> new ViewportLayout(
                        arr.get(0).getAsInt(),
                        arr.get(1).getAsInt(),
                        arr.get(2).getAsInt(),
                        arr.get(3).getAsInt(),
                        layout.get("layer").getAsInt()
                );

                default -> null;
            };
            if(layout.has("border")) {
                JsonArray border = layout.get("border").getAsJsonArray();
                layoutManager.setBorder(new Border(
                        border.get(0).getAsInt(),
                        border.get(1).getAsInt(),
                        border.get(2).getAsInt(),
                        border.get(3).getAsInt()
                ));
            }
            return layoutManager;
        }
    }

    public static Class<? extends LayoutManager> getClass(String id){
        return switch (id){
            case "origin"->OriginLayout.class;
            case "viewport"->ViewportLayout.class;
            case "flow"-> FlowLayout.class;
            default -> throw new IllegalArgumentException("no matched constant named "+id);
        };
    }
}

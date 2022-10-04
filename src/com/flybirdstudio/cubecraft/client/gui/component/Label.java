package com.flybirdstudio.cubecraft.client.gui.component;



import com.flybirdstudio.cubecraft.client.gui.FontRenderer;
import com.flybirdstudio.cubecraft.client.gui.Text;
import com.flybirdstudio.cubecraft.client.gui.layout.LayoutManager;
import com.flybirdstudio.util.file.faml.FAMLDeserializer;
import com.flybirdstudio.util.file.faml.XmlReader;
import com.google.gson.*;
import org.w3c.dom.Element;

import java.lang.reflect.Type;

public class Label extends Component {
    public Text text;
    public int size=16;
    public int color;
    public Label(Text text) {
        this.text=text;
    }


    @Override
    public void render() {
        FontRenderer.renderShadow(text.getText(),layoutManager.ax,layoutManager.ay, text.getColor(), layoutManager.aHeight,text.getAlignment());
    }

    public static class XMLDeserializer implements FAMLDeserializer<Label> {
        @Override
        public Label deserialize(Element element, XmlReader famlLoadingContext) {
            Label label=new Label(famlLoadingContext.deserialize((Element) element.getElementsByTagName("text").item(0),Text.class));
            label.setLayout(famlLoadingContext.deserialize((Element) element.getElementsByTagName("layout").item(0), LayoutManager.class));
            return label;
        }
    }

    public static class JDeserializer implements JsonDeserializer<Label>{
        @Override
        public Label deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject node=jsonElement.getAsJsonObject();
            Label label=new Label(jsonDeserializationContext.deserialize(node.get("text"),Text.class));
            label.setLayout(jsonDeserializationContext.deserialize(node.get("layout"),LayoutManager.class));
            return label;
        }
    }
}

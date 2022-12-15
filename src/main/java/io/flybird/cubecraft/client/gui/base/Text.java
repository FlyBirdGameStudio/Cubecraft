package io.flybird.cubecraft.client.gui.base;

import io.flybird.cubecraft.client.resources.ResourceManager;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import io.flybird.util.math.MathHelper;
import com.google.gson.*;
import org.w3c.dom.Element;

import java.lang.reflect.Type;
import java.util.Random;

import static io.flybird.cubecraft.register.Registries.I18N;

public class Text {
    private String text;
    private int color;
    private FontAlignment alignment;

    public Text(String text, int color, FontAlignment alignment) {
        this.text = text;
        this.color = color;
        this.alignment = alignment;
    }

    public FontAlignment getAlignment() {
        return alignment;
    }

    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAlignment(FontAlignment alignment) {
        this.alignment = alignment;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public static class XMLDeserializer implements FAMLDeserializer<Text> {
        @Override
        public Text deserialize(Element element, XmlReader famlLoadingContext) {
            return new Text(
                    getText(element.getAttribute("type"), element.getTextContent()),
                    MathHelper.hex2Int(element.getAttribute("color")),
                    FontAlignment.from(element.getAttribute("align"))
            );
        }
    }

    public static class JDeserializer implements JsonDeserializer<Text> {
        @Override
        public Text deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject obj = jsonElement.getAsJsonObject();
            return new Text(
                    getText(obj.get("type").getAsString(), obj.get("src").getAsString()),
                    Integer.parseInt(obj.get("color").getAsString()),
                    FontAlignment.from(obj.get("align").getAsString())
            );
        }
    }

    private static String getText(String type, String src) {
        return switch (type) {
            case "raw" -> src;
            case "lang" -> I18N.get(src);
            case "random" -> {
                String[] splash;
                splash = new Gson().fromJson(ResourceManager.instance.getResource(src).getAsText(), String[].class);
                yield splash[new Random().nextInt(splash.length)];
            }
            default -> throw new IllegalArgumentException("no matched constant named %s".formatted(type));
        };
    }

}

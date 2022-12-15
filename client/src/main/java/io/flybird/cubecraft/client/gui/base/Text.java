package io.flybird.cubecraft.client.gui.base;

import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.gui.font.FontAlignment;
import io.flybird.cubecraft.register.Registries;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import io.flybird.util.math.MathHelper;
import com.google.gson.*;
import org.w3c.dom.Element;

import java.util.Random;

public class Text {
    private String text;
    private int color;
    private FontAlignment alignment;
    private final boolean isIcon;

    public Text(String text, int color, FontAlignment alignment, boolean isIcon) {
        this.text = text;
        this.color = color;
        this.alignment = alignment;
        this.isIcon = isIcon;
    }

    public Text(String text, int color, FontAlignment alignment){
        this(text,color,alignment,false);
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
                    FontAlignment.from(element.getAttribute("align")),
                    element.getAttribute("type").equals("icon"));
        }
    }


    private static String getText(String type, String src) {
        return switch (type) {
            case "raw" -> src;
            case "lang" -> Registries.I18N.get(src);
            case "icon" -> String.valueOf(((char) MathHelper.hex2Int(src)));
            case "random" -> {
                String[] splash;
                splash = new Gson().fromJson(ClientRegistries.RESOURCE_MANAGER.getResource(src).getAsText(), String[].class);
                yield splash[new Random().nextInt(splash.length)];
            }
            default -> throw new IllegalArgumentException("no matched constant named %s".formatted(type));
        };
    }

    public boolean isIcon() {
        return isIcon;
    }
}

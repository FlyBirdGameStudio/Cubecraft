package io.flybird.cubecraft.internal.ui.component;


import io.flybird.cubecraft.client.gui.base.Text;
import io.flybird.cubecraft.client.gui.component.Component;
import io.flybird.cubecraft.client.gui.component.LayoutManager;
import io.flybird.cubecraft.register.Registries;
import io.flybird.starfish3d.event.MouseClickEvent;
import io.flybird.util.event.Event;
import io.flybird.util.event.EventHandler;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import com.google.gson.*;
import org.w3c.dom.Element;

import java.lang.reflect.Type;
import java.util.Objects;

public class Button extends Component {
    private Text text;
    private final String style;

    public Button(Text text, String style) {
        this.text = text;
        this.style = style;
    }

    public boolean enabled = true;
    public boolean hovered = false;

    public void setText(Text text) {
        this.text = text;
    }

    @Override
    public String getStatement() {
        return this.style + ":" + (this.enabled ? this.hovered ? "selected" : "normal" : "disabled");
    }

    @Override
    public void tick() {
        int scale = Registries.CLIENT.getGameSetting().getValueAsInt("client.render.gui.scale", 2);
        int xm = Registries.CLIENT.getWindow().getMouseX() / scale;
        int ym = Registries.CLIENT.getWindow().getMouseFixedY() / scale;
        int x0 = this.layoutManager.ax;
        int x1 = x0 + this.layoutManager.aWidth;
        int y0 = this.layoutManager.ay;
        int y1 = y0 + this.layoutManager.aHeight;
        this.hovered = xm > x0 && xm < x1 && ym > y0 && ym < y1;
    }

    @EventHandler
    public void onClicked(MouseClickEvent event) {
        int scale = Registries.CLIENT.getGameSetting().getValueAsInt("client.render.gui.scale", 2);
        int x0 = this.layoutManager.ax;
        int x1 = x0 + this.layoutManager.aWidth;
        int y0 = this.layoutManager.ay;
        int y1 = y0 + this.layoutManager.aHeight;
        int xm = event.x() / scale, ym = event.fixedY() / scale;
        if (xm > x0 && xm < x1 && ym > y0 && ym < y1) {
            if(enabled) {
                Registries.CLIENT.getClientEventBus().callEvent(new ActionEvent(this));
            }
        }
    }

    public static class XMLDeserializer implements FAMLDeserializer<Button> {
        @Override
        public Button deserialize(Element element, XmlReader famlLoadingContext) {
            Button btn = new Button(
                    famlLoadingContext.deserialize((Element) element.getElementsByTagName("text").item(0), Text.class),
                    element.hasAttribute("style") ? element.getAttribute("style") : "default"
            );
            btn.setLayout(famlLoadingContext.deserialize((Element) element.getElementsByTagName("layout").item(0), LayoutManager.class));
            return btn;
        }
    }

    public static class JDeserializer implements JsonDeserializer<Button> {
        @Override
        public Button deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject button = jsonElement.getAsJsonObject();
            Button btn = new Button(jsonDeserializationContext.deserialize(button.get("text"), Text.class), "default");
            btn.setLayout(jsonDeserializationContext.deserialize(button.get("layout"), LayoutManager.class));
            return btn;
        }
    }

    public record ActionEvent(Button component) implements Event {
    }

    @Override
    public Text queryText(String query) {
        if (Objects.equals(query, "button:text")) {
            return this.text;
        }
        return super.queryText(query);
    }
}
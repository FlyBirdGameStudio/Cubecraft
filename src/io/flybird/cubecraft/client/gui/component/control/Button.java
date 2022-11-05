package io.flybird.cubecraft.client.gui.component.control;


import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.client.gui.FontAlignment;
import io.flybird.cubecraft.client.gui.FontRenderer;
import io.flybird.cubecraft.client.gui.Text;
import io.flybird.cubecraft.client.gui.component.Component;
import io.flybird.cubecraft.client.gui.layout.LayoutManager;
import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.resources.ResourceManager;
import io.flybird.starfish3d.platform.Display;
import io.flybird.starfish3d.platform.input.Mouse;
import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.util.event.Event;
import io.flybird.util.file.faml.FAMLDeserializer;
import io.flybird.util.file.faml.XmlReader;
import com.google.gson.*;
import org.w3c.dom.Element;

import java.lang.reflect.Type;

public class Button extends Component {
    static int offsetNormal,offsetPressed, offsetDisabled;

    static {
        initRenderController();
    }

    static void initRenderController(){
        Gson gson=new GsonBuilder().registerTypeAdapter(Button.class, (JsonDeserializer) (jsonElement, type, jsonDeserializationContext) -> {
            offsetNormal=jsonElement.getAsJsonObject().get("text_offset").getAsJsonObject().get("normal").getAsInt();
            offsetPressed=jsonElement.getAsJsonObject().get("text_offset").getAsJsonObject().get("pressed").getAsInt();
            offsetDisabled =jsonElement.getAsJsonObject().get("text_offset").getAsJsonObject().get("disabled").getAsInt();
            return null;
        }).create();
        //gson.fromJson(ResourceManager.instance.getResourceAsText("/resource/ui/component/button_render_controller.json"),Button.class);
    }


    private Listener listener;

    private Text text;

    public Button(int textColor, String text) {
        this(new Text(text, textColor, FontAlignment.MIDDLE));
    }

    public Button(Text text) {
        this.text = text;
    }

    public boolean enabled = true;
    public boolean hovered = false;

    private void render(int x, int y, int w, int h, int layer) {
        FontRenderer.renderShadow(this.text.getText(), x + w / 2, y + 8-(this.enabled?(this.hovered?offsetPressed:offsetNormal): offsetDisabled), this.text.getColor(), 8, this.text.getAlignment());
        Registry.getTextureManager().getTexture2DContainer().bind("/resource/cubecraft/ui/texture/controls/button.png");

        int texturePosition;
        if (this.enabled) {
            if (this.hovered) {
                texturePosition = 1;
            } else {
                texturePosition = 0;
            }
        } else {
            texturePosition = 2;
        }
        ShapeRenderer.begin();
        ShapeRenderer.drawRectUV(x, x + h, y, y + h, -1, -1, 0,
                0.1, texturePosition / 3f, (texturePosition + 1) / 3f);

        ShapeRenderer.drawRectUV(x + h, x + w - h, y,
                y + h, -1, -1, 0.1, 0.9, texturePosition / 3f, (texturePosition + 1) / 3f);

        ShapeRenderer.drawRectUV(x + w - h, x + w, y, y + h, -1, -1,
                0.9, 1, texturePosition / 3f, (texturePosition + 1) / 3f);
        ShapeRenderer.end();
    }

    public void setText(Text text) {
        this.text = text;
    }

    @Override
    public void tick() {
        int scale= GameSetting.instance.getValueAsInt("client.render.gui.scale",2);
        int xm = Mouse.getX()/ scale;
        int ym = (-Mouse.getY()+ Display.getHeight())/scale;
        int x0 = this.layoutManager.ax;
        int x1 = x0 + this.layoutManager.aWidth;
        int y0 = this.layoutManager.ay;
        int y1 = y0 + this.layoutManager.aHeight;
        if (xm > x0 && xm < x1 && ym > y0 && ym < y1) {
            this.hovered = true;
        } else {
            this.hovered = false;
        }
    }

    @Override
    public void render() {
        this.render(this.layoutManager.ax, this.layoutManager.ay, this.layoutManager.aWidth, this.layoutManager.aHeight, this.layer);
    }

    public void setListener(Listener listener) {
        this.listener = listener;

    }

    public interface Listener {
        void buttonClicked();
    }

    @Override
    public void onClicked(int xm, int ym) {
        int x0 = this.layoutManager.ax;
        int x1 = x0 + this.layoutManager.aWidth;
        int y0 = this.layoutManager.ay;
        int y1 = y0 + this.layoutManager.aHeight;
        if (xm > x0 && xm < x1 && ym > y0 && ym < y1) {
            Registry.getClient().getClientEventBus().callEvent(new ActionEvent(this));
        }
    }

    static {
        Registry.getTextureManager().createTexture2D(ResourceManager.instance.getResource("/resource/cubecraft/ui/texture/controls/button.png"), false, false);
    }

    public static class XMLDeserializer implements FAMLDeserializer<Button> {
        @Override
        public Button deserialize(Element element, XmlReader famlLoadingContext) {
            Button btn = new Button(famlLoadingContext.deserialize((Element) element.getElementsByTagName("text").item(0), Text.class));
            btn.setLayout(famlLoadingContext.deserialize((Element) element.getElementsByTagName("layout").item(0), LayoutManager.class));
            return btn;
        }
    }

    public static class JDeserializer implements JsonDeserializer<Button> {
        @Override
        public Button deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject button = jsonElement.getAsJsonObject();
            Button btn = new Button(jsonDeserializationContext.deserialize(button.get("text"), Text.class));
            btn.setLayout(jsonDeserializationContext.deserialize(button.get("layout"), LayoutManager.class));
            return btn;
        }
    }

    public record ActionEvent(Button component) implements Event {}
}

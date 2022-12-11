package io.flybird.cubecraft.internal.ui.component;

import io.flybird.cubecraft.client.gui.FontAlignment;
import io.flybird.cubecraft.client.gui.component.Component;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.client.gui.Text;
import io.flybird.cubecraft.client.gui.layout.LayoutManager;
import io.flybird.starfish3d.event.MouseClickEvent;
import io.flybird.util.event.EventHandler;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import org.w3c.dom.Element;

import java.util.Objects;

public class TopBar extends Component {
    private final Text text;

    public TopBar(Text text) {
        this.text = text;
    }

    @Override
    public void render() {
        Registries.COMPONENT_RENDERER.get(this.getClass()).render(this);
    }

    public static class XMLDeserializer implements FAMLDeserializer<TopBar> {

        @Override
        public TopBar deserialize(Element element, XmlReader famlLoadingContext) {
            TopBar topBar = new TopBar(famlLoadingContext.deserialize((Element) element.getElementsByTagName("text").item(0), Text.class));
            topBar.setLayout(famlLoadingContext.deserialize((Element) element.getElementsByTagName("layout").item(0), LayoutManager.class));
            return topBar;
        }
    }

    @EventHandler
    public void onClicked(MouseClickEvent e){
        int scale= Registries.CLIENT.getGameSetting().getValueAsInt("client.render.gui.scale",2);
        int xm = e.x()/ scale;
        int ym = e.fixedY()/scale;
        int x0 = this.layoutManager.ax;
        int x1 = x0 + this.layoutManager.aWidth;
        int y0 = this.layoutManager.ay;
        int y1 = y0 + this.layoutManager.aHeight;
        if (xm > x0 && xm < x1 && ym > y0 && ym < y1) {
            if (Registries.CLIENT.getScreen().getParentScreen() != null) {
                Registries.CLIENT.setScreen(Registries.CLIENT.getScreen().getParentScreen());
            }
        }
    }

    @Override
    public String getStatement() {
        int scale= Registries.CLIENT.getGameSetting().getValueAsInt("client.render.gui.scale",2);
        int xm = Registries.CLIENT.getWindow().getMouseX()/ scale;
        int ym = Registries.CLIENT.getWindow().getMouseFixedY()/scale;
        int x0 = this.layoutManager.ax;
        int x1 = x0 + this.layoutManager.aHeight;
        int y0 = this.layoutManager.ay;
        int y1 = y0 + this.layoutManager.aHeight;
        if (xm > x0 && xm < x1 && ym > y0 && ym < y1) {
            return "back_pressed";
        } else {
            return "default";
        }
    }

    @Override
    public Text queryText(String query) {
        if(Objects.equals(query, "topbar:text")) {
            return text;
        }else {
            return new Text(" < ",0xffffff, FontAlignment.LEFT);
        }
    }
}

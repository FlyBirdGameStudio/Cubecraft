package io.flybird.cubecraft.client.gui.component;

import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.client.gui.FontRenderer;
import io.flybird.cubecraft.client.gui.Text;
import io.flybird.cubecraft.client.gui.layout.LayoutManager;
import io.flybird.cubecraft.resources.ResourceManager;
import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.util.file.faml.FAMLDeserializer;
import io.flybird.util.file.faml.XmlReader;
import org.w3c.dom.Element;

public class TopBar extends Component {
    private final Text text;

    public TopBar(Text text) {
        this.text = text;
    }

    @Override
    public void render() {
        int x = layoutManager.ax, y = layoutManager.ay, w = layoutManager.aWidth, h = layoutManager.aHeight;
        FontRenderer.renderShadow(this.text.getText(), x + w / 2, y + 8, this.text.getColor(), 8, this.text.getAlignment());
        Registry.getTextureManager().getTexture2DContainer().bind("/resource/cubecraft/ui/texture/controls/topbar.png");
        ShapeRenderer.begin();
        ShapeRenderer.drawRectUV(x, x + w, y, y + h, -1, -1, 0,
                0.1, 0, 1);
        ShapeRenderer.end();
    }

    static {
        Registry.getTextureManager().createTexture2D(ResourceManager.instance.getResource("/resource/cubecraft/ui/texture/controls/topbar.png"), false, false);
    }

    public static class XMLDeserializer implements FAMLDeserializer<TopBar> {

        @Override
        public TopBar deserialize(Element element, XmlReader famlLoadingContext) {
            TopBar topBar = new TopBar(famlLoadingContext.deserialize((Element) element.getElementsByTagName("text").item(0), Text.class));
            topBar.setLayout(famlLoadingContext.deserialize((Element) element.getElementsByTagName("layout").item(0), LayoutManager.class));
            return topBar;
        }
    }
}

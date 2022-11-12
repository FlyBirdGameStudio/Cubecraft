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
        Registry.getComponentRenderManager().get(this.getClass()).render(this);
    }

    public static class XMLDeserializer implements FAMLDeserializer<TopBar> {

        @Override
        public TopBar deserialize(Element element, XmlReader famlLoadingContext) {
            TopBar topBar = new TopBar(famlLoadingContext.deserialize((Element) element.getElementsByTagName("text").item(0), Text.class));
            topBar.setLayout(famlLoadingContext.deserialize((Element) element.getElementsByTagName("layout").item(0), LayoutManager.class));
            return topBar;
        }
    }

    @Override
    public Text queryText(String query) {
        return text;
    }
}

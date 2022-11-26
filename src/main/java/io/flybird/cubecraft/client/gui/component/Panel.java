package io.flybird.cubecraft.client.gui.component;

import io.flybird.cubecraft.client.gui.layout.LayoutManager;
import io.flybird.cubecraft.register.RenderRegistry;
import io.flybird.util.file.faml.FAMLDeserializer;
import io.flybird.util.file.faml.XmlReader;
import org.w3c.dom.Element;

public class Panel extends Component{
    @Override
    public void render() {
        RenderRegistry.getComponentRenderManager().get(this.getClass()).render(this);
    }

    public static class XMLDeserializer implements FAMLDeserializer<Panel> {
        @Override
        public Panel deserialize(Element element, XmlReader famlLoadingContext) {
            Panel panel = new Panel();
            panel.setLayout(famlLoadingContext.deserialize((Element) element.getElementsByTagName("layout").item(0), LayoutManager.class));
            panel.layoutManager.layer=-1;
            return panel;
        }
    }
}

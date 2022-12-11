package io.flybird.cubecraft.internal.ui.component;

import io.flybird.cubecraft.client.gui.component.Component;
import io.flybird.cubecraft.client.gui.layout.LayoutManager;
import io.flybird.cubecraft.register.Registries;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import org.w3c.dom.Element;

public class CircleWaitingAnimation extends Component {
    @Override
    public void render() {
        Registries.COMPONENT_RENDERER.get(this.getClass()).render(this);
    }

    public static class XMLDeserializer implements FAMLDeserializer<CircleWaitingAnimation> {
        @Override
        public CircleWaitingAnimation deserialize(Element element, XmlReader famlLoadingContext) {
            CircleWaitingAnimation ani=new CircleWaitingAnimation();
            ani.setLayout(famlLoadingContext.deserialize((Element) element.getElementsByTagName("layout").item(0), LayoutManager.class));
            return ani;
        }
    }
}

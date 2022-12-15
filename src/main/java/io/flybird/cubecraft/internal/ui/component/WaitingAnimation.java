package io.flybird.cubecraft.internal.ui.component;

import io.flybird.cubecraft.client.gui.component.Component;
import io.flybird.cubecraft.client.gui.component.LayoutManager;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import org.w3c.dom.Element;

public class WaitingAnimation extends Component {
    public static class XMLDeserializer implements FAMLDeserializer<WaitingAnimation> {
        @Override
        public WaitingAnimation deserialize(Element element, XmlReader famlLoadingContext) {
            WaitingAnimation ani=new WaitingAnimation();
            ani.setLayout(famlLoadingContext.deserialize((Element) element.getElementsByTagName("layout").item(0), LayoutManager.class));
            return ani;
        }
    }
}

package io.flybird.cubecraft.internal.ui.component;

import io.flybird.cubecraft.client.gui.component.Component;
import io.flybird.cubecraft.client.gui.component.LayoutManager;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import org.w3c.dom.Element;

public class Panel extends Component {
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

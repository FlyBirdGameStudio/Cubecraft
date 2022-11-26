package io.flybird.cubecraft.client.gui;

import io.flybird.cubecraft.client.gui.component.*;
import io.flybird.cubecraft.client.gui.component.control.Button;
import io.flybird.cubecraft.client.gui.component.control.TextBar;
import io.flybird.cubecraft.client.gui.layout.FlowLayout;
import io.flybird.cubecraft.client.gui.layout.LayoutManager;
import io.flybird.cubecraft.client.gui.layout.OriginLayout;
import io.flybird.cubecraft.client.gui.layout.ViewportLayout;
import io.flybird.cubecraft.client.gui.screen.Screen;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.client.resources.ResourceManager;
import io.flybird.util.file.faml.XmlReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ScreenLoader {
    private static final XmlReader famlLoader = new XmlReader();

    public static void initialize() {
        //basic
        famlLoader.registerDeserializer(Text.class, new Text.XMLDeserializer());
        famlLoader.registerDeserializer(LayoutManager.class, new LayoutManager.XMLDeserializer());
        famlLoader.registerDeserializer(Screen.class, new Screen.XMLDeserializer());

        //layout
        famlLoader.registerDeserializer(OriginLayout.class, new OriginLayout.XMLDeserializer());
        famlLoader.registerDeserializer(ViewportLayout.class, new ViewportLayout.XMLDeserializer());
        famlLoader.registerDeserializer(FlowLayout.class, new FlowLayout.XMLDeserializer());

        famlLoader.registerDeserializer(TopBar.class, new TopBar.XMLDeserializer());
        famlLoader.registerDeserializer(Panel.class, new Panel.XMLDeserializer());

        //controls
        famlLoader.registerDeserializer(Button.class, new Button.XMLDeserializer());
        famlLoader.registerDeserializer(CircleWaitingAnimation.class, new CircleWaitingAnimation.XMLDeserializer());
        famlLoader.registerDeserializer(Label.class, new Label.XMLDeserializer());
        famlLoader.registerDeserializer(ImageRenderer.class, new ImageRenderer.XMLDeserializer());
        famlLoader.registerDeserializer(SplashText.class, new SplashText.XMLDeserializer());
        famlLoader.registerDeserializer(TextBar.class, new TextBar.XMLDeserializer());
    }

    static {
        initialize();
    }

    public static XmlReader getSharedXMLReader() {
        return famlLoader;
    }


    /**
     * load a screen from xml file
     *
     * @param path resource location
     * @return loaded screen object
     */
    private static Screen loadFromXML(String namespace, String path) {
        Document dom = ResourceManager.instance.getResource(ResourceLocation.uiScreen(namespace, path)).getAsDom();
        Element faml = (Element) dom.getElementsByTagName("faml").item(0);
        if (!faml.getAttribute("ext").equals("cubecraft_ui")) {
            throw new RuntimeException("invalid ui xml");
        }
        return famlLoader.deserialize(faml, Screen.class);

    }

    /**
     * load an ui file,using ext name to manifest load type(xml/json)
     *
     * @param uiPosition resource location
     * @return loaded screen object
     */
    public static Screen loadByExtName(String namespace, String uiPosition) {
        if (uiPosition.endsWith(".xml")) {
            return loadFromXML(namespace, uiPosition);
        } else {
            throw new RuntimeException("loaded a none exist file!");
        }
    }
}

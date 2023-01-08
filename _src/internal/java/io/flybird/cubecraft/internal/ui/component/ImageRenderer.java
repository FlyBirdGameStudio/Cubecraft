package io.flybird.cubecraft.internal.ui.component;


import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.gui.component.Component;
import io.flybird.cubecraft.client.gui.component.LayoutManager;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.quantum3d.ShapeRenderer;
import io.flybird.quantum3d.textures.Texture2D;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import org.w3c.dom.Element;

public class ImageRenderer extends Component {
    private final Texture2D texture = new Texture2D(false, false);
    public final HorizontalClipping hClip;
    public final VerticalClipping vClip;

    public ImageRenderer(String file, HorizontalClipping hClip, VerticalClipping vClip) {
        this.hClip = hClip;
        this.vClip = vClip;
        this.texture.generateTexture();
        this.texture.load(ClientRegistries.RESOURCE_MANAGER.getResource(ResourceLocation.uiTexture(
                file.split(":")[0],
                file.split(":")[1]
        )));
        this.texture.bind();
    }

    @Override
    public void render() {
        this.texture.bind();
        float u0 = 0, u1 = 0, v0 = 0, v1 = 0;
        switch (this.vClip) {
            case UP -> {
                v0 = 0;
                v1 = this.layoutManager.height * 1.0f / this.texture.getHeight();
            }
            case MIDDLE -> {
                v0 = (this.texture.getHeight() / 2.0f - this.layoutManager.height / 2.0f) / this.texture.getHeight();
                v1 = (this.texture.getHeight() / 2.0f + this.layoutManager.height / 2.0f) / this.texture.getHeight();
            }
            case DOWN -> {
                v0 = (this.texture.getHeight() / 2.0f - this.layoutManager.height / 2.0f) / this.texture.getHeight();
                v1 = 1;
            }
            case SCALE -> {
                v0 = 0;
                v1 = 1;
            }
        }
        switch (this.hClip) {
            case LEFT -> {
                u0 = 0;
                u1 = (this.layoutManager.width * 1.0f / this.texture.getWidth()) / this.texture.getWidth();
            }
            case MIDDLE -> {
                u0 = (this.texture.getWidth() / 2.0f - this.layoutManager.width / 2.0f) / this.texture.getWidth();
                u1 = (this.texture.getWidth() / 2.0f + this.layoutManager.width / 2.0f) / this.texture.getWidth();
            }
            case RIGHT -> {
                u0 = (this.texture.getWidth() / 2.0f - this.layoutManager.width / 2.0f) / this.texture.getWidth();
                u1 = 1;
            }
            case SCALE -> {
                u0 = 0;
                u1 = 1;
            }
        }
        ShapeRenderer.setColor(0xFFFFFF);
        ShapeRenderer.begin();

        ShapeRenderer.drawRectUV(layoutManager.ax,
                layoutManager.ax + layoutManager.width,
                layoutManager.ay,
                layoutManager.ay + layoutManager.height,
                layer, u0, u1, v0, v1);
        ShapeRenderer.end();
    }

    public static class XMLDeserializer implements FAMLDeserializer<ImageRenderer> {
        @Override
        public ImageRenderer deserialize(Element element, XmlReader famlLoadingContext) {
            ImageRenderer imageRenderer = new ImageRenderer(
                    element.getAttribute("img"),
                    HorizontalClipping.from(element.getAttribute("h-clip")),
                    VerticalClipping.from(element.getAttribute("v-clip"))
            );
            imageRenderer.setLayout(famlLoadingContext.deserialize((Element) element.getElementsByTagName("layout").item(0), LayoutManager.class));
            return imageRenderer;
        }
    }

    public enum VerticalClipping {
        UP,
        MIDDLE,
        DOWN,
        SCALE;

        public static VerticalClipping from(String attribute) {
            return switch (attribute) {
                case "up" -> UP;
                case "middle" -> MIDDLE;
                case "down" -> DOWN;
                case "scale" -> SCALE;
                default -> throw new IllegalArgumentException("no matched constant named %s".formatted(attribute));
            };
        }
    }

    public enum HorizontalClipping {
        LEFT,
        MIDDLE,
        RIGHT,
        SCALE;

        public static HorizontalClipping from(String attribute) {
            return switch (attribute) {
                case "left" -> LEFT;
                case "middle" -> MIDDLE;
                case "right" -> RIGHT;
                case "scale" -> SCALE;
                default -> throw new IllegalArgumentException("no matched constant named %s".formatted(attribute));
            };
        }

    }
}

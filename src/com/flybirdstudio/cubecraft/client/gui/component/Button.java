package com.flybirdstudio.cubecraft.client.gui.component;


import com.flybirdstudio.cubecraft.client.gui.FontAlignment;
import com.flybirdstudio.cubecraft.client.gui.FontRenderer;
import com.flybirdstudio.cubecraft.registery.Registery;
import com.flybirdstudio.starfish3d.render.ShapeRenderer;

public class Button extends Component {
    public static final int HEIGHT = 20;
    public String text;
    public int textColor;
    public int color;
    private Listener listener;

    public Button(int color, int textColor, String text) {
        this.textColor = textColor;
        this.text = text;
    }

    public boolean enabled = true;
    public boolean hovered = false;

    private void render(int x, int y, int w, int h, int layer) {
        FontRenderer.render(text, x + w / 2, y + 8, textColor, 8, FontAlignment.MIDDLE);
        Registery.getTextureManager().getTexture2DContainer().bind("/resource/textures/gui/controls/button.png");

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


    @Override
    public void tick(int xm, int ym) {
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
        if (xm > x0 && xm < x1 && ym > y0 && ym < y1 && this.listener != null) {
            this.listener.buttonClicked();
        }
    }

    static {
        Registery.getTextureManager().createTexture2D("/resource/textures/gui/controls/button.png", false, false);
    }
}

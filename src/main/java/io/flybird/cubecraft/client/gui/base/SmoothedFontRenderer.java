package io.flybird.cubecraft.client.gui.base;

import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.draw.VertexArrayUploader;
import io.flybird.starfish3d.render.textures.Texture2D;
import io.flybird.util.container.CollectionUtil;
import io.flybird.util.math.MathHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SmoothedFontRenderer {
    private static final HashMap<String, AliasFontInfo> aliases = new HashMap<>();
    private static final HashMap<String, Integer> lifetime = new HashMap<>();
    private static Font fontFamily = new Font("微软雅黑", Font.PLAIN, 8);

    public static void render(String string, int x, int y, int color, int size, FontAlignment alignment) {
        if (string == null || string.length() == 0) {
            return;
        }
        Font font = fontFamily.deriveFont((float) size);
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        FontMetrics fm = img.getGraphics().getFontMetrics(font);
        int width = (int) fm.getStringBounds(string, img.getGraphics()).getWidth();
        int height = (int) fm.getStringBounds(string, img.getGraphics()).getHeight();
        if (width == 0 || height == 0) {
            return;
        }

        String id = "%s_%d".formatted(string, size);
        AliasFontInfo f = aliases.get(id);
        if (f == null) {
            f = createFontImage(string, size);
            aliases.put(f.genKey(), f);

        }
        lifetime.put(f.genKey(), 30);

        int charPos_scr = 1;
        switch (alignment) {
            case LEFT -> charPos_scr = x;
            case MIDDLE -> charPos_scr = (int) (x - f.w / 2f);
            case RIGHT -> charPos_scr = x - f.w;
        }

        f.tex.bind();
        int posFix = (f.h - f.size) / 2;
        VertexArrayBuilder builder = new VertexArrayBuilder(4);
        builder.begin();
        builder.color(color);
        ShapeRenderer.drawRectUV(builder, charPos_scr, charPos_scr + f.w, y - posFix, y + f.h * 1.05f - posFix, 0, 0, 1, 0, 1);
        builder.end();
        VertexArrayUploader.uploadPointer(builder);
    }

    public static void renderShadow(String s, int x, int y, int color, int size, FontAlignment alignment) {
        render(s, (int) (x + 0.125 * size), (int) (y + 0.125 * size), (int) MathHelper.clamp(color - 0xbbbbbb, 0xFFFFFF, 0), size, alignment);
        render(s, x, y, color, size, alignment);
    }

    static AliasFontInfo createFontImage(String str, int size) {
        size *= 2;
        try {
            Font font = fontFamily.deriveFont((float) size);

            BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            FontMetrics fm = img.getGraphics().getFontMetrics(font);
            int width = (int) fm.getStringBounds(str, img.getGraphics()).getWidth();
            int height = (int) fm.getStringBounds(str, img.getGraphics()).getHeight();

            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
            g.setFont(font);
            g.drawString(str, 0, size);
            Texture2D tex = new Texture2D(false, false);//a dedicated texture in size.
            tex.generateTexture();
            tex.load(img);

            return new AliasFontInfo(str, size / 2, width / 2, height / 2, tex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void update() {
        List<String> removeList = new ArrayList<>();
        CollectionUtil.iterateMap(lifetime, (key, item) -> {
            if (item <= 0) {
                removeList.add(key);
                aliases.get(key).tex().destroy();
            } else {
                lifetime.put(key, item - 1);
            }
        });
        for (String s : removeList) {
            lifetime.remove(s);
            aliases.remove(s);
        }
    }

    public static int getCacheSize() {
        return aliases.size();
    }

    public static Font getFontFamily() {
        return fontFamily;
    }

    public static void setFontFamily(Font fontFamily) {
        SmoothedFontRenderer.fontFamily = fontFamily;
    }

    public record AliasFontInfo(String s, int size, int w, int h, Texture2D tex) {
        String genKey() {
            return "%s_%d".formatted(s, size);
        }
    }
}

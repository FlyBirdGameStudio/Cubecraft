package io.flybird.cubecraft.client.gui;


import io.flybird.starfish3d.render.GLUtil;
import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.draw.VertexArrayUploader;
import io.flybird.starfish3d.render.textures.Texture2D;
import io.flybird.util.math.MathHelper;

public class FontRenderer {
    public static Texture2D[] textures = new Texture2D[256];

    public static void render(String s, int x, int y, int color, int size, FontAlignment alignment) {
        if(s==null){
            return;
        }
        GLUtil.enableBlend();
        char[] rawData = s.toCharArray();
        int contWidth = 0;
        for (char c : rawData) {
            int pageCode = (int) Math.floor(c / 256.0f);
            String s2 = Integer.toHexString(pageCode);
            if (c == ' ') {
                contWidth += size;
            } else if (s2.equals("0")) {
                contWidth += size / 2;
            } else {
                contWidth += size;
            }
        }
        int charPos_scr = 0;
        switch (alignment) {
            case LEFT -> charPos_scr = x;
            case MIDDLE -> charPos_scr = (int) (x - contWidth / 2.0f);
            case RIGHT -> charPos_scr = x - contWidth;
        }
        VertexArrayBuilder builder = new VertexArrayBuilder(1024);
        for (char c : rawData) {
            int pageCode = (int) Math.floor(c / 256.0f);
            int charPos_Page = c % 256;
            String s2 = Integer.toHexString(pageCode);
            int charPos_V = charPos_Page / 16;
            int charPos_H = charPos_Page % 16;
            if (c == 0x0020) {
                charPos_scr += size * 0.75;
            }
            else if (c == 0x000d) {
                charPos_scr = 0;
            }
            else {
                float x0 = charPos_scr, x1 = charPos_scr + size,
                        y0 = y, y1 = y + size,
                        u0 = charPos_H / 16.0f, u1 = charPos_H / 16f + 0.0625f,
                        v0 = charPos_V / 16.0f, v1 = charPos_V / 16f + 0.0625f;
                textures[pageCode].bind();
                builder.begin();
                builder.color(color);
                ShapeRenderer.drawRectUV(builder,x0, x1, y0, y1, 0, 0, u0, u1, v0, v1);
                builder.end();
                VertexArrayUploader.uploadPointer(builder);
                textures[pageCode].unbind();
                if (s2.equals("0")) {
                    charPos_scr += size / 2;
                } else {
                    charPos_scr += size;
                }
            }
        }
    }

    public static void renderShadow(String s, int x, int y, int color, int size, FontAlignment alignment) {
        render(s, (int) (x + 0.125 * size), (int) (y + 0.125 * size), (int) MathHelper.clamp(color - 0xbbbbbb, 0xFFFFFF, 0), size, alignment);
        render(s, x, y, color, size, alignment);
    }


}

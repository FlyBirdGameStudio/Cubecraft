package io.flybird.cubecraft.client.gui;


import io.flybird.starfish3d.render.GLUtil;
import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.draw.VertexArrayUploader;
import io.flybird.starfish3d.render.textures.Texture2D;
import io.flybird.util.math.MathHelper;

import java.util.List;

public class FontRenderer {
    public static final List<Character> CONTROL_CHAR = List.of(new Character[]{
            0x000d,//return
            0x000a//new line
    });


    public static Texture2D[] textures = new Texture2D[256];

    public static void render(String s, int x, int y, int color, int size, FontAlignment alignment) {
        if (s == null) {
            return;
        }
        GLUtil.enableBlend();
        char[] rawData = s.toCharArray();
        int contWidth = getStringRenderWidth(rawData, size);

        int charPos_scr = 1;
        switch (alignment) {
            case LEFT -> charPos_scr = x;
            case MIDDLE -> charPos_scr = (int) (x - contWidth / 2f);
            case RIGHT -> charPos_scr = x - contWidth;
        }
        VertexArrayBuilder builder = new VertexArrayBuilder(s.length() * 4);
        for (char c : rawData) {
            int pageCode = (int) Math.floor(c / 256.0f);
            String s2 = Integer.toHexString(pageCode);

            //draw tile
            if (!CONTROL_CHAR.contains(c)&& c != 0x0020) {
                int charPos_Page = c % 256;
                int charPos_V = charPos_Page / 16;
                int charPos_H = charPos_Page % 16;
                float x0 = charPos_scr, x1 = charPos_scr + size,
                        y0 = y, y1 = y + size,
                        u0 = charPos_H / 16.0f, u1 = charPos_H / 16f + 0.0625f,
                        v0 = charPos_V / 16.0f, v1 = charPos_V / 16f + 0.0625f;
                textures[pageCode].bind();
                builder.begin();
                builder.color(color);
                ShapeRenderer.drawRectUV(builder, x0, x1, y0, y1, 0, 0, u0, u1, v0, v1);
                builder.end();
                VertexArrayUploader.uploadPointer(builder);
                textures[pageCode].unbind();
            }

            //add draw offset
            if (!CONTROL_CHAR.contains(c)) {
                if (c == '\u0020') {
                    charPos_scr += size / 2;
                } else if (s2.equals("0")) {
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

    public static int getStringRenderWidth(char[] rawData, int size) {
        int contWidth = 0;
        for (char c : rawData) {
            int pageCode = (int) Math.floor(c / 256.0f);
            String s2 = Integer.toHexString(pageCode);
            if (!CONTROL_CHAR.contains(c)) {
                if (c == '\u0020') {
                    contWidth += size / 2;
                } else if (s2.equals("0")) {
                    contWidth += size / 2;
                } else {
                    contWidth += size;
                }
            }
        }

        return contWidth;
    }


}

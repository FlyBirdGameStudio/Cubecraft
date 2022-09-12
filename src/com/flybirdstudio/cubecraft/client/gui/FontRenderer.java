package com.flybirdstudio.cubecraft.client.gui;


import com.flybirdstudio.cubecraft.registery.Registery;
import com.flybirdstudio.starfish3d.render.GLUtil;
import com.flybirdstudio.starfish3d.render.ShapeRenderer;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;
import com.flybirdstudio.starfish3d.render.textures.Texture2D;
import com.flybirdstudio.starfish3d.render.textures.TextureLoadingConfig;
import com.flybirdstudio.util.math.MathHelper;
import com.flybirdstudio.util.task.TaskProgressUpdateListener;

public class FontRenderer {
   // public static Texture2DArray fontTexture = new Texture2DArray(false, false, 256, 256, 256);
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
                ShapeRenderer.setColor(color);
                ShapeRenderer.drawRectUV(x0, x1, y0, y1, 0, 0, u0, u1, v0, v1);
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

    public static void loadTextures(TaskProgressUpdateListener listener) {
        TextureLoadingConfig[] configs=new TextureLoadingConfig[256];
        for (int i = 0; i < 256; i++) {
            if (i >= 241 && i <= 248 || i >= 216 && i <= 239 || i == 8||i==0xf0) {
                continue;
            }
            String s2 = Integer.toHexString(i);
            if (s2.length() == 1) {
                s2 = "0" + Integer.toHexString(i);
            }
            configs[i]=new TextureLoadingConfig("/resource/textures/font/unicode_page_" + s2 + ".png",false,false,0);
        }
        textures= Registery.getTextureManager().loadBatch(configs,1,listener,10,90);
    }
}
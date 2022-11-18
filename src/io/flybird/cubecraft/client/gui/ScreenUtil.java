package io.flybird.cubecraft.client.gui;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.resources.ResourceManager;
import io.flybird.starfish3d.platform.Display;
import io.flybird.starfish3d.render.GLUtil;
import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.draw.VertexArrayUploader;
import io.flybird.starfish3d.render.textures.Texture2D;
import io.flybird.starfish3d.render.textures.TextureStateManager;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;

public class ScreenUtil {

    private static ArrayList<Popup> popupList=new ArrayList<>();

    public static void initBGRenderer(){
        Registry.getTextureManager().createTexture2D(ResourceManager.instance.getResource("/resource/cubecraft/texture/ui/bg.png"),false,false);
        Registry.getTextureManager().createTexture2D(ResourceManager.instance.getResource("/resource/cubecraft/texture/ui/controls/popup.png"),false,false);
        Registry.getTextureManager().createTexture2D(ResourceManager.instance.getResource("/resource/cubecraft/texture/font/unicode_page_00.png"),false,false);
    }

    public static void renderPictureBackground(){
        int scale= GameSetting.instance.getValueAsInt("client.render.gui.scale",2);
        Registry.getTextureManager().getTexture2DContainer().bind("/resource/cubecraft/texture/ui/bg.png");
        ShapeRenderer.begin();
        ShapeRenderer.drawRectUV(0, Display.getWidth()/ scale,0,Display.getHeight()/scale,-1,-1,0,1,0,1);
        ShapeRenderer.end();
        Registry.getTextureManager().getTexture2DContainer().unbind("/resource/cubecraft/texture/ui/bg.png");
    }

    public static void renderMask(){
        int scale= GameSetting.instance.getValueAsInt("client.render.gui.scale",2);
        ShapeRenderer.setColor(0,0,0,127);
        ShapeRenderer.drawRect(0,Display.getWidth()/ scale,0,Display.getHeight()/scale,-1,-1);
    }

    public static void createPopup(String title, String subTitle, int time, int type){
        popupList.add(new Popup("",title,subTitle,time,type));
    }

    public static void tickPopup(){
        Iterator<Popup> p= popupList.iterator();
        while (p.hasNext()){
            Popup pop=p.next();
            pop.tick();
            if(pop.remaining<=0){
                p.remove();
            }
        }
    }

    public static void renderPopup(DisplayScreenInfo info, float interpolationTime){
        Registry.getTextureManager().getTexture2DContainer().bind("/resource/cubecraft/texture/ui/controls/popup.png");
        int yPop=0;
        for (Popup p: popupList){
            GL11.glPushMatrix();
            GL11.glTranslated(info.scrWidth()-200-16+p.getPos(interpolationTime),yPop,0);
            p.render(info);
            GL11.glPopMatrix();
            yPop+=50;
        }
    }

    public static void drawFontASCII(String s, int x, int y, int color, int size, FontAlignment alignment){
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
        Registry.getTextureManager().getTexture2DContainer().bind("/resource/cubecraft/texture/font/unicode_page_00.png");
        for (char c : rawData) {
            VertexArrayBuilder builder=new VertexArrayBuilder(4);
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
                builder.begin();
                builder.color(color);
                ShapeRenderer.drawRectUV(builder,x0, x1, y0, y1, 0, 0,u0,u1,v0,v1);
                builder.end();
                VertexArrayUploader.uploadPointer(builder);
                charPos_scr += size*0.5f;
            }
        }
    }

    public static void renderTileBackground() {
        //todo:add minecraft themed tile background rendering
    }

    public static void renderPictureBackgroundBlur() {
        int scale= GameSetting.instance.getValueAsInt("client.render.gui.scale",2);
        Texture2D tex= Registry.getTextureManager().getTexture2DContainer().get("/resource/cubecraft/texture/ui/bg.png");
        TextureStateManager.setTextureBlur(tex,true,3);
        tex.bind();
        ShapeRenderer.begin();
        ShapeRenderer.drawRectUV(0, Display.getWidth()/ scale,0,Display.getHeight()/scale,-1,-1,0,1,0,1);
        ShapeRenderer.end();
        TextureStateManager.setTextureBlur(tex,false,0);
        Registry.getTextureManager().getTexture2DContainer().unbind("/resource/cubecraft/texture/ui/bg.png");
    }
}

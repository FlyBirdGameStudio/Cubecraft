package com.flybirdstudio.cubecraft.client.gui.screen;

import com.flybirdstudio.cubecraft.client.Cubecraft;
import com.flybirdstudio.cubecraft.client.gui.DisplayScreenInfo;
import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.client.gui.FontAlignment;
import com.flybirdstudio.cubecraft.client.gui.component.Component;
import com.flybirdstudio.cubecraft.client.gui.component.Popup;
import com.flybirdstudio.cubecraft.client.resources.ResourceManager;
import com.flybirdstudio.cubecraft.registery.Registery;
import com.flybirdstudio.starfish3d.platform.Display;
import com.flybirdstudio.starfish3d.platform.input.Keyboard;
import com.flybirdstudio.starfish3d.platform.input.Mouse;
import com.flybirdstudio.starfish3d.platform.input.InputHandler;
import com.flybirdstudio.starfish3d.platform.input.KeyboardCallback;
import com.flybirdstudio.starfish3d.platform.input.MouseCallBack;
import com.flybirdstudio.starfish3d.render.GLUtil;
import com.flybirdstudio.starfish3d.render.ShapeRenderer;
import com.flybirdstudio.starfish3d.render.textures.Texture2D;
import com.flybirdstudio.util.container.CollectionUtil;
import org.lwjgl.opengl.GL11;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class Screen {
    private Cubecraft platform;
    protected HashMap<String,Component> components=new HashMap<>();

    //init
    public Screen(){}

    /**<h3>init a screen here,runs when screen initialize.</h3>
     * This method need to overwrite.
     * you could setup every thing here.
     */
    public void init(){}

    public void init(Cubecraft cubeCraft) {
        this.platform = cubeCraft;
        this.init();
        InputHandler.registerGlobalKeyboardCallback("cubecraft:scr_callback_default",this.getKeyboardCallback());
        InputHandler.registerGlobalMouseCallback("cubecraft:scr_callback_default",this.getMouseCallback());
        InputHandler.registerGlobalKeyboardCallback("cubecraft:scr_callback_base",new KeyboardCallback(){
            @Override
            public void onKeyEventPressed() {
                if(Keyboard.getEventKey()==Keyboard.KEY_F9){
                    createPopup("reloading...","reloading...",40,Popup.INFO);
                    ResourceManager.instance.reload(cubeCraft);
                    Screen.this.init();
                    createPopup("reload success","fully reloaded.",40,Popup.SUCCESS);
                }
            }
        });
        InputHandler.registerGlobalMouseCallback("cubecraft:scr_callback_base",new MouseCallBack(){
            @Override
            public void onButtonClicked(int eventButton) {
                if(eventButton==0){
                    int scale=GameSetting.instance.getValueAsInt("client.gui.scale",2);
                    CollectionUtil.iterateMap(Screen.this.components, (key, item) -> item.onClicked(Mouse.getX()/ scale,(-Mouse.getY()+Display.getHeight())/scale));
                }
            }
        });
    }

    public void render(DisplayScreenInfo info,float interpolationTime) {
        CollectionUtil.iterateMap(this.components, (key, item) -> item.render());
        renderPopup(info,interpolationTime);
    }

    public void tick() {
        CollectionUtil.iterateMap(this.components, (key, item)-> {
            int scale=GameSetting.instance.getValueAsInt("client.gui.scale",2);
            item.resize(Display.getWidth()/ scale,Display.getHeight()/scale);
            item.tick(Mouse.getX()/ scale,(-Mouse.getY()+Display.getHeight())/scale);
        });
        tickPopup();
    }

    public boolean isInGameGUI(){
        return false;
    }

    public void destroy(){

    }

    public MouseCallBack getMouseCallback(){
        return new MouseCallBack(){};
    }

    public KeyboardCallback getKeyboardCallback(){
        return new KeyboardCallback() {};
    }

    public Screen getParentScreen() {
        return null;
    }

    protected void addComponent(Component component) {
        this.components.put(component.toString(),component);
    }

    public Cubecraft getPlatform() {
        return platform;
    }


    //fast render
    private static Texture2D bg;
    public static void initBGRenderer(){
        Registery.getTextureManager().create2DTexture("/resource/textures/gui/bg.png",false,false);
        Registery.getTextureManager().create2DTexture("/resource/textures/gui/controls/popup.png",false,false);
        Registery.getTextureManager().create2DTexture("/resource/textures/font/unicode_page_00.png",false,false);
    }

    public static void renderPictureBackground(){
        int scale=GameSetting.instance.getValueAsInt("client.gui.scale",2);
        Registery.getTextureManager().bind2dTexture("/resource/textures/gui/bg.png");
        ShapeRenderer.begin();
        ShapeRenderer.drawRectUV(0, Display.getWidth()/ scale,0,Display.getHeight()/scale,-1,-1,0,1,0,1);
        ShapeRenderer.end();
        Registery.getTextureManager().unBind2dTexture("/resource/textures/gui/bg.png");
    }

    public static void renderMask(){
        int scale= GameSetting.instance.getValueAsInt("client.gui.scale",2);
        ShapeRenderer.setColor(0,0,0,127);
        ShapeRenderer.drawRect(0,Display.getWidth()/ scale,0,Display.getHeight()/scale,-1,-1);
    }

    private static ArrayList<Popup> popupList=new ArrayList<>();

    public static void createPopup(String title,String subTitle,int time,int type){
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

    public static void renderPopup(DisplayScreenInfo info,float interpolationTime){
        Registery.getTextureManager().bind2dTexture("/resource/textures/gui/controls/popup.png");
        int yPop=0;
        for (Popup p:popupList){
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
                Registery.getTextureManager().bind2dTexture("/resource/textures/font/unicode_page_00.png");
                ShapeRenderer.setColor(color);
                ShapeRenderer.drawRectUV(x0, x1, y0, y1, 0, 0,u0,u1,v0,v1);
                Registery.getTextureManager().unBind2dTexture("/resource/textures/font/unicode_page_00.png");
                charPos_scr += size*0.5f;
            }
        }
    }
}
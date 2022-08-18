package com.sunrisestudio.cubecraft.client.gui.screen;

import com.sunrisestudio.cubecraft.client.CubeCraft;
import com.sunrisestudio.cubecraft.client.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.GameSetting;
import com.sunrisestudio.cubecraft.client.gui.component.Component;
import com.sunrisestudio.cubecraft.client.gui.component.Popup;
import com.sunrisestudio.cubecraft.client.resources.ResourceManager;
import com.sunrisestudio.cubecraft.registery.Registry;
import com.sunrisestudio.grass3d.platform.Display;
import com.sunrisestudio.grass3d.platform.input.Keyboard;
import com.sunrisestudio.grass3d.platform.input.Mouse;
import com.sunrisestudio.grass3d.platform.input.InputHandler;
import com.sunrisestudio.grass3d.platform.input.KeyboardCallback;
import com.sunrisestudio.grass3d.platform.input.MouseCallBack;
import com.sunrisestudio.grass3d.render.ShapeRenderer;
import com.sunrisestudio.grass3d.render.textures.Texture2D;
import com.sunrisestudio.util.container.CollectionUtil;
import com.sunrisestudio.util.file.lang.Language;
import org.lwjgl.opengl.GL11;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class Screen {
    protected CubeCraft platform;
    protected HashMap<String,Component> components=new HashMap<>();

    //init
    public Screen(){}

    /**<h3>init a screen here,runs when screen initialize.</h3>
     * This method need to overwrite.
     * you could setup every thing here.
     */
    public void init(){}

    /**
     * system call-init
     * @param cubeCraft
     */
    public void init(CubeCraft cubeCraft) {
        this.platform = cubeCraft;
        this.init();
        InputHandler.registerGlobalKeyboardCallback("cubecraft:scr_callback_default",this.getKeyboardCallback());
        InputHandler.registerGlobalMouseCallback("cubecraft:scr_callback_default",this.getMouseCallback());
        InputHandler.registerGlobalKeyboardCallback("cubecraft:scr_callback_base",new KeyboardCallback(){
            @Override
            public void onKeyEventPressed() {
                if(Keyboard.getEventKey()==Keyboard.KEY_F9){
                    createPopup("reloading...","reloading current UI...",40,Popup.INFO);
                    Language.selectedLanguage.clear();
                    Language.selectedLanguage.attachTranslationFile(
                            ResourceManager.instance.getResource("/resource/text/language/zh_cn.lang",
                                    "/resource/text/language/zh_cn.lang")
                    );
                    Screen.this.init();
                    createPopup("reload success","UI fully reloaded.",40,Popup.SUCCESS);
                }
            }
        });
        InputHandler.registerGlobalMouseCallback("cubecraft:scr_callback_base",new MouseCallBack(){
            @Override
            public void onLeftClick() {
                int scale=GameSetting.instance.getValueAsInt("client.gui.scale",2);
                CollectionUtil.iterateMap(Screen.this.components, (key, item) -> item.onClicked(Mouse.getX()/ scale,(-Mouse.getY()+Display.getHeight())/scale));
            }
        });
    }

    /**
     * <h3>this method will invoke every frame in client rendering.</h3>
     * You can do anything here,but if you put any {@link Component} here than you have to call super.render();
     * @param info display info
     */
    public void render(DisplayScreenInfo info,float interpolationTime) {
        CollectionUtil.iterateMap(this.components, (key, item) -> item.render());
        renderPopup(info,interpolationTime);
    }

    /**
     * <h3>this method invoke within every tick of client. </h3>
     * You can update everything here,but if you put any {@link Component} here than you have to call super.tick();
     */
    public void tick() {
        CollectionUtil.iterateMap(this.components, (key, item)-> {
            int scale=GameSetting.instance.getValueAsInt("client.gui.scale",2);
            item.resize(Display.getWidth()/ scale,Display.getHeight()/scale);
            item.tick(Mouse.getX()/ scale,(-Mouse.getY()+Display.getHeight())/scale);
        });
        tickPopup();
    }

    /**
     * <h3>this boolean declared whether level render will work or not during screen render.</h3>
     * if true:level renderer will work and output
     * if false:level renderer will stop rendering to lower GPU usage
     */
    public boolean isInGameGUI(){
        return false;
    }

    /**
     * <h3>screen destroy function.</h3>
     * trigger when screen destroyed.
     * you have to call super.destroy() when overwriting this method.
     */
    public void destroy(){

    }


    /**
     * <h3>get a mouse callback for this screen. </h3>
     * This method need to overwrite.
     * @return callback
     */
    public MouseCallBack getMouseCallback(){
        return new MouseCallBack();
    }

    /**
     * <h3>get a keyboard callback for this screen.</h3>
     * This method need to overwrite.
     * @return callback
     */
    public KeyboardCallback getKeyboardCallback(){
        return new KeyboardCallback();
    }

    /**
     * overriding this returning to set parent screen for this.
     * @return screen
     */
    public Screen getParentScreen() {
        return null;
    }

    /**
     * offer a component.
     * @param component
     */
    protected void addComponent(Component component) {
        this.components.put(component.toString(),component);
    }


    private static Texture2D bg;
    public static void initBGRenderer(){
        Registry.getTextureManager().create2DTexture("/resource/textures/gui/bg.png",false,false);
        Registry.getTextureManager().create2DTexture("/resource/textures/gui/controls/popup.png",false,false);
    }

    public static void renderPictureBackground(){
        int scale=GameSetting.instance.getValueAsInt("client.gui.scale",2);
        Registry.getTextureManager().bind2dTexture("/resource/textures/gui/bg.png");
        ShapeRenderer.begin();
        ShapeRenderer.drawRectUV(0, Display.getWidth()/ scale,0,Display.getHeight()/scale,-1,-1,0,1,0,1);
        ShapeRenderer.end();
        Registry.getTextureManager().unBind2dTexture("/resource/textures/gui/bg.png");
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
        Registry.getTextureManager().bind2dTexture("/resource/textures/gui/controls/popup.png");
        int yPop=0;
        for (Popup p:popupList){
            GL11.glPushMatrix();
            GL11.glTranslated(info.scrWidth()-200-16+p.getPos(interpolationTime),yPop,0);
            p.render(info);
            GL11.glPopMatrix();
            yPop+=50;
        }
    }
}

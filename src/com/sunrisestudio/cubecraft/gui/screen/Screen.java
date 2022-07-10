package com.sunrisestudio.cubecraft.gui.screen;

import com.sunrisestudio.cubecraft.CubeCraft;
import com.sunrisestudio.cubecraft.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.GameSetting;
import com.sunrisestudio.cubecraft.gui.component.Component;
import com.sunrisestudio.util.input.InputCallbackHandler;
import com.sunrisestudio.util.input.KeyboardCallback;
import com.sunrisestudio.util.input.MouseCallBack;
import org.lwjglx.input.Mouse;
import org.lwjglx.opengl.Display;

import java.util.ArrayList;

public abstract class Screen {
    protected CubeCraft platform;
    protected ArrayList<Component> components=new ArrayList<>();

    //init
    public Screen(){
        this.init();
    }

    /**<h3>init a screen here,runs when screen initialize.</h3>
     * This method need to overwrite.
     * you could setup every thing here.
     */
    public void init(){}

    public void init(CubeCraft cubeCraft) {
        this.platform = cubeCraft;
        this.init();
        InputCallbackHandler.registerGlobalKeyboardCallback("cubecraft:scr_callback_default",this.getKeyboardCallback());
        InputCallbackHandler.registerGlobalMouseCallback("cubecraft:scr_callback_default",this.getMouseCallback());
    }

    /**
     * <h3>this method will invoke every frame in client rendering.</h3>
     * You can do anything here,but if you put any {@link Component} here than you have to call super.render();
     * @param info display info
     */
    public void render(DisplayScreenInfo info) {
        for (Component p: this.components) {
            p.render();
        }
    }

    /**
     * <h3>this method invoke within every tick of client. </h3>
     * You can update everything here,but if you put any {@link Component} here than you have to call super.tick();
     */
    public void tick() {
        for (Component p:this.components){
            p.resize(Display.getWidth()/ GameSetting.instance.GUIScale,Display.getHeight()/GameSetting.instance.GUIScale);
            p.tick(Mouse.getX()/ GameSetting.instance.GUIScale,(-Mouse.getY()+Display.getHeight())/GameSetting.instance.GUIScale);
        }
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
        InputCallbackHandler.releaseGlobalKeyboardCallback("cubecraft:scr_callback_default");
        InputCallbackHandler.releaseGlobalMouseCallback("cubecraft:scr_callback_default");
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
     * <h3>get a mouse callback for this screen.</h3>
     * This method need to overwrite.
     * @return callback
     */
    public KeyboardCallback getKeyboardCallback(){
        return new KeyboardCallback();
    }

    public Screen getParentScreen() {
        return null;
    }
}

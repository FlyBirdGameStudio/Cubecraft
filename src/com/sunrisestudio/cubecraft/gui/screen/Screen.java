package com.sunrisestudio.cubecraft.gui.screen;

import com.sunrisestudio.cubecraft.CubeCraft;
import com.sunrisestudio.cubecraft.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.GameSetting;
import com.sunrisestudio.cubecraft.gui.component.Button;
import com.sunrisestudio.cubecraft.gui.component.Component;
import com.sunrisestudio.grass3d.platform.Display;
import com.sunrisestudio.grass3d.platform.Mouse;
import com.sunrisestudio.grass3d.platform.input.InputHandler;
import com.sunrisestudio.grass3d.platform.input.KeyboardCallback;
import com.sunrisestudio.grass3d.platform.input.MouseCallBack;
import com.sunrisestudio.util.container.CollectionUtil;


import java.util.ArrayList;
import java.util.HashMap;

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
    }

    /**
     * <h3>this method will invoke every frame in client rendering.</h3>
     * You can do anything here,but if you put any {@link Component} here than you have to call super.render();
     * @param info display info
     */
    public void render(DisplayScreenInfo info) {
        CollectionUtil.iterateMap(this.components, (key, item) -> item.render());
    }

    /**
     * <h3>this method invoke within every tick of client. </h3>
     * You can update everything here,but if you put any {@link Component} here than you have to call super.tick();
     */
    public void tick() {
        CollectionUtil.iterateMap(this.components, (key, item)-> {
            item.resize(Display.getWidth()/ GameSetting.instance.GUIScale,Display.getHeight()/GameSetting.instance.GUIScale);
            item.tick(Mouse.getX()/ GameSetting.instance.GUIScale,(-Mouse.getY()+Display.getHeight())/GameSetting.instance.GUIScale);
        });
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
        InputHandler.releaseGlobalKeyboardCallback("cubecraft:scr_callback_default");
        InputHandler.releaseGlobalMouseCallback("cubecraft:scr_callback_default");
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
}

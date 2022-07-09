package com.sunrisestudio.cubecraft.gui.screen;

import com.sunrisestudio.cubecraft.CubeCraft;
import com.sunrisestudio.cubecraft.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.GameSetting;
import com.sunrisestudio.cubecraft.gui.component.Component;
import org.lwjglx.input.Mouse;
import org.lwjglx.opengl.Display;

import java.util.ArrayList;

public abstract class Screen {
    protected CubeCraft cubeCraft;
    public int width;
    public int height;
    protected ArrayList<Component> components=new ArrayList<>();

    //init
    public Screen(){
        this.init();
    }

    public abstract void init();

    public void init(CubeCraft cubeCraft) {
        this.cubeCraft = cubeCraft;
        this.init();
    }

    //update
    public void render(DisplayScreenInfo info) {
        for (Component p: this.components) {
            p.render();
        }
    }

    public void tick() {
        for (Component p:this.components){
            p.resize(Display.getWidth()/ GameSetting.instance.GUIScale,Display.getHeight()/GameSetting.instance.GUIScale);
            p.tick(Mouse.getX()/ GameSetting.instance.GUIScale,(-Mouse.getY()+Display.getHeight())/GameSetting.instance.GUIScale);
        }
    }

    //attribute
    public boolean isInGameGUI(){
        return false;
    }
}

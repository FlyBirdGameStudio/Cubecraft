package com.sunrisestudio.cubecraft.gui.screen;



public class SettingScreen extends Screen {
    public Screen parent;

    public SettingScreen(Screen parent){
        this.parent=parent;
    }

    @Override
    public Screen getParentScreen() {
        return parent;
    }
}

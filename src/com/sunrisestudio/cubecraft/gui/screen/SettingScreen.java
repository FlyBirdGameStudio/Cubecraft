package com.sunrisestudio.cubecraft.gui.screen;

import com.sunrisestudio.cubecraft.CubeCraft;

public class SettingScreen extends Screen {
    public Screen parent;

    public SettingScreen(Screen parent){
        this.parent=parent;
    }

    @Override
    public void init() {
        this.components.clear();
    }

    @Override
    public Screen getParentScreen() {
        return parent;
    }
}

package com.sunrisestudio.cubecraft.client.gui.screen;


import com.sunrisestudio.cubecraft.client.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.client.gui.ScreenLoader;
import com.sunrisestudio.cubecraft.client.gui.component.Button;
import com.sunrisestudio.grass3d.platform.Display;
import com.sunrisestudio.grass3d.render.GLUtil;
import com.sunrisestudio.grass3d.render.ShapeRenderer;

public class SettingScreen extends Screen {
    public Screen parent;

    public SettingScreen(Screen parent){
        this.parent=parent;
    }

    @Override
    public Screen getParentScreen() {
        return parent;
    }

    @Override
    public boolean isInGameGUI() {
        return parent.isInGameGUI();
    }

    @Override
    public void init() {
        this.components.clear();
        this.components.putAll(ScreenLoader.load("/resource/gui/settingscreen.json"));

        ((Button)this.components.get("button_back")).setListener(() -> {
            this.platform.setScreen(getParentScreen());
        });

        ((Button)this.components.get("button_confirm")).setListener(() -> {
            this.platform.setScreen(getParentScreen());
            //todo:flush option
        });
    }

    @Override
    public void render(DisplayScreenInfo info) {
        GLUtil.enableBlend();
        super.render(info);
        if(!this.isInGameGUI()){
            Screen.renderPictureBackground();
        }else{
            renderMask();
        }
        GLUtil.disableBlend();
    }
}

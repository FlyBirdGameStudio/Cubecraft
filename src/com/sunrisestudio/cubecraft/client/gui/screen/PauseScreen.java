package com.sunrisestudio.cubecraft.client.gui.screen;


import com.sunrisestudio.cubecraft.client.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.client.gui.ScreenLoader;
import com.sunrisestudio.cubecraft.client.gui.component.Button;
import com.sunrisestudio.cubecraft.client.gui.screen.options.SettingScreen;
import com.sunrisestudio.grass3d.render.GLUtil;

public class PauseScreen extends Screen {

    @Override
    public void init() {
        this.components.clear();
        this.components.putAll(ScreenLoader.load("/resource/ui/pausescreen.json"));

        ((Button)this.components.get("button_back")).setListener(() -> {
            this.platform.setScreen(new HUDScreen());
        });

        ((Button)this.components.get("button_option")).setListener(() -> {
            this.platform.setScreen(new SettingScreen(this));
        });

        ((Button)this.components.get("button_save_and_quit")).setListener(() -> {
            this.platform.setScreen(new TitleScreen());
        });
    }


    @Override
    public boolean isInGameGUI() {
        return true;
    }

    @Override
    public Screen getParentScreen() {
        return new HUDScreen();
    }

    @Override
    public void render(DisplayScreenInfo info, float interpolationTime) {
        GLUtil.enableBlend();
        super.render(info, interpolationTime);
        renderMask();
        GLUtil.disableBlend();
    }
}

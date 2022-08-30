package com.flybirdstudio.cubecraft.client.gui.screen;


import com.flybirdstudio.cubecraft.client.gui.DisplayScreenInfo;
import com.flybirdstudio.cubecraft.client.gui.ScreenLoader;
import com.flybirdstudio.cubecraft.client.gui.component.Button;
import com.flybirdstudio.cubecraft.client.gui.screen.options.SettingScreen;
import com.flybirdstudio.starfish3d.render.GLUtil;

public class PauseScreen extends Screen {

    @Override
    public void init() {
        this.components.clear();
        this.components.putAll(ScreenLoader.load("/resource/ui/pausescreen.json"));

        ((Button)this.components.get("button_back")).setListener(() -> {
            this.getPlatform().setScreen(new HUDScreen());
        });

        ((Button)this.components.get("button_option")).setListener(() -> {
            this.getPlatform().setScreen(new SettingScreen(this));
        });

        ((Button)this.components.get("button_save_and_quit")).setListener(() -> {
            this.getPlatform().setScreen(new TitleScreen());
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
        renderMask();
        super.render(info, interpolationTime);
        GLUtil.disableBlend();
    }
}

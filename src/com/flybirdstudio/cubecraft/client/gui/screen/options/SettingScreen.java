package com.flybirdstudio.cubecraft.client.gui.screen.options;


import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.client.gui.DisplayScreenInfo;
import com.flybirdstudio.cubecraft.client.gui.ScreenLoader;
import com.flybirdstudio.cubecraft.client.gui.ScreenUtil;
import com.flybirdstudio.cubecraft.client.gui.component.Button;
import com.flybirdstudio.cubecraft.client.gui.screen.Screen;

public class SettingScreen extends Screen {
    public Screen parent;

    public SettingScreen(Screen parent){
        super("cubecraft:setting",ScreenType.IN_GAME);
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
        ((Button)this.components.get("button_confirm")).setListener(() -> {
            this.getPlatform().setScreen(getParentScreen());
            GameSetting.instance.flush();
            GameSetting.instance.save();
        });
    }

    @Override
    public void render(DisplayScreenInfo info,float interpolationTime) {
        if(!this.isInGameGUI()){
            ScreenUtil.renderPictureBackground();
        }else{
            ScreenUtil.renderMask();
        }
        super.render(info,interpolationTime);
    }
}

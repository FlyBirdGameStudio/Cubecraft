package com.sunrisestudio.cubecraft.gui.screen;


import com.sunrisestudio.cubecraft.gui.component.Button;
import com.sunrisestudio.cubecraft.gui.layout.Border;
import com.sunrisestudio.cubecraft.gui.layout.OriginLayout;

import java.util.ArrayList;
import java.util.List;

public class PauseScreen extends Screen {
    @Override
    public void init() {
        this.components.clear();
        {
            Button singlePlayerButton=new Button(0xFFFFFF,0xFFFFFF,"回到游戏");
            singlePlayerButton.setLayout(new OriginLayout(0,-10,300,30, OriginLayout.Origin.MIDDLE_MIDDLE,0));
            singlePlayerButton.setBorder(new Border(0,0,4,4));
            singlePlayerButton.setListener(() -> {
                this.platform.setScreen(new HUDScreen());
            });
            this.components.add(singlePlayerButton);
        }//single player button
        {
            Button multiPlayerButton=new Button(0xFFFFFF,0xFFFFFF,"设置");
            multiPlayerButton.setLayout(new OriginLayout(0,20,300,30, OriginLayout.Origin.MIDDLE_MIDDLE,0));
            multiPlayerButton.setBorder(new Border(0,0,4,4));
            multiPlayerButton.setListener(() -> {
                this.platform.setScreen(new SettingScreen(this));
            });
            this.components.add(multiPlayerButton);
        }//multi player button
        {
            Button settingButton=new Button(0xFFFFFF,0xFFFFFF,"保存并退出");
            settingButton.setLayout(new OriginLayout(0,50,300,30, OriginLayout.Origin.MIDDLE_MIDDLE,0));
            settingButton.setBorder(new Border(0,0,4,4));
            settingButton.setListener(() -> {
                this.platform.setScreen(new TitleScreen());
            });
            this.components.add(settingButton);
        }//setting button
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean isInGameGUI() {
        return true;
    }

    @Override
    public Screen getParentScreen() {
        return new HUDScreen();
    }
}

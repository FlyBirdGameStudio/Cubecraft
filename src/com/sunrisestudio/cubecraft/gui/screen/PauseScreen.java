package com.sunrisestudio.cubecraft.gui.screen;


import com.sunrisestudio.cubecraft.gui.FontAlignment;
import com.sunrisestudio.cubecraft.gui.component.Button;
import com.sunrisestudio.cubecraft.gui.component.Label;
import com.sunrisestudio.cubecraft.gui.layout.Border;
import com.sunrisestudio.cubecraft.gui.layout.OriginLayout;
import com.sunrisestudio.util.lang.Language;

import java.util.ArrayList;
import java.util.List;

public class PauseScreen extends Screen {
    @Override
    public void init() {
        this.components.clear();
        {
            Label singlePlayerButton=new Label( Language.get("pausescreen.title"),12,0xFFFFFF,0, FontAlignment.MIDDLE);
            singlePlayerButton.setLayout(new OriginLayout(0,-45,0,12, OriginLayout.Origin.MIDDLE_MIDDLE,0));
            singlePlayerButton.setBorder(new Border(0,0,4,4));
            this.addComponent(singlePlayerButton);
        }//back
        {
            Button singlePlayerButton=new Button(0xFFFFFF,0xFFFFFF, Language.get("pausescreen.button.backtogame"));
            singlePlayerButton.setLayout(new OriginLayout(0,-20,300,30, OriginLayout.Origin.MIDDLE_MIDDLE,0));
            singlePlayerButton.setBorder(new Border(0,0,4,4));
            singlePlayerButton.setListener(() -> {
                this.platform.setScreen(new HUDScreen());
            });
            this.addComponent(singlePlayerButton);
        }//single player button
        {
            Button multiPlayerButton=new Button(0xFFFFFF,0xFFFFFF,Language.get("pausescreen.button.option"));
            multiPlayerButton.setLayout(new OriginLayout(0,10,300,30, OriginLayout.Origin.MIDDLE_MIDDLE,0));
            multiPlayerButton.setBorder(new Border(0,0,4,4));
            multiPlayerButton.setListener(() -> {
                this.platform.setScreen(new SettingScreen(this));
            });
            this.addComponent(multiPlayerButton);
        }//multi player button
        {
            Button settingButton=new Button(0xFFFFFF,0xFFFFFF,Language.get("mainscreen.button.setting"));
            settingButton.setLayout(new OriginLayout(-75,50,150,30, OriginLayout.Origin.MIDDLE_MIDDLE,0));
            settingButton.setBorder(new Border(0,4,4,4));
            settingButton.setListener(() -> {
                this.platform.setScreen(new SettingScreen(this));
            });
            this.addComponent(settingButton);
        }//setting button
        {
            Button settingButton=new Button(0xFFFFFF,0xFFFFFF,Language.get("pausescreen.button.saveandquit"));
            settingButton.setLayout(new OriginLayout(0,50,300,30, OriginLayout.Origin.MIDDLE_MIDDLE,0));
            settingButton.setBorder(new Border(0,0,4,4));
            settingButton.setListener(() -> {
                this.platform.setScreen(new TitleScreen());
            });
            this.addComponent(settingButton);
        }//setting button
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

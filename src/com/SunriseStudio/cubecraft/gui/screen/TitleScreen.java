package com.SunriseStudio.cubecraft.gui.screen;

import com.SunriseStudio.cubecraft.gui.DisplayScreenInfo;
import com.SunriseStudio.cubecraft.GameSetting;
import com.SunriseStudio.cubecraft.gui.LoadingScreenTask;
import com.SunriseStudio.cubecraft.gui.component.Button;
import com.SunriseStudio.cubecraft.resources.textures.Texture2D;
import com.SunriseStudio.cubecraft.util.grass3D.render.ShapeRenderer;
import com.SunriseStudio.cubecraft.gui.layout.Border;
import com.SunriseStudio.cubecraft.gui.layout.OriginLayout;
import org.lwjglx.opengl.Display;

public class TitleScreen extends Screen {
    private Texture2D bg;
    private Texture2D logoTex;

    @Override
    public void init() {
        bg=new Texture2D(false,false);
        bg.generateTexture();
        bg.load("/resource/textures/gui/bg.png");
        logoTex=new Texture2D(false,false);
        logoTex.generateTexture();
        logoTex.load("/resource/textures/gui/logo.png");

        this.components.clear();
        {
            Button singlePlayerButton=new Button(0xFFFFFF,0xFFFFFF,"单人游戏");
            singlePlayerButton.setLayout(new OriginLayout(0,-10,300,30, OriginLayout.Origin.MIDDLE_MIDDLE,0));
            singlePlayerButton.setBorder(new Border(0,0,4,4));
            singlePlayerButton.setListener(() -> {
            this.cubeCraft.setScreen(new LoadingScreen(new TitleScreen(), new HUDScreen(), new LoadingScreenTask() {
                @Override
                public void run() {
                    for (int i = 0; i < 101; i++) {
                        this.setText("加入世界中...");
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            this.cancel();
                        }
                        this.setStatus(i);
                    }
                }
            }));//actullyJoinTheGame
            });
            this.components.add(singlePlayerButton);
        }//single player button
        {
            Button multiPlayerButton=new Button(0xFFFFFF,0xFFFFFF,"多人游戏");
            multiPlayerButton.setLayout(new OriginLayout(0,20,300,30, OriginLayout.Origin.MIDDLE_MIDDLE,0));
            multiPlayerButton.setBorder(new Border(0,0,4,4));
            multiPlayerButton.setListener(() -> {
                this.cubeCraft.setScreen(new HUDScreen());//actullyJoinTheGame
            });
            this.components.add(multiPlayerButton);
        }//multi player button
        {
            Button settingButton=new Button(0xFFFFFF,0xFFFFFF,"设置");
            settingButton.setLayout(new OriginLayout(-75,50,150,30, OriginLayout.Origin.MIDDLE_MIDDLE,0));
            settingButton.setBorder(new Border(0,4,4,4));
            settingButton.setListener(() -> {
                this.cubeCraft.setScreen(new SettingScreen());//actullyJoinTheGame
            });
            this.components.add(settingButton);
        }//setting button
        {
            Button quitButton=new Button(0xFFFFFF,0xFFFFFF,"退出");
            quitButton.setLayout(new OriginLayout(75,50,150,30, OriginLayout.Origin.MIDDLE_MIDDLE,0));
            quitButton.setBorder(new Border(4,4,4,4));
            quitButton.setListener(() -> {
                this.cubeCraft.stop();
            });
            this.components.add(quitButton);
        }//exit button
    }

    @Override
    public void render(DisplayScreenInfo info) {
        this.bg.bind();
        ShapeRenderer.begin();
        ShapeRenderer.drawRectUV(0,Display.getWidth()/ GameSetting.instance.GUIScale,0,Display.getHeight()/GameSetting.instance.GUIScale,-1,-1,0,1,0,1);
        ShapeRenderer.end();
        this.bg.unbind();
        ShapeRenderer.setColor(0xFFFFFF);
        this.logoTex.bind();
        ShapeRenderer.drawRectUV(info.centerX()-160, info.centerX()+160,
                info.centerY()-100,info.centerY()-20,
                1,1,0,1,0,1);
        this.logoTex.unbind();
        super.render(info);
    }
}

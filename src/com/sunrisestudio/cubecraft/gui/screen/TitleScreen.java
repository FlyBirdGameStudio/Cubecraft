package com.sunrisestudio.cubecraft.gui.screen;

import com.google.gson.Gson;
import com.sunrisestudio.cubecraft.gui.*;
import com.sunrisestudio.cubecraft.GameSetting;
import com.sunrisestudio.cubecraft.gui.component.Button;
import com.sunrisestudio.cubecraft.gui.component.Label;
import com.sunrisestudio.cubecraft.resources.ResourcePacks;
import com.sunrisestudio.grass3d.platform.Display;
import com.sunrisestudio.grass3d.render.GLUtil;
import com.sunrisestudio.grass3d.render.textures.Texture2D;
import com.sunrisestudio.grass3d.render.ShapeRenderer;
import com.sunrisestudio.cubecraft.gui.layout.Border;
import com.sunrisestudio.cubecraft.gui.layout.OriginLayout;
import com.sunrisestudio.util.lang.Language;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;


public class TitleScreen extends Screen {
    private Texture2D bg;
    private Texture2D logoTex;
    private String splashText;

    @Override
    public void init() {
        bg=new Texture2D(false,false);
        bg.generateTexture();
        bg.load("/resource/textures/gui/bg.png");
        logoTex=new Texture2D(false,false);
        logoTex.generateTexture();
        logoTex.load("/resource/textures/gui/logo.png");
        this.components.clear();
        this.components.putAll(ScreenLoader.load("/resource/gui/titlescreen.json"));

        ((Button) components.get("button_singleplayer")).setListener(() -> {
            this.platform.setScreen(new LoadingScreen(new TitleScreen(), new HUDScreen(), new LoadingScreenTask() {
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
        ((Button) components.get("button_multiplayer")).setListener(() -> {
            this.platform.setScreen(new HUDScreen());//actullyJoinTheGame
        });
        ((Button) components.get("button_option")).setListener(() -> {
            this.platform.setScreen(new SettingScreen(this));
        });
        ((Button) components.get("button_quit")).setListener(() -> TitleScreen.this.platform.stop());
        {
            String[] splash;
            try {
                splash=new Gson().fromJson(new String(ResourcePacks.instance.getResource("/resource/text/splash.json","/resource/text/splash.json").readAllBytes(), StandardCharsets.UTF_8),String[].class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this. splashText=splash[new Random().nextInt(splash.length)];
        }//splash
    }

    @Override
    public void render(DisplayScreenInfo info) {
        this.bg.bind();
        ShapeRenderer.begin();
        ShapeRenderer.drawRectUV(0, Display.getWidth()/ GameSetting.instance.GUIScale,0,Display.getHeight()/GameSetting.instance.GUIScale,-1,-1,0,1,0,1);
        ShapeRenderer.end();
        this.bg.unbind();
        ShapeRenderer.setColor(0xFFFFFF);
        this.logoTex.bind();
        ShapeRenderer.drawRectUV(info.centerX()-160, info.centerX()+160,
                info.centerY()-120,info.centerY()-40,
                1,1,0,1,0,1);
        this.logoTex.unbind();
        super.render(info);
        GLUtil.disableDepthTest();
        GL11.glPushMatrix();
        GL11.glTranslatef(info.centerX()+95,info.centerY()-72,0);
        double sin=Math.sin(System.currentTimeMillis()/300d)*0.1+1.1;
        GL11.glScaled(sin,sin,sin);
        GL11.glRotatef(-30,0,0,1);
        FontRenderer.renderShadow(splashText,0,0,0xefff00,12,FontAlignment.MIDDLE);
        GL11.glPopMatrix();
        GLUtil.enableDepthTest();
    }
}

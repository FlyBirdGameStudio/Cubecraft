package com.sunrisestudio.cubecraft.client.gui.screen;

import com.google.gson.Gson;
import com.sunrisestudio.cubecraft.client.CubeCraft;
import com.sunrisestudio.cubecraft.client.gui.*;
import com.sunrisestudio.cubecraft.GameSetting;
import com.sunrisestudio.cubecraft.client.gui.component.Button;
import com.sunrisestudio.cubecraft.client.gui.component.Label;
import com.sunrisestudio.cubecraft.client.gui.component.Popup;
import com.sunrisestudio.cubecraft.client.resources.ResourceManager;
import com.sunrisestudio.cubecraft.extansion.ModManager;
import com.sunrisestudio.cubecraft.extansion.PluginManager;
import com.sunrisestudio.cubecraft.world.LevelInfo;
import com.sunrisestudio.cubecraft.world.World;
import com.sunrisestudio.grass3d.platform.Display;
import com.sunrisestudio.grass3d.platform.Mouse;
import com.sunrisestudio.grass3d.render.GLUtil;
import com.sunrisestudio.grass3d.render.textures.Texture2D;
import com.sunrisestudio.grass3d.render.ShapeRenderer;
import com.sunrisestudio.util.file.lang.Language;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;


public class TitleScreen extends Screen {

    private Texture2D logoTex;
    private String splashText;

    @Override
    public void init() {
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
            this.platform.joinWorld(new World(new LevelInfo("NULL", "NULL", 0, new Date(), false, "NULL", null)));
        });
        ((Button) components.get("button_multiplayer")).setListener(() -> {
            Screen.createPopup("?","function not implmented!",60, Popup.WARNING);
        });
        ((Button) components.get("button_option")).setListener(() -> {
            this.platform.setScreen(new SettingScreen(this));
        });
        ((Button) components.get("button_quit")).setListener(() -> TitleScreen.this.platform.stop());
        {
            String[] splash;
            try {
                splash=new Gson().fromJson(new String(ResourceManager.instance.getResource("/resource/text/splash.json","/resource/text/splash.json").readAllBytes(), StandardCharsets.UTF_8),String[].class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this. splashText=splash[new Random().nextInt(splash.length)];
        }//splash


        ((Label) components.get("version_string")).text= Language.getInstance().getFormattedMessage(
                "titlescreen.version", CubeCraft.VERSION, ModManager.getLoadedMods().size(), PluginManager.getLoadedPlugins().size()
        );

        ((Label) components.get("copyright_string")).text= "©SunriseStudio,Do not Copy!";


    }

    @Override
    public void render(DisplayScreenInfo info,float interpolationTime) {
        renderPictureBackground();
        ShapeRenderer.setColor(0xFFFFFF);
        super.render(info,interpolationTime);
        GL11.glPushMatrix();
        GL11.glTranslatef(info.centerX()+95,info.centerY()-72,0);
        double sin=Math.sin(System.currentTimeMillis()/300d)*0.1+1.1;
        GL11.glScaled(sin,sin,sin);
        GL11.glRotatef(-30,0,0,1);
        FontRenderer.renderShadow(splashText,0,0,0xefff00,12,FontAlignment.MIDDLE);
        GL11.glPopMatrix();

        this.logoTex.bind();
        ShapeRenderer.setColor(0xffffff);
        ShapeRenderer.drawRectUV(info.centerX()-160, info.centerX()+160,
                info.centerY()-120,info.centerY()-40,
                -0.001,-0.001,0,1,0,1);
        this.logoTex.unbind();
    }
}

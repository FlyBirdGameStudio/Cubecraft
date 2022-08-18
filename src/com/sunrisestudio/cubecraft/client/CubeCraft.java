/*
    Copyright (c) [Year] [name of copyright holder]
    [Software Name] is licensed under Mulan PSL v2.
    You can use this software according to the terms and conditions of the Mulan PSL v2.
    You may obtain a copy of Mulan PSL v2 at:
    http://license.coscl.org.cn/MulanPSL2
    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
    EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
    MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
    See the Mulan PSL v2 for more details.
 */
package com.sunrisestudio.cubecraft.client;

import com.sunrisestudio.cubecraft.GameSetting;
import com.sunrisestudio.cubecraft.registery.Registry;
import com.sunrisestudio.cubecraft.client.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.client.gui.FontRenderer;
import com.sunrisestudio.cubecraft.client.gui.screen.*;
import com.sunrisestudio.cubecraft.client.render.renderer.LevelRenderer;
import com.sunrisestudio.cubecraft.client.resources.ResourceManager;
import com.sunrisestudio.cubecraft.extansion.ExtansionRunningTarget;
import com.sunrisestudio.cubecraft.extansion.ModManager;
import com.sunrisestudio.cubecraft.extansion.PlatformClient;
import com.sunrisestudio.cubecraft.net.ClientIO;
import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.cubecraft.world.World;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;
import com.sunrisestudio.grass3d.audio.Audio;
import com.sunrisestudio.grass3d.platform.Display;
import com.sunrisestudio.grass3d.platform.input.Keyboard;
import com.sunrisestudio.grass3d.platform.input.InputHandler;
import com.sunrisestudio.grass3d.platform.input.KeyboardCallback;
import com.sunrisestudio.grass3d.render.GLUtil;
import com.sunrisestudio.grass3d.render.textures.Texture2D;
import com.sunrisestudio.util.LoadTask;
import com.sunrisestudio.util.LogHandler;
import com.sunrisestudio.util.LoopTickingApplication;
import com.sunrisestudio.util.file.lang.Language;
import com.sunrisestudio.util.timer.Timer;
import org.lwjgl.opengl.GL11;

import java.io.File;

//todo:add server net support
//todo:add resource support
//todo:add inventory support


public class CubeCraft extends LoopTickingApplication {
    public static final String VERSION = "alpha-0.2.5";

    private DisplayScreenInfo screenInfo;
    public LevelRenderer levelRenderer;
    private Screen screen;

    private final ClientIO clientIO = new ClientIO();
    public IWorldAccess clientWorld = null;
    public Player player = new Player(null);
    public final PlayerController controller=new PlayerController(this.player);


    //world

    public void joinWorld(World world){
        this.clientWorld=world;
        player.setPos(400,20,0);
        this.levelRenderer = new LevelRenderer(this.clientWorld, this.player);
        this.clientWorld.addEntity(this.player);
    }

    public void leaveWorld(){
        this.clientWorld=null;
        this.player=null;
    }


    //init and stop

    @Override
    public void init() {
        GameSetting.instance.read();
        this.timer = new Timer(20);
        this.logHandler = LogHandler.create("main", "client");
        this.initDisplay();
        this.loadGameContent();

        this.setScreen(new TitleScreen());
        InputHandler.registerGlobalKeyboardCallback("cubecraft:main", new KeyboardCallback() {
            @Override
            public void onKeyEventPressed() {
                if (Keyboard.getEventKey() == Keyboard.KEY_F11) {
                    Display.setFullscreen(!Display.isFullscreen());
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                    if (CubeCraft.this.screen.getParentScreen() != null) {
                        CubeCraft.this.setScreen(CubeCraft.this.screen.getParentScreen());
                    }
                }
            }
        });
    }

    private void loadGameContent() {
        LogoLoadingScreen logoLoadingScreen = new LogoLoadingScreen();
        this.setScreen(logoLoadingScreen);
        Screen.initBGRenderer();



        //register blocks
        logHandler.info("constructing...");
        Registry.registerVanillaContent();
        File[] mods = new File(Start.getGamePath() + "/mods").listFiles();
        if(new File(Start.getGamePath() + "/mods").exists()) {
            if (mods != null) {
                new LoadTask(mods.length, 0.2f, 0.5f, count ->
                        ModManager.loadMod(mods[count].getAbsolutePath(), null,
                                CubeCraft.this.getPlatformClient(),
                                ExtansionRunningTarget.CLIENT)
                );
            } else {
                throw new RuntimeException("null or invalid mod path");
            }
        }


        this.logHandler.info("loading resources...");
        this.logHandler.checkGLError("pre_font_load");
        for (int i = 0; i < 256; i++) {
            if (i >= 241 && i <= 248 || i >= 216 && i <= 239 || i == 8||i==0xf0) {
                continue;
            }
            FontRenderer.textures[i] = new Texture2D(false, false);
            FontRenderer.textures[i].generateTexture();
            String s2 = Integer.toHexString(i);
            if (s2.length() == 1) {
                s2 = "0" + Integer.toHexString(i);
            }

            FontRenderer.textures[i].load("/resource/textures/font/unicode_page_" + s2 + ".png");
            logoLoadingScreen.updateProgress(0.3f + (i / 255f) * 0.7f);
        }
        this.logHandler.checkGLError("post_font_load");

        Language.create(Language.LanguageType.ZH_CN);
        Language.selectInstance(Language.LanguageType.ZH_CN).attachTranslationFile(ResourceManager.instance.getResource(
                "/resource/text/language/zh_cn.lang",
                "/resource/text/language/zh_cn.lang"
        ));
    }

    @Override
    public void stop() {
        Display.destroy();
        logHandler.info("game stopped...");
        LogHandler.allSave();
        Audio.destroy();
        System.exit(0);
    }


    //loop

    @Override
    public void on1sec() {
        if(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()>512*1024*1024)
            System.gc();
    }

    @Override
    public void shortTick() {
        this.render();
    }

    @Override
    public void longTick() {
        InputHandler.tick();
        this.screen.tick();
        if(this.clientWorld!=null){
            this.clientWorld.tick();
        }
    }


    //render
    public void setScreen(Screen screen) {
        if (screen != null) {
            screen.destroy();
        }
        this.screen = screen;
        if (screen != null) {
            screen.init(this);
        }
    }

    private void render(){
        this.logHandler.checkGLError("pre_render");
        this.screenInfo = getDisplaySize();
        Display.clear();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);//16640

        if (this.screen.isInGameGUI()) {
            this.logHandler.checkGLError("pre_world_render");
            levelRenderer.render(this.timer.interpolatedTime);
            this.logHandler.checkGLError("post_world_render");
        }
        if (Display.isCloseRequested()) {
            this.stop();
        }

        GLUtil.setupOrthogonalCamera(0, 0, Display.getWidth(), Display.getHeight(), screenInfo.scrWidth(), screenInfo.scrHeight());

        if (this.screen != null) {
            GLUtil.enableDepthTest();
            GLUtil.enableBlend();
            this.logHandler.checkGLError("pre_screen_render");
            this.screenInfo = this.getDisplaySize();
            this.screen.render(this.screenInfo,this.timer.interpolatedTime);
            this.logHandler.checkGLError("post_screen_render");
            GLUtil.disableBlend();
        }
        Display.sync(120);
        Display.update();
        this.logHandler.checkGLError("post_render");
    }

    private DisplayScreenInfo getDisplaySize() {
        int scale = GameSetting.instance.getValueAsInt("client.gui.scale",2);
        return new DisplayScreenInfo(
                scale,
                Math.max(Display.getWidth() / scale, 1),
                Math.max(Display.getHeight() / scale, 1),
                Math.max(Display.getWidth() / scale, 1) / 2,
                Math.max(Display.getHeight() / scale, 1) / 2
        );
    }

    private void initDisplay() {
        Display.create();
        Display.setFXAA(GameSetting.instance.FXAA);
        Display.setTitle((String) Start.getStartGameArguments().getValue("title", "CubeCraft-" + VERSION));
        Display.setIcon(ResourceManager.instance.getResource("/resource/textures/gui/icon.png",null));
        Display.setResizable(true);
        Display.setVsyncEnable(false);
        Audio.create();
    }

    public PlatformClient getPlatformClient() {
        return new PlatformClient(this,
                this.clientIO,
                this.screen,
                this.screenInfo,
                this.clientWorld,
                this.player);
    }

}
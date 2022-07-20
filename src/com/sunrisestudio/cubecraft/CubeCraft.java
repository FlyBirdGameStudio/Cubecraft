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
package com.sunrisestudio.cubecraft;

import com.sunrisestudio.cubecraft.extansion.ExtansionRunningTarget;
import com.sunrisestudio.cubecraft.extansion.ModManager;
import com.sunrisestudio.cubecraft.extansion.PlatformClient;
import com.sunrisestudio.cubecraft.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.gui.FontRenderer;
import com.sunrisestudio.cubecraft.gui.screen.LogoLoadingScreen;
import com.sunrisestudio.cubecraft.gui.screen.Screen;
import com.sunrisestudio.cubecraft.gui.screen.TitleScreen;
import com.sunrisestudio.cubecraft.net.ClientIO;
import com.sunrisestudio.cubecraft.render.renderer.WorldRenderer;
import com.sunrisestudio.cubecraft.resources.ResourcePacks;
import com.sunrisestudio.cubecraft.world.*;
import com.sunrisestudio.cubecraft.world.entity._Player;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;
import com.sunrisestudio.grass3d.audio.Audio;
import com.sunrisestudio.grass3d.platform.Display;
import com.sunrisestudio.grass3d.platform.Keyboard;
import com.sunrisestudio.grass3d.platform.input.InputHandler;
import com.sunrisestudio.grass3d.platform.input.KeyboardCallback;
import com.sunrisestudio.grass3d.render.GLUtil;
import com.sunrisestudio.grass3d.render.textures.Texture2D;
import com.sunrisestudio.util.LoadTask;
import com.sunrisestudio.util.LogHandler;
import com.sunrisestudio.util.LoopTickingApplication;
import com.sunrisestudio.util.lang.Language;
import com.sunrisestudio.util.timer.Timer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.io.File;
import java.util.Date;

//todo:add server net support;
//todo:add resource support;
//todo:add inventory support

public class CubeCraft extends LoopTickingApplication {
    private DisplayScreenInfo screenInfo;

    public static final String VERSION = "alpha-0.1.9";
    public _Level world;

    public WorldRenderer worldRenderer;
    private Screen screen;
    public _Player _player;

    private final ClientIO clientIO = new ClientIO();

    private final IWorldAccess clientWorld = new World(new LevelInfo("NULL", "NULL", 0, new Date(), false, "NULL", null));
    public final Player player = new Player(null);
    public final PlayerController controller=new PlayerController(this.player);


    @Override
    public void init() {
        this.timer = new Timer(20);
        this.logHandler = LogHandler.create("main", "client");

        this.initDisplay();
        this.loadGameContent();

        this.setScreen(new TitleScreen());

        this.world = new _Level(0);
        this._player = new _Player(this.world);
        _player.setPos(1024, -20, 1024);
        player.setPos(0,20,0);
        this.worldRenderer = new WorldRenderer(this.world, this._player);

        this.clientWorld.addEntity(this.player);

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

    @Override
    public void stop() {
        Display.destroy();
        logHandler.info("game stopped...");
        LogHandler.allSave();
        Audio.destroy();
        System.exit(0);
    }

    @Override
    public void on1sec() {
        if (Runtime.getRuntime().freeMemory() < 1024 * 1024 * 200) {
            MemoryUtil.memSet(0,0,0);
            new Thread(System::gc).start();
        }
    }

    @Override
    public void shortTick() {
        this.logHandler.checkGLError("pre_render");
        this.screenInfo = getDisplaySize();
        Display.clear();
        GL11.glClear(16640);

        if (this.screen.isInGameGUI()) {
            this.logHandler.checkGLError("pre_world_render");
            worldRenderer.render(this.timer.interpolatedTime, this.screenInfo.scrWidth(), this.screenInfo.scrHeight());
            this.logHandler.checkGLError("post_world_render");
        }
        if (Display.isCloseRequested()) {
            this.stop();
        }

        GLUtil.setupOrthogonalCamera(0, 0, Display.getWidth(), Display.getHeight(), screenInfo.scrWidth(), screenInfo.scrHeight());

        if (this.screen != null) {
            GLUtil.enableDepthTest();
            this.logHandler.checkGLError("pre_screen_render");
            this.screenInfo = this.getDisplaySize();
            this.screen.render(this.screenInfo);
            this.logHandler.checkGLError("post_screen_render");
        }
        Display.sync(120);
        Display.update();
        this.logHandler.checkGLError("post_render");
    }

    @Override
    public void longTick() {
        InputHandler.tick();
        this.screen.tick();
        this.world.tick();
        this.clientWorld.tick();
        this.controller.tick();
    }

    public void setScreen(Screen screen) {
        if (screen != null) {
            screen.destroy();
        }
        this.screen = screen;
        if (screen != null) {
            screen.init(this);
        }
    }

    private void loadGameContent() {
        LogoLoadingScreen logoLoadingScreen = new LogoLoadingScreen();
        this.setScreen(logoLoadingScreen);

        //register blocks
        logHandler.info("constructing...");
        Registry.getBlockBehaviorMap().registerGetter(BlockBehaviors.class);
        Registry.getBlockMap().registerGetter(Blocks.class);
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


        this.logHandler.info("loading textures...");
        this.logHandler.checkGLError("pre_font_load");
        for (int i = 0; i < 256; i++) {
            if (i >= 241 && i <= 248 || i >= 216 && i <= 239 || i == 8) {
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
        Language.selectInstance(Language.LanguageType.ZH_CN).attachTranslationFile(ResourcePacks.instance.getResource(
                "/resource/text/language/zh_cn.lang",
                "/resource/text/language/zh_cn.lang"
        ));
    }

    private DisplayScreenInfo getDisplaySize() {
        int scale = GameSetting.instance.GUIScale;
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
        Display.setResizable(true);

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

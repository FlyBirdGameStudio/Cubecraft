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

import com.sunrisestudio.cubecraft.extansion.PlatformBase;
import com.sunrisestudio.cubecraft.extansion.PlatformClient;
import com.sunrisestudio.cubecraft.gui.screen.Screen;
import com.sunrisestudio.cubecraft.net.ClientIO;
import com.sunrisestudio.cubecraft.render.renderer.ContentWorldRenderer;
import com.sunrisestudio.cubecraft.gui.FontRenderer;
import com.sunrisestudio.cubecraft.resources.ResourcePacks;
import com.sunrisestudio.cubecraft.world.entity.EntityMap;
import com.sunrisestudio.grass3d.render.GLUtil;
import com.sunrisestudio.util.input.InputCallbackHandler;
import com.sunrisestudio.util.input.KeyboardCallback;
import com.sunrisestudio.util.lang.Language;
import com.sunrisestudio.util.timer.Timer;

import com.sunrisestudio.cubecraft.world.World;
import com.sunrisestudio.cubecraft.world.block.registery.BlockMap;
import com.sunrisestudio.cubecraft.world.entity._Player;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;
import com.sunrisestudio.cubecraft.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.gui.screen.LogoLoadingScreen;
import com.sunrisestudio.cubecraft.gui.screen.TitleScreen;
import com.sunrisestudio.grass3d.render.textures.Texture2D;
import com.sunrisestudio.util.LogHandler;
import com.sunrisestudio.util.LoopTickingApplication;
import com.sunrisestudio.cubecraft.world.access.IWorldAccess;
import com.sunrisestudio.cubecraft.world._Level;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjglx.LWJGLException;
import org.lwjglx.input.Keyboard;
import org.lwjglx.input.Mouse;
import org.lwjglx.opengl.Display;
import org.lwjglx.opengl.DisplayMode;
import org.lwjglx.opengl.PixelFormat;


//todo:add server net support;
//todo:add resource support;
//todo:add inventory support

public class CubeCraft extends LoopTickingApplication {
    private DisplayScreenInfo screenInfo;
    private boolean fullscreen;

    public static final String VERSION = "alpha-0.1.5";
    public _Level world;

    public ContentWorldRenderer worldRenderer;
    private Screen screen;
    public _Player _player;

    private final ClientIO clientIO=new ClientIO();

    private final IWorldAccess clientWorld = new World();
    private final Player player = new Player(null);

    private final BlockMap blockMap=new BlockMap();
    private final EntityMap entityMap=new EntityMap();

    private long lastToggle =System.currentTimeMillis();

    @Override
    public void init() throws LWJGLException {
        this.timer=new Timer(20);
        this.logHandler = LogHandler.create("main", "client");

        this.initDisplay();
        this.initGameContent();

        this.setScreen(new TitleScreen());

        this.world = new _Level(0);
        this._player = new _Player(this.world);
        _player.setPos(1024,140, 1024);
        this.worldRenderer = new ContentWorldRenderer(this.world, this._player);

        this.clientWorld.getEntityAccess().addEntity(this.player);

        InputCallbackHandler.registerGlobalKeyboardCallback("cubecraft:main",new KeyboardCallback(){
            @Override
            public void onKeyEventPressed() {
                if(Keyboard.getEventKey()==Keyboard.KEY_F11){
                    try {
                        Display.setFullscreen(!Display.isFullscreen());
                    } catch (LWJGLException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(Keyboard.getEventKey()==Keyboard.KEY_ESCAPE){
                    if(CubeCraft.this.screen.getParentScreen()!=null){
                        CubeCraft.this.setScreen(CubeCraft.this.screen.getParentScreen());
                    }
                }
            }
        });
    }

    @Override
    public void stop() {
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
        logHandler.info("game stopped...");
        LogHandler.allSave();
        System.exit(0);
    }

    @Override
    public void on1sec() {
        if (Runtime.getRuntime().freeMemory() < 1024 * 1024 * 200){
            new Thread(System::gc).start();
        }
    }

    @Override
    public void shortTick() {
        this.logHandler.checkGLError("pre_render");
        float a=this.timer.interpolatedTime;

        this.screenInfo=getDisplaySize();
        GL11.glClear(16640);
        if (this.screen.isInGameGUI()) {
            this.logHandler.checkGLError("pre_world_render");
            worldRenderer.render(a,this.screenInfo.scrWidth(),this.screenInfo.scrHeight());
            this.logHandler.checkGLError("post_world_render");
        }

        if(Display.isCloseRequested()){
            this.stop();
        }

        GLUtil.setupOrthogonalCamera(0, 0, Display.getWidth(),Display.getHeight(),screenInfo.scrWidth(), screenInfo.scrHeight());

        if (this.screen != null) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            this.logHandler.checkGLError("pre_screen_render");
            this.screenInfo=this.getDisplaySize();
            this.screen.render(this.screenInfo);
            this.logHandler.checkGLError("post_screen_render");
        }
        Display.sync(120);
        Display.update();
        this.logHandler.checkGLError("post_render");
    }

    @Override
    public void longTick() {
        InputCallbackHandler.tick();
        this.screen.tick();
        this.world.tick();

        new Thread(this.clientWorld::tick).start();
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

    private void initGameContent(){
        LogoLoadingScreen logoLoadingScreen=new LogoLoadingScreen();
        this.setScreen(logoLoadingScreen);

        logHandler.info("registering blocks...");
        new Thread(() -> {
            BlockMap.getInstance().registerBlockBehavior();
            BlockMap.getInstance().registerBlock();
        }).start();
        while (BlockMap.getInstance().getStatus()<1){
            logoLoadingScreen.updateProgress(0.3f*BlockMap.getInstance().getStatus());
        }

        this.logHandler.info("loading textures...");
        this.logHandler.checkGLError("pre_font_load");
        for (int i=0;i<256;i++) {
            if(i >= 241 && i <= 248 || i >= 216 && i <= 239 || i == 8){
                continue;
            }
            FontRenderer.textures[i]=new Texture2D(false,false);
            FontRenderer.textures[i].generateTexture();
            String s2=Integer.toHexString(i);
            if(s2.length()==1){
                s2="0"+Integer.toHexString(i);
            }
            FontRenderer.textures[i].load("/resource/textures/font/unicode_page_" + s2+".png");
            logoLoadingScreen.updateProgress(0.3f+(i/255f)*0.7f);
        }
        this.logHandler.checkGLError("post_font_load");

        Language.create(Language.LanguageType.ZH_CN);
        Language.selectInstance(Language.LanguageType.ZH_CN).
                attachTranslationFile(ResourcePacks.instance.getResource(
                        "/resource/texts/language/zh_cn.lang",
                        "/resource/texts/language/zh_cn.lang"
                ));
    }

    private DisplayScreenInfo getDisplaySize() {
        int scale=GameSetting.instance.GUIScale;
        return new DisplayScreenInfo(
                scale,
                Math.max(Display.getWidth()/ scale,1),
                Math.max(Display.getHeight()/scale,1),
                Math.max(Display.getWidth()/scale,1)/2,
                Math.max(Display.getHeight()/scale,1)/2
        );
    }

    private void initDisplay() throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(
                Integer.parseInt((String) Start.getArgs("width", "1280")),
                Integer.parseInt((String) Start.getArgs("height", "720"))));
        Display.setTitle((String) Start.getArgs("title", "CubeCraft-" + VERSION));
        Display.setResizable(true);
        Display.create(new PixelFormat(),Boolean.getBoolean((String) Start.getArgs("fullScreen", "false")));
        Keyboard.create();
        Mouse.create();
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES,GameSetting.instance.FXAA);
        //.setIcon(ResourceUtil.getImageBufferFromStream(ResourcePacks.instance.getImage("/resources/textures/gui/logo2.png")));
        getDisplaySize();
    }

    public PlatformClient getPlatformClient(){
        return new PlatformClient(this,
                this.clientIO,
                this.screen,
                this.screenInfo,
                this.clientWorld,
                this.player,
                new PlatformBase(this.blockMap,this.entityMap));
    }
}

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
package com.flybirdstudio.cubecraft.client;

import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.Start;
import com.flybirdstudio.cubecraft.client.gui.component.Popup;
import com.flybirdstudio.cubecraft.registery.Registery;
import com.flybirdstudio.cubecraft.client.gui.DisplayScreenInfo;
import com.flybirdstudio.cubecraft.client.gui.screen.*;
import com.flybirdstudio.cubecraft.client.render.renderer.LevelRenderer;
import com.flybirdstudio.cubecraft.client.resources.ResourceManager;
import com.flybirdstudio.cubecraft.extansion.PlatformClient;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.entity.humanoid.Player;
import com.flybirdstudio.starfish3d.audio.Audio;
import com.flybirdstudio.starfish3d.platform.Display;
import com.flybirdstudio.starfish3d.platform.input.InputHandler;
import com.flybirdstudio.starfish3d.render.GLUtil;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayUploader;
import com.flybirdstudio.util.HTTPUtil;
import com.flybirdstudio.util.LogHandler;
import com.flybirdstudio.util.LoopTickingApplication;
import com.flybirdstudio.util.file.lang.Language;
import com.flybirdstudio.util.net.UDPSocket;
import com.flybirdstudio.util.net.UDPSocketThread;
import com.flybirdstudio.util.task.TaskProgressUpdateListener;
import com.flybirdstudio.util.timer.Timer;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

//todo:add server net support
//todo:add resource support
//todo:add inventory support


public class Cubecraft extends LoopTickingApplication implements TaskProgressUpdateListener {
    public static final String VERSION = "alpha-0.2.5";
    private DisplayScreenInfo screenInfo;
    public LevelRenderer levelRenderer;
    private Screen screen;
    private LogoLoadingScreen logoLoadingScreen = new LogoLoadingScreen();
    private ClientInputHandler clientInputHandler=new ClientInputHandler(this);
    public IWorld clientWorld = null;
    private Player player = new Player(null);
    public final PlayerController controller=new PlayerController(this.player);
    public final UDPSocket clientIO=new UDPSocket(Registery.getPacketEncoderMap(), Registery.getPacketDecoderMap(), "127.0.0.1",11451);

    //world
    public void joinWorld(IWorld world){
        this.clientWorld=world;
        player.setPos(0,2,0);
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
        Timer.startTiming();
        GameSetting.instance.read();
        this.timer = new Timer(20);
        this.logHandler = LogHandler.create("main", "client");
        this.initDisplay();
        InputHandler.registerGlobalKeyboardCallback("cubecraft:main",this.clientInputHandler);
        this.setScreen(logoLoadingScreen);
        Screen.initBGRenderer();
        logHandler.info("registering vanilla contents...");
        Registery.registerVanillaContent();
        this.logHandler.info("loading resources...");
        ResourceManager.instance.reload(this);
        Language.selectInstance(Language.LanguageType.valueOf((String) GameSetting.instance.getValue("client.language","ZH_CN")));
        this.setScreen(new TitleScreen());
        this.logHandler.info("starting client io thread...");
        //new Thread(new UDPSocketThread(this.clientIO,128.0f),"client_io").start();
        this.checkVersion();
        this.logHandler.info("client initialization done,in%dms".formatted(Timer.endTiming()));
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
        VertexArrayUploader.resetUploadCount();
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
        Display.sync(GameSetting.instance.getValueAsInt("client.render.maxFPS",60));
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

    public Screen getScreen() {
        return this.screen;
    }

    public void checkVersion(){
        this.logHandler.info("checking for updates...");
        new Thread(() -> {
            try {
                String request = HTTPUtil.get("http://api.sunrisestudio.top/version_check?product=cubecraft_client");
                if(request!=VERSION){
                    Screen.createPopup(
                            Language.get("versioncheck.exception.title"),
                            Language.getFormattedMessage("versioncheck.exception.subtitle",VERSION,request),
                            60, Popup.ERROR
                    );
                }else{
                    Screen.createPopup(
                            Language.get("versioncheck.latest"),
                            Language.getFormattedMessage("versioncheck.latest.subtitle",request),
                            60, Popup.ERROR
                    );
                }
            }catch (IOException e){
                Screen.createPopup(
                        Language.get("versioncheck.exception.title"),
                        Language.get("versioncheck.exception.subtitle"),
                        60, Popup.ERROR
                );
            }
        },"client_update_check").start();
    }


    //progress
    @Override
    public void onProgressChange(int prog) {
        this.logoLoadingScreen.updateProgress(prog/100f);
    }

    @Override
    public void onProgressStageChanged(String newStage) {
        this.logoLoadingScreen.setText(newStage);
    }

    @Override
    public void refreshScreen() {
        this.shortTick();
    }

    public Player getPlayer() {
        return player;
    }
}
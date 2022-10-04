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
import com.flybirdstudio.cubecraft.client.event.ClientInitializeEvent;
import com.flybirdstudio.cubecraft.client.event.ClientShutdownEvent;
import com.flybirdstudio.cubecraft.client.event.ScreenInitializeEvent;
import com.flybirdstudio.cubecraft.client.gui.DisplayScreenInfo;
import com.flybirdstudio.cubecraft.client.gui.ScreenLoader;
import com.flybirdstudio.cubecraft.client.gui.ScreenUtil;
import com.flybirdstudio.cubecraft.client.gui.screen.LogoLoadingScreen;
import com.flybirdstudio.cubecraft.client.gui.screen.Screen;
import com.flybirdstudio.cubecraft.client.render.renderer.LevelRenderer;
import com.flybirdstudio.cubecraft.client.resources.ResourceLoader;
import com.flybirdstudio.cubecraft.client.resources.ResourceManager;
import com.flybirdstudio.cubecraft.extansion.PlatformClient;
import com.flybirdstudio.cubecraft.registery.Registry;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.entity.humanoid.Player;
import com.flybirdstudio.starfish3d.audio.Audio;
import com.flybirdstudio.starfish3d.platform.Display;
import com.flybirdstudio.starfish3d.platform.input.InputHandler;
import com.flybirdstudio.starfish3d.render.GLUtil;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayUploader;
import com.flybirdstudio.util.LogHandler;
import com.flybirdstudio.util.LoopTickingApplication;
import com.flybirdstudio.util.container.StartArguments;
import com.flybirdstudio.util.event.EventBus;
import com.flybirdstudio.util.net.UDPSocket;
import com.flybirdstudio.util.task.TaskProgressUpdateListener;
import com.flybirdstudio.util.timer.Timer;
import org.lwjgl.opengl.GL11;

//todo:add server net support
//todo:add inventory support
//todo:add block model
//todo:serialize screen into xml
//todo:fix smooth light engine

public class Cubecraft extends LoopTickingApplication implements TaskProgressUpdateListener {


    private final EventBus clientEventBus = new EventBus();


    //display
    private DisplayScreenInfo screenInfo;
    public LevelRenderer levelRenderer;
    private Screen screen;
    private final LogoLoadingScreen logoLoadingScreen = new LogoLoadingScreen();
    private final ClientInputHandler clientInputHandler = new ClientInputHandler(this);
    public static final String VERSION = "alpha-0.2.5";

    private IWorld clientWorld = null;
    private Player player = new Player(null);
    public final PlayerController controller = new PlayerController(this.player);
    public final UDPSocket clientIO = new UDPSocket(Registry.getPacketEncoderMap(), Registry.getPacketDecoderMap(), "127.0.0.1", 11451);

    //world
    public void joinWorld(IWorld world) {
        this.clientWorld = world;
        player.setPos(0, 2, 0);
        this.levelRenderer = new LevelRenderer(this.getClientWorld(), this.player);
        this.getClientWorld().addEntity(this.player);
    }

    public void leaveWorld() {
        this.clientWorld = null;
        this.player = null;
    }

    public EventBus getClientEventBus() {
        return clientEventBus;
    }

    public IWorld getClientWorld() {
        return clientWorld;
    }

    public Player getPlayer() {
        return player;
    }

    public PlatformClient getPlatformClient() {
        return new PlatformClient(this,
                this.clientIO,
                this.screen,
                this.screenInfo,
                this.getClientWorld(),
                this.player);
    }


    //application
    @Override
    public void init() {

        Registry.setClient(this);
        Timer.startTiming();

        //init application

        this.timer = new Timer(20);
        this.logHandler = LogHandler.create("main", "client");
        this.initDisplay();

        InputHandler.registerGlobalKeyboardCallback("cubecraft:main", this.clientInputHandler);
        ScreenUtil.initBGRenderer();

        //load content

        this.clientEventBus.callEvent(new ClientInitializeEvent(this));
        this.setScreen(logoLoadingScreen);
        this.logoLoadingScreen.display();
        GameSetting.instance.read();
        Registry.registerVanillaContent();
        this.clientEventBus.registerEventListener(new ResourceLoader());
        ResourceManager.instance.reload(this);

        this.logoLoadingScreen.dispose();
        this.getClientEventBus().registerEventListener(new ScreenController());
        this.setScreen("/resource/ui/title_screen.xml");

        //check version
        new Thread(new VersionCheck(), "client_update_check").start();

        //finish loading
        this.logHandler.info("client initialization done,in%dms".formatted(Timer.endTiming()));
    }

    @Override
    public void on1sec() {
        if (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() > 512 * 1024 * 1024)
            System.gc();
        VertexArrayUploader.resetUploadCount();
    }

    @Override
    public void render() {
        this.logHandler.checkGLError("pre_render");
        this.screenInfo = getDisplaySize();
        Display.clear();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);//16640

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
            this.screen.render(this.screenInfo, this.timer.interpolatedTime);
            if (!(this.screen instanceof LogoLoadingScreen)) {
                this.logoLoadingScreen.render(screenInfo, this.timer.interpolatedTime);
            }
            this.logHandler.checkGLError("post_screen_render");
            GLUtil.disableBlend();
        }
        Display.sync(GameSetting.instance.getValueAsInt("client.render.maxFPS", 60));
        Display.update();
        this.logHandler.checkGLError("post_render");
    }

    @Override
    public void tick() {
        InputHandler.tick();
        this.screen.tick();
        if (!(this.screen instanceof LogoLoadingScreen)) {
            this.logoLoadingScreen.tick();
        }
        if (this.getClientWorld() != null) {
            this.getClientWorld().tick();
        }
    }

    @Override
    public void stop() {
        this.clientEventBus.callEvent(new ClientShutdownEvent(this));
        Display.destroy();
        logHandler.info("game stopped...");
        LogHandler.allSave();
        Audio.destroy();
        System.exit(0);
    }




    //render
    public void setScreen(Screen screen) {
        this.clientEventBus.callEvent(new ScreenInitializeEvent(this, screen));
        this.screen = screen;
        if (screen != null) {
            screen.init(this);
        }
    }

    public void setScreen(String uiPosition){
        this.setScreen(ScreenLoader.loadByExtName(uiPosition));
    }

    private DisplayScreenInfo getDisplaySize() {
        int scale = GameSetting.instance.getValueAsInt("client.gui.scale", 2);
        return new DisplayScreenInfo(
                scale,
                Math.max(Display.getWidth() / scale, 1),
                Math.max(Display.getHeight() / scale, 1),
                Math.max(Display.getWidth() / scale, 1) / 2,
                Math.max(Display.getHeight() / scale, 1) / 2
        );
    }

    private void initDisplay() {
        StartArguments arg=Start.getStartGameArguments();
        Display.create();
        Display.setFXAA(GameSetting.instance.FXAA);
        Display.setTitle(arg.getValueAsString("title", "Cubecraft-" + VERSION));
        Display.setIcon(ResourceManager.instance.getResource("/resource/textures/gui/icon.png", null));
        Display.setResizable(true);
        Display.setVsyncEnable(false);
        Display.setSize(arg.getValueAsInt("width",1280), arg.getValueAsInt("height",720));
        Audio.create();
    }

    public Screen getScreen() {
        return this.screen;
    }

    @Override
    public void refreshScreen() {
        this.render();
    }


    //progress
    @Override
    public void onProgressChange(int prog) {
        this.logoLoadingScreen.updateProgress(prog / 100f);
    }

    @Override
    public void onProgressStageChanged(String newStage) {
        this.logoLoadingScreen.setText(newStage);
    }

    public LogoLoadingScreen getLoadingScreen() {
        return this.logoLoadingScreen;
    }
}
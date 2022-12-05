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
package io.flybird.cubecraft.client;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.auth.Session;
import io.flybird.cubecraft.client.event.*;
import io.flybird.cubecraft.client.gui.*;
import io.flybird.cubecraft.client.gui.screen.*;
import io.flybird.cubecraft.client.render.renderer.LevelRenderer;
import io.flybird.cubecraft.extansion.ExtansionRunningTarget;
import io.flybird.cubecraft.extansion.ModManager;
import io.flybird.cubecraft.extansion.PlatformClient;
import io.flybird.cubecraft.internal.ClientInputHandler;
import io.flybird.cubecraft.internal.InternalContent;
import io.flybird.cubecraft.internal.ScreenController;
import io.flybird.cubecraft.internal.net.handler.ClientNetHandlerConnection;
import io.flybird.cubecraft.internal.net.handler.ClientNetHandlerPlaying;
import io.flybird.cubecraft.internal.net.packet.connect.PacketPlayerLeave;
import io.flybird.util.network.base.ClientNettyPipeline;
import io.flybird.cubecraft.internal.net.packet.connect.PacketPlayerJoinRequest;
import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.client.resources.ResourceLoader;
import io.flybird.cubecraft.client.resources.ResourceManager;
import io.flybird.cubecraft.server.CubecraftServer;
import io.flybird.cubecraft.world.*;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.starfish3d.audio.Audio;
import io.flybird.starfish3d.platform.*;
import io.flybird.starfish3d.render.GLUtil;
import io.flybird.util.logging.LogHandler;
import io.flybird.util.LoopTickingApplication;
import io.flybird.util.container.StartArguments;
import io.flybird.util.event.EventBus;
import io.flybird.util.task.TaskProgressUpdateListener;
import io.flybird.util.timer.Timer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.Date;

////////////////////////////////////////////////////////////////////
//                          _ooOoo_                               //
//                         o8888888o                              //
//                         88" . "88                              //
//                         (| ^_^ |)                              //
//                         O\  =  /O                              //
//                      ____/`---'\____                           //
//                    .'  \\|     |//  `.                         //
//                   /  \\|||  :  |||//  \                        //
//                  /  _||||| -:- |||||-  \                       //
//                  |   | \\\  -  /// |   |                       //
//                  | \_|  ''\---/''  |   |                       //
//                  \  .-\__  `-`  ___/-. /                       //
//                ___`. .'  /--.--\  `. . ___                     //
//              ."" '<  `.___\_<|>_/___.'  >'"".                  //
//            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
//            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
//      ========`-.____`-.___\_____/___.-`____.-'========         //
//                           `=---='                              //
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//            佛祖保佑       永不内存泄漏     永无BUG                  //
////////////////////////////////////////////////////////////////////


//todo:add server net support
//todo:add inventory support
//todo:fix smooth light engine
//todo:add json driven block register
//todo:merge code

//todo:add cache for event bus
//todo:add cache and sort for render compile service :D


public class Cubecraft extends LoopTickingApplication implements TaskProgressUpdateListener {
    public static final String VERSION = "0.2.2";

    private final Window window=new Window();

    private InetSocketAddress integratedServerLocation;
    private ClientNetHandlerPlaying handler=new ClientNetHandlerPlaying(this);
    private CubecraftServer server;
    private final EventBus clientEventBus = new EventBus();
    private final GameSetting setting = new GameSetting(ClientMain.getGamePath() + "/data/configs/settings.properties", "cubecraft client " + VERSION);
    private DisplayScreenInfo screenInfo;
    public LevelRenderer levelRenderer;
    private Screen screen;
    private final LogoLoadingScreen logoLoadingScreen = new LogoLoadingScreen();
    private final ClientInputHandler clientInputHandler = new ClientInputHandler(this);
    private IWorld clientWorld;
    private final Session session = new Session("CubeVlmu", "cubecraft:default");
    private Player player;
    public PlayerController controller;
    private final ClientNettyPipeline clientIO = new ClientNettyPipeline();
    private LevelInfo clientLevelInfo;


    //world
    public void joinWorld() {
        this.handler.setWorld(this.clientWorld);
        this.clientWorld.getEventBus().registerEventListener(this.handler);
        this.player = new Player(this.clientWorld, this.session);
        this.controller = new PlayerController(this, this.player);
        this.levelRenderer = new LevelRenderer(this.getClientWorld(), this.player, this);
        this.setScreen(new HUDScreen());
        this.getClientWorld().addEntity(this.player);
    }

    public void joinLocalWorld(String name) {
        this.clientWorld=new ServerWorld("cubecraft:overworld",new Level("az",this.setting),null,this.setting);
        this.joinWorld();

        /*
        this.integratedServerLocation = new InetSocketAddress(65535);

        Screen waiting = ScreenLoader.loadByExtName("cubecraft", "join_single_player_screen.xml");
        this.setScreen(waiting);
        if (this.server != null) {
            if (this.server.isRunning()) {
                this.server.setRunning(false);
            }
            while (this.server.getStatus() != ServerStatus.UNAVAILABLE) {
                ((Label) waiting.getComponents().get("stage")).setText(new Text(Language.get("join_singleplayer.before_server_init"), 0xFFFFFF, FontAlignment.LEFT));
                this.refreshScreen();
            }
        }

        this.server = new CubecraftServer(this.integratedServerLocation.getPort(), name);
        new Thread(this.server, "server_main").start();
        while (this.server.getStatus() != ServerStatus.STARTUP) {
            ((Label) waiting.getComponents().get("stage")).setText(new Text(Language.get("join_singleplayer.server_init"), 0xFFFFFF, FontAlignment.LEFT));
            this.refreshScreen();
        }
        while (this.server.getStatus() != ServerStatus.RUNNING) {
            ((Label) waiting.getComponents().get("stage")).setText(new Text(Language.get("join_singleplayer.server_load"), 0xFFFFFF, FontAlignment.LEFT));
            this.refreshScreen();
        }
        this.joinOnlineWorld(this.integratedServerLocation);
        while (this.clientWorld == null) {
            ((Label) waiting.getComponents().get("stage")).setText(new Text(Language.get("join_singleplayer.connect"), 0xFFFFFF, FontAlignment.LEFT));
            this.refreshScreen();
        }
        this.joinWorld();

         */
    }

    public void joinOnlineWorld(InetSocketAddress addr) {
        if (this.clientIO.isRunning()) {
            this.leaveServer();
        }
        this.clientIO.setServerAddr(addr);
        this.clientIO.init(this.setting.getValueAsInt("client.net.threads", 4));
        this.clientIO.getHandler().pushSend(new PacketPlayerJoinRequest(this.session));
    }

    public void leaveServer() {
        this.clientIO.getHandler().pushSend(new PacketPlayerLeave());
        this.clientIO.shutdown();
        this.clientWorld = null;
    }

    public void leaveWorld() {
        this.clientWorld = null;
    }


    //application
    @Override
    public void init() {
        Registry.setClient(this);
        Timer.startTiming();
        this.setting.read();

        StartArguments arg = ClientMain.getStartGameArguments();

        Window.initGLFW();

        this.window.create();
        this.window.hint(GLFW.GLFW_SAMPLES,this.setting.getValueAsInt("client.render.fxaa",0));
        this.window.setWindowTitle(arg.getValueAsString("title", "Cubecraft-" + VERSION));
        this.window.setWindowSize(arg.getValueAsInt("width", 1280), arg.getValueAsInt("height", 720));
        this.window.setWindowIcon(ResourceManager.instance.getResource("/resource/cubecraft/texture/ui/icons/icon.png").getAsStream());
        this.window.hint(GLFW.GLFW_RESIZABLE,GLFW.GLFW_TRUE);
        this.window.setWindowVsyncEnable(false);

        Audio.create();


        //init application

        this.timer = new Timer(20);
        this.logHandler = LogHandler.create("Client/Main");
        this.logHandler.info("initializing client...");
        //this.initDisplay();


        this.window.getEventBus().registerEventListener(this.clientInputHandler);


        //load content

        ScreenUtil.initFont();
        this.clientEventBus.callEvent(new ClientInitializeEvent(this));
        this.setScreen(logoLoadingScreen);
        this.logoLoadingScreen.display();
        ScreenUtil.init(this);

        this.logHandler.info("loading mods...");
        ModManager.loadMod(InternalContent.class, null, getPlatformClient(), ExtansionRunningTarget.CLIENT);

        this.player = new Player(null, this.session);

        this.logHandler.info("loading resources...");
        ResourceManager.instance.registerEventListener(new ResourceLoader());
        ResourceManager.instance.reload(this);

        this.logoLoadingScreen.dispose();
        this.getClientEventBus().registerEventListener(new ScreenController());
        this.setScreen("cubecraft", "title_screen.xml");

        this.clientIO.registerNetHandler(this.handler);
        this.clientIO.registerNetHandler(new ClientNetHandlerConnection(this));

        //check version
        if (this.setting.getValueAsBoolean("client.check_update", true)) {
            VersionCheck.check();
        }

        //finish loading
        this.logHandler.info("client initialization done,in%dms", Timer.endTiming());


    }

    @Override
    public void on1sec() {
        if (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() > 512 * 1024 * 1024) {
            System.gc();
        }
    }

    @Override
    public void render() {
        this.logHandler.checkGLError("pre_render");
        this.screenInfo = getDisplaySize();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);//16640
        if (this.window.isWindowCloseRequested()) {
            this.stop();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        //render world
        if (this.screen.isInGameGUI()) {
            this.logHandler.checkGLError("pre_world_render");
            levelRenderer.render(this.timer.interpolatedTime);
            this.logHandler.checkGLError("post_world_render");
        }


        //render gui
        GLUtil.setupOrthogonalCamera(0, 0, this.window.getWindowWidth(), this.window.getWindowHeight(), screenInfo.scrWidth(), screenInfo.scrHeight());
        if (this.screen != null) {
            GLUtil.enableDepthTest();
            GLUtil.enableBlend();
            this.logHandler.checkGLError("pre_screen_render");
            this.screenInfo = this.getDisplaySize();
            this.screen.render(this.screenInfo, this.timer.interpolatedTime);
            ScreenUtil.renderPopup(this.screenInfo, this.timer.interpolatedTime);
            if (!(this.screen instanceof LogoLoadingScreen)) {
                this.logoLoadingScreen.render(screenInfo, this.timer.interpolatedTime);
            }
            this.logHandler.checkGLError("post_screen_render");
            GLUtil.disableBlend();
        }

        //post
        Sync.sync(this.setting.getValueAsInt("client.render.maxFPS", 60));
        this.window.update();
        this.logHandler.checkGLError("post_render");
    }

    @Override
    public void tick() {
        this.screen.tick();
        ScreenUtil.tickPopup();
        if (!(this.screen instanceof LogoLoadingScreen)) {
            this.logoLoadingScreen.tick();
        }
        if (this.getClientWorld() != null) {
            this.getClientWorld().tick();
        }
        if (this.setting.getValueAsBoolean("client.tick_gc", false)) {
            System.gc();
        }
    }

    @Override
    public void stop() {
        this.clientEventBus.callEvent(new ClientShutdownEvent(this));
        this.window.destroy();
        logHandler.info("game stopped...");
        LogHandler.allSave();
        Audio.destroy();
        Window.destroyGLFW();
        System.exit(0);
    }


    //render
    public void setScreen(Screen screen) {
        this.clientEventBus.callEvent(new ScreenInitializeEvent(this, screen));
        if (this.screen != null) {
            this.screen.release();
        }
        this.screen = screen;
        if (screen != null) {
            screen.init(this);
        }
    }

    public void setScreen(String namespace, String uiPosition) {
        this.setScreen(ScreenLoader.loadByExtName(namespace, uiPosition));
    }

    @Override
    public void refreshScreen() {
        this.render();
        if (System.currentTimeMillis() % 5 == 0) {
            this.screen.tick();
        }
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


    //access
    private DisplayScreenInfo getDisplaySize() {
        int scale = this.setting.getValueAsInt("client.render.gui.scale", 2);
        return new DisplayScreenInfo(
                scale,
                Math.max(this.window.getWindowWidth() / scale, 1),
                Math.max(this.window.getWindowHeight() / scale, 1),
                Math.max(this.window.getWindowWidth() / scale, 1) / 2,
                Math.max(this.window.getWindowHeight() / scale, 1) / 2
        );
    }

    public Screen getScreen() {
        return this.screen;
    }

    public LogoLoadingScreen getLoadingScreen() {
        return this.logoLoadingScreen;
    }

    public GameSetting getGameSetting() {
        return this.setting;
    }

    public ClientNettyPipeline getClientIO() {
        return this.clientIO;
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

    public CubecraftServer getServer() {
        return server;
    }

    public void setClientWorld(ClientWorld clientWorld) {
        this.clientWorld = clientWorld;
    }

    public LevelInfo getClientLevelInfo() {
        return this.clientLevelInfo;
    }

    public void setClientLevelInfo(LevelInfo clientLevelInfo) {
        this.clientLevelInfo = clientLevelInfo;
    }

    public Window getWindow() {
        return window;
    }
}
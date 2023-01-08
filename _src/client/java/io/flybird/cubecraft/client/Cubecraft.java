package io.flybird.cubecraft.client;

import io.flybird.cubecraft.client.event.ClientInitializeEvent;
import io.flybird.cubecraft.client.event.ClientShutdownEvent;
import io.flybird.cubecraft.client.event.ScreenInitializeEvent;
import io.flybird.cubecraft.client.gui.HUDScreen;
import io.flybird.cubecraft.client.gui.LogoLoadingScreen;
import io.flybird.cubecraft.client.gui.ScreenUtil;
import io.flybird.cubecraft.client.gui.base.DisplayScreenInfo;
import io.flybird.cubecraft.client.gui.component.Screen;
import io.flybird.cubecraft.auth.Session;
import io.flybird.cubecraft.client.render.renderer.LevelRenderer;
import io.flybird.cubecraft.client.resources.ResourceLoader;

import io.flybird.cubecraft.internal.renderer.ParticleRenderer;
import io.flybird.cubecraft.network.base.ClientIO;
import io.flybird.cubecraft.network.base.RakNetClientIO;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.server.CubecraftServer;
import io.flybird.cubecraft.world.*;
import io.flybird.cubecraft.world.entity.living.Player;
import io.flybird.cubecraft.world.entity.particle.ParticleEngine;
import io.flybird.quantum3d.platform.Sync;
import io.flybird.quantum3d.platform.Window;
import io.flybird.quantum3d.GLUtil;
import io.flybird.util.GameSetting;
import io.flybird.util.LoopTickingApplication;
import io.flybird.util.container.StartArguments;
import io.flybird.util.event.CachedEventBus;
import io.flybird.util.event.EventBus;
import io.flybird.util.logging.LogHandler;
import io.flybird.util.task.TaskProgressUpdateListener;
import io.flybird.util.timer.Timer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.Date;
//todo:add server net support
//todo:add inventory support
//todo:fix smooth light engine
//todo:add json driven block register


public class Cubecraft extends LoopTickingApplication implements TaskProgressUpdateListener {
    private final Logger logger= LogManager.getLogger("Client/Main");

    public static final String VERSION = "0.2.5";

    private final Window window = new Window();
    private final EventBus clientEventBus = new CachedEventBus();
    private final GameSetting setting = new GameSetting(ClientMain.getGamePath() + "/data/configs/settings.properties", "cubecraft client " + VERSION);
    private final GameSetting keyBindSetting = new GameSetting(ClientMain.getGamePath() + "/data/configs/keymapping.properties", "cubecraft client " + VERSION);
    private final LogoLoadingScreen logoLoadingScreen = new LogoLoadingScreen();

    private final Session session = new Session("Notch", "cubecraft:default");

    private ClientIO clientIO = null;
    public LevelRenderer levelRenderer;
    private ParticleEngine particleEngine;
    public PlayerController controller;
    private InetSocketAddress integratedServerLocation;
    private CubecraftServer server;
    private Screen screen;
    private IWorld clientWorld;
    private Player player;
    private LevelInfo clientLevelInfo;


    //world
    public void joinWorld() {
        this.player = new Player(this.clientWorld, this.session);
        this.controller = new PlayerController(this, this.player);
        this.levelRenderer = new LevelRenderer(this.getClientWorld(), this.player, this);
        this.particleEngine=new ParticleEngine(this.clientWorld);
        ((ParticleRenderer) this.levelRenderer.renderers.get("cubecraft:particle_renderer")).setParticleEngine(this.particleEngine);
        this.setScreen(new HUDScreen());
        this.getClientWorld().addEntity(this.player);
        this.player.setPos(0, 130, 0);
        //this.clientIO.getListener().attachEventBus(this.clientWorld.getEventBus());
    }

    public void joinLocalWorld(String name) {
        this.clientWorld = new ServerWorld("cubecraft:overworld", new Level("az", this.setting), null, this.setting);
        this.joinWorld();


        /*
        this.integratedServerLocation = new InetSocketAddress("127.0.0.1",65535);

        Screen waiting = ClientRegistries.GUI_MANAGER.loadFAML("cubecraft", "join_single_player_screen.xml");
        this.setScreen(waiting);
        if (this.server != null) {
            if (this.server.isRunning()) {
                this.server.setRunning(false);
            }
            while (this.server.getStatus() != ServerStatus.UNAVAILABLE) {
                ((Label) waiting.getComponents().get("stage")).setText(new Text(Registries.I18N.get("join_singleplayer.before_server_init"), 0xFFFFFF, FontAlignment.LEFT));
                this.refreshScreen();
            }
        }

        this.server = new CubecraftServer(this.integratedServerLocation.getPort(), name);
        new Thread(this.server, "server_main").start();
        while (this.server.getStatus() != ServerStatus.STARTUP) {
            ((Label) waiting.getComponents().get("stage")).setText(new Text(Registries.I18N.get("join_singleplayer.server_init"), 0xFFFFFF, FontAlignment.LEFT));
            this.refreshScreen();
        }
        while (this.server.getStatus() != ServerStatus.RUNNING) {
            ((Label) waiting.getComponents().get("stage")).setText(new Text(Registries.I18N.get("join_singleplayer.server_load"), 0xFFFFFF, FontAlignment.LEFT));
            this.refreshScreen();
        }
        this.joinOnlineWorld(this.integratedServerLocation);
        while (this.clientWorld == null) {
            ((Label) waiting.getComponents().get("stage")).setText(new Text(Registries.I18N.get("join_singleplayer.connect"), 0xFFFFFF, FontAlignment.LEFT));
            this.refreshScreen();
        }
        this.joinWorld();

         */
    }


    public void joinOnlineWorld(InetSocketAddress addr) {
        if(this.clientIO!=null) {
            this.clientIO.closeConnection();
        }
        this.clientIO=new RakNetClientIO();
        this.clientIO.start(addr);

    }

    public void leaveServer() {
        this.clientIO.closeConnection();
        this.clientIO=null;
        this.clientWorld = null;
    }

    public void leaveWorld() {
        this.clientWorld = null;
    }


    //application
    @Override
    public void init() {
        Window.initGLFW();

        Registries.CLIENT = this;
        Timer.startTiming();

        this.setting.read();
        this.keyBindSetting.read();

        StartArguments arg = ClientMain.getStartGameArguments();
        this.window.create(false);
        this.window.hint(GLFW.GLFW_SAMPLES, this.setting.getValueAsInt("client.render.fxaa", 0));
        this.window.setWindowTitle(arg.getValueAsString("title", "Cubecraft-" + VERSION));
        this.window.setWindowSize(arg.getValueAsInt("width", 1280), arg.getValueAsInt("height", 720));
        this.window.setWindowIcon(ClientRegistries.RESOURCE_MANAGER.getResource("/resource/cubecraft/texture/ui/icons/icon.png").getAsStream());
        this.window.hint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        this.window.setWindowVsyncEnable(false);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);

        //init application

        this.timer = new Timer(20);
        this.logger.info("initializing client...");
        //this.initDisplay();


        //load content

        ScreenUtil.initFont();
        this.clientEventBus.callEvent(new ClientInitializeEvent(this));
        this.setScreen(logoLoadingScreen);
        this.logoLoadingScreen.display();
        ScreenUtil.init(this);

        this.logger.info("loading mods...");
        Registries.MOD.loadMod();

        this.player = new Player(null, this.session);

        this.logger.info("loading resources...");
        ClientRegistries.RESOURCE_MANAGER.registerEventListener(new ResourceLoader());
        ClientRegistries.RESOURCE_MANAGER.reload(this);

        this.logoLoadingScreen.dispose();

        this.setScreen("cubecraft", "title_screen.xml");

        //check version
        if (this.setting.getValueAsBoolean("client.check_update", true)) {
            VersionCheck.check();
        }

        //finish loading
        this.logger.info("client initialization done,in{0}ms", Timer.endTiming());

    }

    @Override
    public void on1sec() {
        if (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() > 512 * 1024 * 1024) {
            System.gc();
        }
    }

    @Override
    public void render() {
        GLUtil.checkGLError("pre_render");
        DisplayScreenInfo screenInfo = getDisplaySize();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);//16640
        if (this.window.isWindowCloseRequested()) {
            this.stop();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        //render world
        if (this.screen.isInGameGUI()) {
            GLUtil.checkGLError("pre_world_render");
            levelRenderer.render(this.timer.interpolatedTime);
            GLUtil.checkGLError("post_world_render");
        }


        //render gui
        GLUtil.setupOrthogonalCamera(0, 0, this.window.getWindowWidth(), this.window.getWindowHeight(), screenInfo.scrWidth(), screenInfo.scrHeight());
        if (this.screen != null) {
            GLUtil.enableDepthTest();
            GLUtil.enableBlend();
            GLUtil.checkGLError("pre_screen_render");
            screenInfo = this.getDisplaySize();
            this.screen.render(screenInfo, this.timer.interpolatedTime);
            ScreenUtil.renderPopup(screenInfo, this.timer.interpolatedTime);
            if (!(this.screen instanceof LogoLoadingScreen)) {
                this.logoLoadingScreen.render(screenInfo, this.timer.interpolatedTime);
            }
            GLUtil.checkGLError("post_screen_render");
            GLUtil.disableBlend();
        }

        //post
        Sync.sync(this.setting.getValueAsInt("client.render.maxFPS", 60));
        this.window.update();
        GLUtil.checkGLError("post_render");
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
        ClientRegistries.SMOOTH_FONT_RENDERER.update();
        ClientRegistries.ICON_FONT_RENDERER.update();
    }

    @Override
    public void stop() {
        this.clientEventBus.callEvent(new ClientShutdownEvent(this));
        this.window.destroy();
        logger.info("game stopped...");
        LogHandler.allSave();
        Window.destroyGLFW();
        System.exit(0);
    }

    public void setScreen(String namespace, String uiPosition) {
        this.setScreen(ClientRegistries.GUI_MANAGER.loadFAML(namespace, uiPosition));
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

    public LogoLoadingScreen getLoadingScreen() {
        return this.logoLoadingScreen;
    }

    public GameSetting getGameSetting() {
        return this.setting;
    }

    public ClientIO getClientIO() {
        return this.clientIO;
    }

    public EventBus getClientEventBus() {
        return clientEventBus;
    }

    public IWorld getClientWorld() {
        return clientWorld;
    }

    public void setClientWorld(ClientWorld clientWorld) {
        this.clientWorld = clientWorld;
    }

    public Player getPlayer() {
        return player;
    }

    public CubecraftServer getServer() {
        return server;
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

    public GameSetting getKeyBindSetting() {
        return keyBindSetting;
    }
}
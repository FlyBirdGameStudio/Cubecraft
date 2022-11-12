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
import io.flybird.cubecraft.internal.InternalContent;
import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.Start;
import io.flybird.cubecraft.client.event.ClientInitializeEvent;
import io.flybird.cubecraft.client.event.ClientShutdownEvent;
import io.flybird.cubecraft.client.event.ScreenInitializeEvent;
import io.flybird.cubecraft.client.gui.DisplayScreenInfo;
import io.flybird.cubecraft.client.gui.ScreenLoader;
import io.flybird.cubecraft.client.gui.ScreenUtil;
import io.flybird.cubecraft.client.gui.screen.LogoLoadingScreen;
import io.flybird.cubecraft.client.gui.screen.Screen;
import io.flybird.cubecraft.client.render.renderer.LevelRenderer;
import io.flybird.cubecraft.resources.ResourceLoader;
import io.flybird.cubecraft.resources.ResourceManager;
import io.flybird.cubecraft.extansion.ExtansionRunningTarget;
import io.flybird.cubecraft.extansion.ModManager;
import io.flybird.cubecraft.extansion.PlatformClient;
import io.flybird.cubecraft.server.CubecraftServer;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.starfish3d.audio.Audio;
import io.flybird.starfish3d.platform.Display;
import io.flybird.starfish3d.platform.input.InputHandler;
import io.flybird.starfish3d.platform.input.Keyboard;
import io.flybird.starfish3d.render.GLUtil;
import io.flybird.util.LogHandler;
import io.flybird.util.LoopTickingApplication;
import io.flybird.util.container.StartArguments;
import io.flybird.util.event.EventBus;
import io.flybird.util.net.UDPSocket;
import io.flybird.util.task.TaskProgressUpdateListener;
import io.flybird.util.timer.Timer;
import org.lwjgl.opengl.GL11;

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
//todo:add block model
//todo:serialize screen into xml
//todo:fix smooth light engine
//todo:add json driven block register

public class Cubecraft extends LoopTickingApplication implements TaskProgressUpdateListener {
    private final CubecraftServer server=new CubecraftServer();
    private final EventBus clientEventBus = new EventBus();
    public static final String VERSION = "alpha-0.2.6";

    //display
    private DisplayScreenInfo screenInfo;
    public LevelRenderer levelRenderer;
    private Screen screen;
    private final LogoLoadingScreen logoLoadingScreen = new LogoLoadingScreen();
    private final ClientInputHandler clientInputHandler = new ClientInputHandler(this);


    private IWorld clientWorld = null;
    private Player player = new Player(null);
    public final PlayerController controller = new PlayerController(this.player);
    public final UDPSocket clientIO = new UDPSocket(Registry.getPacketEncoderMap(), Registry.getPacketDecoderMap(), "127.0.0.1", 11451);

    //world
    public void joinWorld(IWorld world) {
        this.clientWorld = world;
        player.setPos(1024, 10, 1024);
        this.levelRenderer = new LevelRenderer(this.getClientWorld(), this.player);
        this.getClientWorld().addEntity(this.player);
    }

    public void leaveWorld() {
        this.clientWorld = null;
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

    //application
    @Override
    public void init() {
        Registry.setClient(this);
        Timer.startTiming();

        //init application

        this.timer = new Timer(20);
        this.logHandler = LogHandler.create("main", "game");
        this.initDisplay();

        Keyboard.getKeyboardEventBus().registerEventListener(this.clientInputHandler);
        ScreenUtil.initBGRenderer();

        //load content
        ModManager.loadMod(InternalContent.class, null, getPlatformClient(), ExtansionRunningTarget.CLIENT);
        this.clientEventBus.callEvent(new ClientInitializeEvent(this));
        this.setScreen(logoLoadingScreen);
        this.logoLoadingScreen.display();
        GameSetting.instance.read();
        Registry.registerVanillaContent();
        ResourceManager.instance.registerEventListener(new ResourceLoader());
        ResourceManager.instance.reload(this);

        this.logoLoadingScreen.dispose();
        this.getClientEventBus().registerEventListener(new ScreenController());
        this.setScreen("cubecraft","title_screen.xml");

        //check version
        new Thread(new VersionCheck(), "client_update_check").start();

        //finish loading
        this.logHandler.info("client initialization done,in%dms".formatted(Timer.endTiming()));
    }

    @Override
    public void on1sec() {
        if (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() > 512 * 1024 * 1024)
            System.gc();

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

    public void setScreen(String namespace,String uiPosition) {
        this.setScreen(ScreenLoader.loadByExtName(namespace,uiPosition));
    }

    private DisplayScreenInfo getDisplaySize() {
        int scale = GameSetting.instance.getValueAsInt("client.render.gui.scale", 2);
        return new DisplayScreenInfo(
                scale,
                Math.max(Display.getWidth() / scale, 1),
                Math.max(Display.getHeight() / scale, 1),
                Math.max(Display.getWidth() / scale, 1) / 2,
                Math.max(Display.getHeight() / scale, 1) / 2
        );
    }

    protected void initDisplay() {
        StartArguments arg = Start.getStartGameArguments();
        Display.create();
        Display.setFXAA(GameSetting.instance.FXAA);
        String instanceName=arg.getValueAsString("instance", " ");
        Display.setTitle((arg.getValueAsString("title", "Cubecraft-" + VERSION) + instanceName).equals(" ") ?"":"(%s)".formatted(instanceName));
        Display.setIcon(ResourceManager.instance.getResource("/resource/cubecraft/ui/texture/icons/icon.png").getAsStream());
        Display.setResizable(true);
        Display.setVsyncEnable(false);
        Display.setSize(arg.getValueAsInt("width", 1280), arg.getValueAsInt("height", 720));
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
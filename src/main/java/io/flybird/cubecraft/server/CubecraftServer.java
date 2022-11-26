package io.flybird.cubecraft.server;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.net.base.ServerNettyPipeline;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.util.event.EventBus;
import io.flybird.util.logging.LogHandler;
import io.flybird.util.LoopTickingApplication;
import io.flybird.util.container.options.Option;
import io.flybird.util.task.MultiSourceExecution;
import io.flybird.util.timer.Timer;
import io.flybird.cubecraft.world.Level;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CubecraftServer extends LoopTickingApplication {
    private ServerStatus status = ServerStatus.UNAVAILABLE;

    GameSetting setting= new GameSetting("/data/configs/settings.properties","server");
    private Level level;
    private final ExecutorService worldTickingService = Executors.newFixedThreadPool(this.setting.getValueAsInt("server.worldTickThread",1));

    private final MultiSourceExecution<Chunk> worldProvideService = new MultiSourceExecution<>(this.setting.getValueAsInt("server.worldGenThread",1));

    public CubecraftServer(int port, String levelName) {
        this.port = port;
        this.levelName = levelName;
    }

    public MultiSourceExecution<Chunk> getWorldProvideService() {
        return worldProvideService;
    }
    private ServerNettyPipeline serverIO =new ServerNettyPipeline();
    private final PlayerTable playerTable =new PlayerTable();

    private final int port;
    private final String levelName;
    private final EventBus eventBus=new EventBus();

    @Override
    public void init(){
        this.status = ServerStatus.STARTUP;
        long startTime=System.currentTimeMillis();

        this.logHandler= LogHandler.create("Server/Main");
        this.timer=new Timer(20);
        this.level =new Level(levelName,this.setting);
        this.level.createLevelFolder();

        logHandler.info("starting server on"+this.port);

        this.serverIO.setPort(this.port);
        this.serverIO.init(4);

        logHandler.info("loading world...");
        for (IWorld dim:this.level.dims) {
            dim.tick();
        }
        logHandler.info("done,"+((System.currentTimeMillis()-startTime)/1000d));
        this.status = ServerStatus.RUNNING;
    }

    @Override
    public void tick() {
        for (IWorld dim:this.level.dims) {
            worldTickingService.submit(dim::tick);
        }
    }

    @Override
    public void stop() {
        this.status = ServerStatus.STOPPING;
        this.running = false;
        logHandler.info("game stopped...");
        LogHandler.allSave();
        this.status = ServerStatus.UNAVAILABLE;
    }

    public ServerStatus getStatus() {
        return status;
    }

    public PlayerTable getPlayers() {
        return playerTable;
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}

package io.flybird.cubecraft.server;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.internal.net.handler.ServerHandlerPlayerConnection;
import io.flybird.cubecraft.internal.net.handler.ServerHandlerPlayerPlaying;
import io.flybird.util.event.CachedEventBus;
import io.flybird.util.network.base.ServerNettyPipeline;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.util.event.EventBus;
import io.flybird.util.logging.LogHandler;
import io.flybird.util.LoopTickingApplication;
import io.flybird.util.timer.Timer;
import io.flybird.cubecraft.world.Level;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CubecraftServer extends LoopTickingApplication {
    private ServerStatus status = ServerStatus.UNAVAILABLE;

    GameSetting setting= new GameSetting("/data/configs/settings.properties","server");
    private Level level;
    private final ExecutorService worldTickingService = Executors.newFixedThreadPool(this.setting.getValueAsInt("server.worldTickThread",1));

    public CubecraftServer(int port, String levelName) {
        this.port = port;
        this.levelName = levelName;
    }

    private ServerNettyPipeline serverIO =new ServerNettyPipeline();
    private final PlayerTable playerTable =new PlayerTable();

    private final int port;
    private final String levelName;
    private final EventBus eventBus=new CachedEventBus();

    @Override
    public void init(){
        this.status = ServerStatus.STARTUP;
        long startTime=System.currentTimeMillis();

        this.logHandler= LogHandler.create("Server/Main");
        this.timer=new Timer(20);
        this.level =new Level(levelName,this.setting);
        this.level.createLevelFolder();

        logHandler.info("starting server on"+this.port);

        this.serverIO.registerNetHandler(new ServerHandlerPlayerConnection(this));
        this.serverIO.registerNetHandler(new ServerHandlerPlayerPlaying(this));
        this.serverIO.setPort(this.port);
        this.serverIO.init(4);

        logHandler.info("loading world...");
        for (IWorld dim:this.level.dims.values()) {
            dim.tick();
        }
        logHandler.info("done,"+((System.currentTimeMillis()-startTime)/1000d));
        this.status = ServerStatus.RUNNING;
    }

    @Override
    public void tick() {
        for (IWorld dim:this.level.dims.values()) {
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

    public Level getLevel() {
        return this.level;
    }

    public IWorld getDim(String id) {
        return this.getLevel().getDims().get(id);
    }

    public Entity getEntity(String uuid) {
        Entity e;
        for (IWorld world:this.level.dims.values()){
            e=world.getEntity(uuid);
            if(e!=null){
                return e;
            }
        }
        return null;
    }
}

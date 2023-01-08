package io.flybird.cubecraft.server;

import io.flybird.cubecraft.network.base.RakNetServerIO;
import io.flybird.cubecraft.network.base.ServerIO;
import io.flybird.util.GameSetting;
import io.flybird.cubecraft.register.Registries;
import io.flybird.util.event.CachedEventBus;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.util.event.EventBus;
import io.flybird.util.logging.LogHandler;
import io.flybird.util.LoopTickingApplication;
import io.flybird.util.timer.Timer;
import io.flybird.cubecraft.world.Level;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.*;

public class CubecraftServer extends LoopTickingApplication{
    Logger logHandler=LogManager.getLogger("Server/Main");
    public static final String VERSION="0.2.4";



    private ServerStatus status = ServerStatus.UNAVAILABLE;

    final GameSetting setting= new GameSetting("/data/configs/settings.properties","server");
    private Level level;
    private final ExecutorService worldTickingService = Executors.newFixedThreadPool(this.setting.getValueAsInt("server.worldTickThread",1));

    public CubecraftServer(int port, String levelName) {
        this.port = port;
        this.levelName = levelName;
    }

    private final ServerIO serverIO = new RakNetServerIO();
    private final PlayerTable playerTable =new PlayerTable();

    private final int port;
    private final String levelName;
    private final EventBus eventBus=new CachedEventBus();

    @Override
    public void init(){
        Registries.SERVER=this;
        this.status = ServerStatus.STARTUP;
        long startTime=System.currentTimeMillis();

        this.logHandler= LogManager.getLogger("Server/Main");
        this.timer=new Timer(20);
        this.level =new Level(levelName,this.setting);
        this.level.createLevelFolder();

        logHandler.info("starting server on"+this.port);

        //todo:reg handler

        this.serverIO.start(port,128);

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

    public ServerIO getServerIO() {
        return serverIO;
    }
}

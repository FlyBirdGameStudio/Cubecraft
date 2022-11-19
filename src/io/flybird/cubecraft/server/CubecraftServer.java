package io.flybird.cubecraft.server;

import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.util.LogHandler;
import io.flybird.util.LoopTickingApplication;
import io.flybird.util.container.options.Option;
import io.flybird.util.net.UDPSocket;
import io.flybird.util.task.MultiSourceExecution;
import io.flybird.util.timer.Timer;
import io.flybird.cubecraft.world.Level;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CubecraftServer extends LoopTickingApplication {
    private boolean available;

    Option option= new Option("server");
    private Level level;
    private final ExecutorService worldTickingService = Executors.newFixedThreadPool(option.getAsInt("server.worldTickThread",1));

    private final MultiSourceExecution<Chunk> worldProvideService = new MultiSourceExecution<>(option.getAsInt("server.worldGenThread",1));

    public MultiSourceExecution<Chunk> getWorldProvideService() {
        return worldProvideService;
    }

    //private ServerIO serverIO;
    private io.flybird.util.net.UDPSocket UDPSocket;

    @Override
    public void init(){
        this.available =false;
        long startTime=System.currentTimeMillis();

        this.logHandler= LogHandler.create("main","server");
        this.timer=new Timer(20);
        this.level =new Level(StartServer.getArgs().getValueAsString("level","world"));
        this.level.createLevelFolder();

        logHandler.info("starting server on"+this.option.get("server.port"));
        try {
            this.UDPSocket =new UDPSocket(
                    Registry.getPacketEncoderMap(),
                    Registry.getPacketDecoderMap(),
                    "127.0.0.1",
                    (int)this.option.getOrDefault("server.port",11451)
            );
        } catch (Exception e) {
            logHandler.exception(e);
        }

        logHandler.info("loading world...");
        logHandler.info("done,"+((System.currentTimeMillis()-startTime)/1000d));
    }

    @Override
    public void tick() {
        for (IWorld dim:this.level.dims) {
            worldTickingService.submit(dim::tick);
        }
        //this.UDPSocket.tick();
    }

    @Override
    public void stop() {
        this.running = false;
        logHandler.info("game stopped...");
        LogHandler.allSave();
        this.available =true;
    }

    public boolean isAvailable() {
        return available;
    }
}

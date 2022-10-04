package com.flybirdstudio.cubecraft.server;

import com.flybirdstudio.cubecraft.registery.Registry;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.util.LogHandler;
import com.flybirdstudio.util.LoopTickingApplication;
import com.flybirdstudio.util.container.options.Option;
import com.flybirdstudio.util.net.UDPSocket;
import com.flybirdstudio.util.timer.Timer;
import com.flybirdstudio.cubecraft.world.Level;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends LoopTickingApplication {
    Option option= new Option("server");
    private Level world;
    private final ExecutorService worldTickingService = Executors.newFixedThreadPool((Integer) option.get("server.worldTickThread"));
    //private ServerIO serverIO;
    private UDPSocket UDPSocket;

    @Override
    public void init(){
        long startTime=System.currentTimeMillis();

        this.logHandler= LogHandler.create("main","server");
        this.timer=new Timer(20);
        this.world=new Level(StartServer.getArgs().getValueAsString("level","world"));

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
        for (IWorld dim:this.world.dims) {
            worldTickingService.submit(dim::tick);
        }
        this.UDPSocket.tick();
    }

    @Override
    public void stop() {
        this.running = false;
        logHandler.info("game stopped...");
        LogHandler.allSave();
        System.exit(0);
    }
}

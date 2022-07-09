package com.sunrisestudio.cubecraft.server;

import com.sunrisestudio.cubecraft.net.ServerIO;
import com.sunrisestudio.util.LogHandler;
import com.sunrisestudio.util.LoopTickingApplication;
import com.sunrisestudio.util.container.options.Option;
import com.sunrisestudio.util.timer.Timer;
import com.sunrisestudio.cubecraft.world.access.IWorldAccess;
import com.sunrisestudio.cubecraft.world.Level;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends LoopTickingApplication {
    Option option= new Option("server");
    private Level world;
    private final ExecutorService worldTickingService = Executors.newFixedThreadPool((Integer) option.get("server.worldTickThread"));
    private ServerIO serverIO;

    @Override
    public void init(){
        long startTime=System.currentTimeMillis();

        this.logHandler= LogHandler.create("main","server");
        this.timer=new Timer(20);
        this.world=new Level((String) StartServer.getArgs("world name","world"));
        this.serverIO=new ServerIO(this.option);

        logHandler.info("starting server on"+this.option.get("server.port"));
        try {
            this.serverIO.initNettyPipeline();
        } catch (Exception e) {
            logHandler.exception(e);
        }

        logHandler.info("loading world...");
        logHandler.info("done,"+((System.currentTimeMillis()-startTime)/1000d));
    }

    @Override
    public void longTick() {
        for (IWorldAccess dim:this.world.dims) {
            worldTickingService.submit(dim::tick);
        }
    }

    public void stop() {
        this.running = false;
        logHandler.info("game stopped...");
        LogHandler.allSave();
        System.exit(0);
    }
}

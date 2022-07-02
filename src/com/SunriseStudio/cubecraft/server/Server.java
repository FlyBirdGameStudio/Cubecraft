package com.SunriseStudio.cubecraft.server;

import com.SunriseStudio.cubecraft.net.ServerIO;
import com.SunriseStudio.cubecraft.util.LogHandler;
import com.SunriseStudio.cubecraft.util.LoopTickingApplication;
import com.SunriseStudio.cubecraft.util.collections.options.Option;
import com.SunriseStudio.cubecraft.util.timer.Timer;
import com.SunriseStudio.cubecraft.world.IDimensionAccess;
import com.SunriseStudio.cubecraft.world.World;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends LoopTickingApplication {
    Option option= new Option("server");
    private World world;
    private final ExecutorService worldTickingService = Executors.newFixedThreadPool((Integer) option.get("server.worldTickThread"));
    private ServerIO serverIO;

    @Override
    public void init(){
        long startTime=System.currentTimeMillis();

        this.logHandler= LogHandler.create("main","server");
        this.timer=new Timer(20);
        this.world=new World((String) StartServer.getArgs("world name","world"));
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
        for (IDimensionAccess dim:this.world.dims) {
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

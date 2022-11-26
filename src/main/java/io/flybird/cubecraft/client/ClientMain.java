package io.flybird.cubecraft.client;

import io.flybird.cubecraft.client.resources.ResourceManager;
import io.flybird.util.logging.LogHandler;
import io.flybird.util.container.StartArguments;

import java.util.Arrays;

public class ClientMain {
    public static void main(String[] args) {
        startArguments =new StartArguments(args);
        //init log handler
        LogHandler handler=LogHandler.create("Client/Bootstrap");
        LogHandler.setLogPath(getGamePath()+"/data/logs/");
        LogHandler.setLogOutput(startArguments.getValueAsBoolean("log_output",true));
        LogHandler.setLogFormat(getGamePath()+"/data/configs/log_format.json");

        //init game runtime

        handler.info("args:"+ Arrays.toString(args));
        gamePath= startArguments.getValueAsString("path", System.getProperty("user.dir"));
        handler.info("runtime path:"+gamePath);
        System.setProperty("java.library.path", startArguments.getValueAsString("native",gamePath+"/data/native"));
        handler.info("native:"+ System.getProperty("java.library.path"));

        ResourceManager.createResourceFolder();

        //start thread
        Thread thread=new Thread(new Cubecraft());
        thread.setName("client_main");
        thread.start();
    }

    private static String gamePath=System.getProperty("user.dir");

    public static String getGamePath() {
        return gamePath;
    }

    private static StartArguments startArguments;

    public static StartArguments getStartGameArguments() {
        return ClientMain.startArguments;
    }
}

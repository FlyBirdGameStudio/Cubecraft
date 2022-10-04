package com.flybirdstudio.cubecraft;

import com.flybirdstudio.cubecraft.client.Cubecraft;
import com.flybirdstudio.cubecraft.client.resources.ResourceManager;
import com.flybirdstudio.util.LogHandler;
import com.flybirdstudio.util.SystemInfoHelper;
import com.flybirdstudio.util.container.StartArguments;

public class Start {
    public static void main(String[] args) {
        SystemInfoHelper.init();
        //init game runtime
        startArguments =new StartArguments(args);
        gamePath= startArguments.getValueAsString("path", System.getProperty("user.dir"));
        System.setProperty("java.library.path", startArguments.getValueAsString("native",gamePath+"native"));
        LogHandler.setLogPath(getGamePath()+"/data/logs/");
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
        return Start.startArguments;
    }
}

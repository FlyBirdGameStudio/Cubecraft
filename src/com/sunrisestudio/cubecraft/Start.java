package com.sunrisestudio.cubecraft;

import com.sunrisestudio.cubecraft.client.Cubecraft;
import com.sunrisestudio.cubecraft.client.resources.ResourceManager;
import com.sunrisestudio.util.LogHandler;
import com.sunrisestudio.util.container.StartArguments;

public class Start {
    public static void main(String[] args) {
        //init game runtime
        startArguments =new StartArguments(args);
        gamePath= (String) startArguments.getValue("path", System.getProperty("user.dir"));
        System.setProperty("java.library.path", (String) startArguments.getValue("native",gamePath+"native"));
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

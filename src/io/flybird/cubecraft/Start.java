package io.flybird.cubecraft;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.resources.ResourceManager;
import io.flybird.util.LogHandler;
import io.flybird.util.container.StartArguments;

import java.io.*;

public class Start {
    public static void main(String[] args) throws IOException {
        //init game runtime
        startArguments =new StartArguments(args);
        gamePath= startArguments.getValueAsString("path", System.getProperty("user.dir"));
        System.setProperty("java.library.path", startArguments.getValueAsString("native",gamePath+"/data/native"));
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

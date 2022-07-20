package com.sunrisestudio.cubecraft;

import com.sunrisestudio.util.StartArguments;

public class Start {
    public static void main(String[] args) {
        //init game runtime
        startArguments =new StartArguments(args);
        gamePath= (String) startArguments.getValue("path", System.getProperty("user.dir"));
        System.setProperty("java.library.path", (String) startArguments.getValue("native","native"));

        //start thread
        Thread thread=new Thread(new CubeCraft());
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

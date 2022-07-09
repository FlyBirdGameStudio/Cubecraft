package com.sunrisestudio.cubecraft;

import com.sunrisestudio.cubecraft.options.StartGameArguments;
import com.sunrisestudio.util.LogHandler;

public class Start {
    public static void main(String[] args) {
        //init game runtime
        startGameArguments=new StartGameArguments(args);
        gamePath= (String) getArgs("path",System.getProperty("user.dir"));
        loadLib();
        LogHandler logHandler = LogHandler.create("start", "client");

        //start thread
        Thread thread=new Thread(new CubeCraft());
        thread.setName("client_main");
        thread.start();
    }

    private static String gamePath;

    public static String getGamePath() {
        return gamePath;
    }

    private static StartGameArguments startGameArguments;

    public static Object getArgs(String id,Object ifNull){
        return startGameArguments.getValue(id,ifNull);
    }

    public static void loadLib(){
        System.setProperty("java.library.path", (String) startGameArguments.getValue("native","native"));
    }
}

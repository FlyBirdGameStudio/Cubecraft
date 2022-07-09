package com.sunrisestudio.cubecraft.server;

import com.sunrisestudio.cubecraft.options.StartGameArguments;
import com.sunrisestudio.util.LogHandler;

public class StartServer {
    public static Server server;

    public static void main(String[] args) {
        LogHandler logHandler=LogHandler.create("start","server");
        //init game runtime
        startGameArguments=new StartGameArguments(args);
        gamePath= (String) getArgs("path",System.getProperty("user.dir"));

        //start thread
        if(server!=null&&server.isRunning()){
            logHandler.warning("already running server,stopping it!");
            server.stop();
            logHandler.info("existing server stopped,now starting new server!");
        }

        server=new Server();
        Thread thread=new Thread(server);
        thread.setName("server_main");
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
}

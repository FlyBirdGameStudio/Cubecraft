package io.flybird.cubecraft.server;

import io.flybird.util.container.StartArguments;
import io.flybird.util.LogHandler;

public class StartServer {
    public static CubecraftServer server;

    public static void main(String[] args) {
        LogHandler logHandler=LogHandler.create("start","server");
        //init game runtime
        startArguments =new StartArguments(args);
        gamePath= startArguments.getValueAsString("path",System.getProperty("user.dir"));

        //start thread
        if(server!=null&&server.isRunning()){
            logHandler.warning("already running server,stopping it!");
            server.stop();
            logHandler.info("existing server stopped,now starting new server!");
        }

        server=new CubecraftServer();
        Thread thread=new Thread(server);
        thread.setName("server_main");
        thread.start();
    }

    private static String gamePath;

    public static String getGamePath() {
        return gamePath;
    }

    private static StartArguments startArguments;

    public static StartArguments getArgs() {
        return startArguments;
    }
}

package io.flybird.cubecraftbootstrap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String dir=System.getProperty("user.dir")+"/bootstrap.json";
        if(args.length!=0){
           dir=args[0];
        }

        Gson gson=new GsonBuilder()
                .registerTypeAdapter(Instance.class,new InstanceDispatcher())
                .registerTypeAdapter(RunConfiguration[].class,new BootstrapFileDispatcher())
                .create();

        try {
            RunConfiguration defaultConfig=gson.fromJson(new String(new FileInputStream(dir).readAllBytes()),RunConfiguration[].class)[0];
            System.out.println(generateCommand(defaultConfig));
        } catch (IOException e) {
            System.out.println("could not read bootstrap file,launcher core will exit.");
            e.printStackTrace();
            System.exit(0);
        }

    }


    public static String generateCommand(RunConfiguration config){
        StringBuilder command=new StringBuilder().append("java ");

        //class path:mod and lib
        command.append("-classpath");
        for (String lib: config.lib()){
            command.append("/libraries").append(lib).append(" ");
        }
        for (String m:config.instance().mods()){
            command.append("/data/mods").append(m).append(" ");
        }

        //custom option
        for (String m:config.instance().vmOption()){
            command.append(m).append(" ");
        }

        //jar
        command.append("-jar ").append(config.instance().core()).append(" ");

        for (String m:config.instance().gameOption()){
            command.append(m).append(" ");
        }

        command.append("instance=").append(config.name());
        return command.toString();
    }
}

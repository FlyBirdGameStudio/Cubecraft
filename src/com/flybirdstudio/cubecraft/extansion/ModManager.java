package com.flybirdstudio.cubecraft.extansion;

import com.flybirdstudio.cubecraft.Start;
import com.flybirdstudio.cubecraft.client.Cubecraft;
import com.flybirdstudio.util.task.LoadTask;
import com.flybirdstudio.util.ReflectHelper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

public class ModManager {
    public static HashMap<String,Mod> mods=new HashMap<>();

    public static void loadMod(String path,PlatformServer server,PlatformClient client,ExtansionRunningTarget tgt){
        Attributes attr;
        try {
            attr = new JarFile(path).getManifest().getAttributes("modInfo");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Mod mod= ((Class<? extends Mod>) ReflectHelper.loadJarFromAbsolute(path).get(attr.getValue("modEntry"))).
                    getDeclaredConstructor(PlatformClient.class,PlatformServer.class,ExtansionRunningTarget.class)
                    .newInstance(client,server,tgt);
            mod.construct();
            mods.put(attr.getValue("modName"),mod);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException |
                 IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, Mod> getLoadedMods() {
        return mods;
    }

    public static void loadMod(Cubecraft client){
        File[] mods = new File(Start.getGamePath() + "/mods").listFiles();
        if(new File(Start.getGamePath() + "/mods").exists()) {
            if (mods != null) {
                new LoadTask(mods.length, 0.2f, 0.5f, count ->
                        ModManager.loadMod(mods[count].getAbsolutePath(), null,
                                client.getPlatformClient(),
                                ExtansionRunningTarget.CLIENT),client);
            } else {
                throw new RuntimeException("null or invalid mod path");
            }
        }
    }
}

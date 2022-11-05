package io.flybird.cubecraft.extansion;

import io.flybird.cubecraft.Start;
import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.util.LogHandler;
import io.flybird.util.task.LoadTask;
import io.flybird.util.ReflectHelper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

public class ModManager {
    static LogHandler logger=LogHandler.create("mod loader","client");
    public static HashMap<String,Mod> mods=new HashMap<>();

    public static void loadMod(String path,PlatformServer server,PlatformClient client,ExtansionRunningTarget tgt){
        Attributes attr;
        try {
            attr = new JarFile(path).getManifest().getAttributes("modInfo");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            loadMod(ReflectHelper.loadJarFromAbsolute(path).get(attr.getValue("modEntry")),server,client,tgt);
        } catch (IOException e) {
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

    //todo:read mod attr
    public static void loadMod(Class<?> clazz,PlatformServer server,PlatformClient client,ExtansionRunningTarget tgt) {
        Mod mod= null;
        try {
            mod = (Mod) clazz
                    .getDeclaredConstructor(PlatformClient.class, PlatformServer.class, ExtansionRunningTarget.class)
                    .newInstance(client,server,tgt);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        CubecraftMod annotation=mod.getClass().getAnnotation(CubecraftMod.class);
        mod.construct();
        logger.info("constructing mod:"+annotation.name()+"-"+annotation.version());
        mods.put(annotation.name(), mod);
    }
}

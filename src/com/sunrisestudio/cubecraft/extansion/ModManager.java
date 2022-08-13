package com.sunrisestudio.cubecraft.extansion;

import com.sunrisestudio.util.ReflectHelper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
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
}

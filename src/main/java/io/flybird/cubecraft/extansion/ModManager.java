package io.flybird.cubecraft.extansion;

import io.flybird.cubecraft.client.ClientMain;
import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.extansion.event.ModLoadEvent;
import io.flybird.util.logging.LogHandler;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;


public class ModManager {
    static final LogHandler logger = LogHandler.create("ModLoader");
    public static final HashMap<String, Mod> mods = new HashMap<>();

    public static HashMap<String, Mod> getLoadedMods() {
        return mods;
    }

    public static void loadMod(Cubecraft client) {
        File[] mods = new File(ClientMain.getGamePath() + "/mods").listFiles();
        if (new File(ClientMain.getGamePath() + "/mods").exists()) {
            if (mods != null) {
                //todo:load mod
            } else {
                throw new RuntimeException("null or invalid mod path");
            }
        }
    }

    public static void loadMod(Class<?> clazz,boolean client) {
        Mod mod;
        try {
            mod = (Mod) clazz
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        CubecraftMod annotation = mod.getClass().getAnnotation(CubecraftMod.class);
        mod.load(new ModLoadEvent(client));
        logger.info("constructing mod:" + annotation.name() + "-" + annotation.version());
        mods.put(annotation.name(), mod);
    }
}

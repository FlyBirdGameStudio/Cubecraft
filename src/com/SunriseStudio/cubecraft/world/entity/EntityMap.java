package com.SunriseStudio.cubecraft.world.entity;

import com.SunriseStudio.cubecraft.util.LogHandler;
import com.SunriseStudio.cubecraft.world.IDimensionAccess;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class EntityMap {
    private static final LogHandler logHandler=LogHandler.create("EntityManager","server");

    public static HashMap<String,Class<? extends Entity>> entityClasses=new HashMap<>();

    public static Entity getEntity(IDimensionAccess world, String id){
        if(!entityClasses.containsKey(id)){
            logHandler.warning("invalid entity id:"+id);
            return null;
        }
        try {
            return entityClasses.get(id).getDeclaredConstructor(IDimensionAccess.class).newInstance(world);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logHandler.exception(e);
            return null;
        }
    }
}

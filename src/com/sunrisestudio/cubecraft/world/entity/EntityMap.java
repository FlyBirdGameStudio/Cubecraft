package com.sunrisestudio.cubecraft.world.entity;

import com.sunrisestudio.util.LogHandler;
import com.sunrisestudio.cubecraft.world.access.IWorldAccess;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class EntityMap {

    private static final EntityMap instance=new EntityMap();
    private final LogHandler logHandler=LogHandler.create("EntityManager","server");

    public HashMap<String,Class<? extends Entity>> entityClasses=new HashMap<>();


    public Entity getEntity(IWorldAccess world, String id){
        if(!entityClasses.containsKey(id)){
            logHandler.warning("invalid entity id:"+id);
            return null;
        }
        try {
            return entityClasses.get(id).getDeclaredConstructor(IWorldAccess.class).newInstance(world);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logHandler.exception(e);
            return null;
        }
    }

    public void register(String id, Class<? extends Entity> clazz) {
        if(entityClasses.containsKey(id)){
            logHandler.error("find conflict id:"+clazz.getName()+entityClasses.get(id));
            return;
        }
        entityClasses.put(id,clazz);
    }

    public static EntityMap getInstance() {
        return instance;
    }
}

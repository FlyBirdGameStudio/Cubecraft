package com.sunrisestudio.cubecraft.world.access;

import com.sunrisestudio.cubecraft.world.entity.Entity;

import java.util.Collection;

public interface IEntityAccess {
    //--- entity ---
    Collection<Entity> getAllEntities();

    Entity getEntity(String uid);

    void spawnEntity(String id, double x, double y, double z);

    void addEntity(Entity e);

    void removeEntity(String uid);

    void removeEntity(Entity e);
}

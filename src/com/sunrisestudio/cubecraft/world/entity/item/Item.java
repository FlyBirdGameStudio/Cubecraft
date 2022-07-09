package com.sunrisestudio.cubecraft.world.entity.item;

import com.sunrisestudio.cubecraft.world.access.IWorldAccess;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import org.joml.Vector3d;

public abstract class Item extends Entity {
    public Item(IWorldAccess world) {
        super(world);
    }

    @Override
    public Vector3d getCameraPosition() {
        return new Vector3d(0,0,0);
    }

    @Override
    public double getReachDistance() {
        return 0;
    }

    @Override
    public Item[] getDrop() {
        return null;
    }

    @Override
    public int getHealth() {
        return 114514;
    }
}
package com.flybirdstudio.cubecraft.world.entity.item;

import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.entity.Entity;
import com.flybirdstudio.cubecraft.world.item.ItemStack;
import org.joml.Vector3d;

public abstract class Item extends Entity {
    public ItemStack stack;

    public Item(IWorld world) {
        super(world);
    }

    @Override
    public Vector3d getCameraPosition() {
        return new Vector3d(0,0,0);
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

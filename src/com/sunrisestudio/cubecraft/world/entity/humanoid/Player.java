package com.sunrisestudio.cubecraft.world.entity.humanoid;

import com.sunrisestudio.cubecraft.world.access.IWorldAccess;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.cubecraft.world.entity.item.Item;

public class Player extends Humanoid {
    public Player(IWorldAccess world) {
        super(world);
    }

    @Override
    public void onHit(Entity from, IWorldAccess world) {

    }

    @Override
    public void render(float interpolationTime) {

    }

    @Override
    public String getID() {
        return "player";
    }

    @Override
    public Item[] getDrop() {
        return new Item[0];
    }

}

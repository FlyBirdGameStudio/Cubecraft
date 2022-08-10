package com.sunrisestudio.cubecraft.world.entity.humanoid;

import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.cubecraft.world.entity.item.Item;

public class Player extends Humanoid {
    public Player(IWorldAccess world) {
        super(world);
        this.flyingMode=true;
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


    @Override
    public void tick() {
        super.tick();
    }
}

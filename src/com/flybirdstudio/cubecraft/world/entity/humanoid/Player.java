package com.flybirdstudio.cubecraft.world.entity.humanoid;

import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.entity.item.Item;

public class Player extends Humanoid {


    public Player(IWorld world) {
        super(world);
        this.flyingMode=true;
    }


    @Override
    public void render(float interpolationTime) {

    }

    @Override
    public String getID() {
        return "cubecraft:player";
    }

    @Override
    public Item[] getDrop() {
        return new Item[0];
    }


    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public double getReachDistance() {
        return 5;
    }
}

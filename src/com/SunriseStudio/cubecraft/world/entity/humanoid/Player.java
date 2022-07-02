package com.SunriseStudio.cubecraft.world.entity.humanoid;

import com.SunriseStudio.cubecraft.world.IDimensionAccess;
import com.SunriseStudio.cubecraft.world.entity.Entity;
import com.SunriseStudio.cubecraft.world.entity.EntityMap;
import com.SunriseStudio.cubecraft.world.entity.item.Item;

public class Player extends Humanoid {
    public Player(IDimensionAccess world) {
        super(world);
    }

    @Override
    public void onHit(Entity from, IDimensionAccess world) {

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

    static {
        EntityMap.entityClasses.put("player",Player.class);
    }
}

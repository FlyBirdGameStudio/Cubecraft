package com.sunrisestudio.cubecraft.client.render.model.object;

import com.sunrisestudio.cubecraft.world.entity.Entity;

public record EntityModel(
        String id,String namespace,
        Cube[] objects
) implements Model{
    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getNameSpace() {
        return namespace;
    }

    public void render(Entity entity){

    }
}

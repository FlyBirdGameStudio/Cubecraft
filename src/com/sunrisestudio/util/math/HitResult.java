package com.sunrisestudio.util.math;

import com.sunrisestudio.cubecraft.world.World;
import com.sunrisestudio.cubecraft.world.entity.Entity;

public record HitResult(HitBox aabb, byte facing) {
    public void hit(World worldAccess, Entity from) {
        this.aabb.getObject().onHit(from,worldAccess,this);
    }

    public void interact(World worldAccess, Entity from) {
        this.aabb.getObject().onInteract(from,worldAccess,this);
    }
}

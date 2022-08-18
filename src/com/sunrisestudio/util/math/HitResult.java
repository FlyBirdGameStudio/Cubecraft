package com.sunrisestudio.util.math;

import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.cubecraft.world.entity.Entity;

public record HitResult(HitBox aabb, byte facing) {
    public void hit(IWorldAccess worldAccess, Entity from) {
        this.aabb.getObject().onHit(from,worldAccess,this);
    }

    public void interact(IWorldAccess worldAccess, Entity from) {
        this.aabb.getObject().onInteract(from,worldAccess,this);
    }
}

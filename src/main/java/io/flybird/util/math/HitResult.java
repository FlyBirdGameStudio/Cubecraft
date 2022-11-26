package io.flybird.util.math;

import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.Entity;

public record HitResult(HitBox aabb, byte facing) {
    public void hit(IWorld worldAccess, Entity from) {
        this.aabb.getObject().onHit(from,worldAccess,this);
    }

    public void interact(IWorld worldAccess, Entity from) {
        this.aabb.getObject().onInteract(from,worldAccess,this);
    }
}

package io.flybird.util.math;

import io.flybird.cubecraft.world.HittableObject;
import org.joml.Vector3d;

public class HitBox extends AABB {
    HittableObject obj;
    Vector3d vec;

    public HitBox(AABB collisionBox, HittableObject obj, Vector3d vec) {
        super(collisionBox);
        this.obj=obj;
        this.vec=vec;
    }

    public HittableObject getObject() {
        return this.obj;
    }

    public Vector3d getPosition() {
        return vec;
    }
}

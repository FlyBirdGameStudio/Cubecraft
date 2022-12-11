package io.flybird.util.math;

import io.flybird.cubecraft.world.HittableObject;
import org.joml.Vector3d;

/**
 * a simple aabb,but takes more hit info.
 *
 * @author GrassBlock2022
 */
public class HitBox extends AABB {
    HittableObject obj;
    Vector3d vec;

    public HitBox(AABB collisionBox, HittableObject obj, Vector3d vec) {
        super(collisionBox);
        this.obj = obj;
        this.vec = vec;
    }

    /**
     * get target object
     *
     * @return obj
     */
    public HittableObject getObject() {
        return this.obj;
    }

    /**
     * get position
     *
     * @return pos
     */
    public Vector3d getPosition() {
        return vec;
    }
}

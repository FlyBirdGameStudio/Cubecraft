package io.flybird.util.math;

import org.joml.Vector3d;

/**
 * a simple aabb,but takes more hit info.
 *
 * @author GrassBlock2022
 */
public class HitBox<F, W> extends AABB {
    final HittableObject<F,W> obj;
    final Vector3d vec;

    public HitBox(AABB collisionBox, HittableObject<F,W> obj, Vector3d vec) {
        super(collisionBox);
        this.obj = obj;
        this.vec = vec;
    }

    /**
     * get target object
     *
     * @return obj
     */
    public HittableObject<F,W> getObject() {
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

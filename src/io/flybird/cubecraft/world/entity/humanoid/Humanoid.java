package io.flybird.cubecraft.world.entity.humanoid;

import io.flybird.cubecraft.world.IWorld;
import io.flybird.util.math.AABB;
import io.flybird.util.math.HitBox;
import io.flybird.cubecraft.world.entity.Entity;
import org.joml.Vector3d;

public abstract class Humanoid extends Entity {
    @Override
    public Vector3d getCameraPosition() {
        return new Vector3d(0,1.62,0);
    }

    @Override
    public double getReachDistance() {
        return 5;
    }

    @Override
    public HitBox[] getSelectionBoxes() {
        return new HitBox[]{new HitBox(this.collisionBox,this,this.collisionBox.getCenter())};
    }

    @Override
    public AABB getCollisionBoxSize() {
        return new AABB(-0.3,0,-0.3,0.3,1.75,0.3);
    }

    public Humanoid(IWorld world) {
        super(world);
    }

    @Override
    public int getHealth() {
        return 20;
    }
}

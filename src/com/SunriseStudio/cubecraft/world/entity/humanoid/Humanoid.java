package com.sunrisestudio.cubecraft.world.entity.humanoid;

import com.sunrisestudio.util.math.AABB;
import com.sunrisestudio.util.math.HitBox;
import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.cubecraft.world.entity.Entity;
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
        return null;
    }

    @Override
    public AABB getCollisionBoxSize() {
        return new AABB(-0.3,0,-0.3,0.3,1.8,0.3);
    }

    public Humanoid(IWorldAccess world) {
        super(world);
    }

    @Override
    public int getHealth() {
        return 20;
    }
}
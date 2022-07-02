package com.SunriseStudio.cubecraft.world.entity.humanoid;

import com.SunriseStudio.cubecraft.util.math.AABB;
import com.SunriseStudio.cubecraft.util.math.HitBox;
import com.SunriseStudio.cubecraft.world.IDimensionAccess;
import com.SunriseStudio.cubecraft.world.entity.Entity;
import org.joml.Vector3d;

public abstract class Humanoid extends Entity {
    @Override
    public Vector3d getCameraPosition() {
        return new Vector3d(0.3,1.62,0);
    }

    @Override
    public double getReachDistance() {
        return 3;
    }

    @Override
    public HitBox[] getSelectionBoxes() {
        return new HitBox[]{HitBox.createFromCollision(this)};
    }

    @Override
    public AABB getCollisionBoxSize() {
        return new AABB(-0.3,0,-0.3,0.3,1.8,0.3);
    }

    public Humanoid(IDimensionAccess world) {
        super(world);
    }

    @Override
    public int getHealth() {
        return 20;
    }
}

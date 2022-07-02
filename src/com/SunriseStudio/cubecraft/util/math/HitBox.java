package com.SunriseStudio.cubecraft.util.math;

import com.SunriseStudio.cubecraft.world.IDimensionAccess;
import com.SunriseStudio.cubecraft.world.entity.Entity;

public class HitBox extends AABB {
    HitListener hitListener;
    public HitBox(double x0, double y0, double z0, double x1, double y1, double z1,HitListener hl) {
        super(x0, y0, z0, x1, y1, z1);
        this.hitListener=hl;
    }

    public HitBox(AABB collisionBox, HitListener hitListener) {
        super(collisionBox);
        this.hitListener=hitListener;
    }

    public interface HitListener{
        void hit(IDimensionAccess iDimensionAccess, Entity from);
    }

    /**
     * this creates a hit box from an {@link Entity},also added {@link HitListener}
     * @param target entity
     * @return hit box
     */
    public static HitBox createFromCollision(Entity target){
        return new HitBox(target.collisionBox, (iDimensionAccess, from) -> target.onHit(from,iDimensionAccess));
    }
}

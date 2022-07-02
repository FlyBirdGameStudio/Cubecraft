package com.SunriseStudio.cubecraft.util.grass3D.render.culling;

import com.SunriseStudio.cubecraft.util.math.AABB;
import org.joml.FrustumIntersection;

public class JomlFrustumAdapter extends ICuller{
    @Override
    public boolean aabbVisible(AABB aabb) {
        return new FrustumIntersection().testAab((float) aabb.x0,(float) aabb.y0,(float) aabb.z0,(float) aabb.x1,(float) aabb.y1,(float) aabb.z1);
    }

    @Override
    public boolean[] aabbsVisible(AABB[] aabb) {
        boolean[] booleans=new boolean[aabb.length];
        for (int i=0;i< aabb.length;i++){
            booleans[i]=this.aabbVisible(aabb[i]);
        }
        return booleans;
    }
}

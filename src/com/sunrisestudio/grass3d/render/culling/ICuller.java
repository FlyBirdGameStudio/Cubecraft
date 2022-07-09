package com.sunrisestudio.grass3d.render.culling;

import com.sunrisestudio.util.math.AABB;

import java.util.Arrays;

public abstract class ICuller {
    public boolean listVisible(int list) {
        return true;
    }

    public boolean[] listsVisible(int base, int range) {
        boolean[] booleans=new boolean[range];
        Arrays.fill(booleans,true);
        return booleans;
    }

    public boolean[] listsVisible(int[] list) {
        boolean[] booleans=new boolean[list.length];
        Arrays.fill(booleans,true);
        return booleans;
    }

    public boolean aabbVisible(AABB aabb) {
        return true;
    }

    public boolean[] aabbsVisible(AABB[] aabb) {
        boolean[] booleans=new boolean[aabb.length];
        Arrays.fill(booleans,true);
        return booleans;
    }

    public static ICuller CreateOcclusionCuller(){return new OcclusionCuller();}
    public static ICuller CreateFrustumCuller(){return new Frustum();}
    public static ICuller CreateJomlFrustumCuller(){return new JomlFrustumAdapter();}

    public void update() {

    }
}

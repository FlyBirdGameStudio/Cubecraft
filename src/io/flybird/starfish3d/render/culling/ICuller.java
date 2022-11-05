package io.flybird.starfish3d.render.culling;

import io.flybird.starfish3d.render.Camera;
import io.flybird.util.math.AABB;

import java.util.Arrays;

public abstract class ICuller {
    final Camera camera;

    public ICuller(Camera camera){
        this.camera = camera;
    }


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

    public void update() {

    }


}

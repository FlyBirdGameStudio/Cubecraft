package com.sunrisestudio.cubecraft.render.object;

import com.sunrisestudio.grass3d.render.culling.Cullable;
import com.sunrisestudio.cubecraft.render.sort.DistanceComparable;
import com.sunrisestudio.grass3d.render.multiThread.VertexArrayCompileCallable;
import com.sunrisestudio.util.math.AABB;

public interface IRenderObject extends DistanceComparable, VertexArrayCompileCallable, Cullable {
    void render();
    AABB getVisibleArea();
}

package com.sunrisestudio.cubecraft.client.render.object;

import com.sunrisestudio.grass3d.render.culling.Cullable;
import com.sunrisestudio.cubecraft.client.render.sort.DistanceComparable;
import com.sunrisestudio.grass3d.render.multiThread.VertexArrayCompileCallable;
import com.sunrisestudio.util.math.AABB;

public interface IRenderObject extends DistanceComparable, VertexArrayCompileCallable {
    void render();
}

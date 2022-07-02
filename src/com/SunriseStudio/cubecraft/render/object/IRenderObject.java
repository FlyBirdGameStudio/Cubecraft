package com.SunriseStudio.cubecraft.render.object;

import com.SunriseStudio.cubecraft.util.grass3D.render.culling.Cullable;
import com.SunriseStudio.cubecraft.render.sort.DistanceComparable;
import com.SunriseStudio.cubecraft.util.grass3D.render.multiThread.VertexArrayCompileCallable;
import com.SunriseStudio.cubecraft.util.math.AABB;

public interface IRenderObject extends DistanceComparable, VertexArrayCompileCallable, Cullable {
    void render();
    AABB getVisibleArea();
}

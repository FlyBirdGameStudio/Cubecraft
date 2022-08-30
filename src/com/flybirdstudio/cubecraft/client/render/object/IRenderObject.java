package com.flybirdstudio.cubecraft.client.render.object;

import com.flybirdstudio.cubecraft.client.render.sort.DistanceComparable;
import com.flybirdstudio.starfish3d.render.multiThread.VertexArrayCompileCallable;

public interface IRenderObject extends DistanceComparable, VertexArrayCompileCallable {
    void render();
}

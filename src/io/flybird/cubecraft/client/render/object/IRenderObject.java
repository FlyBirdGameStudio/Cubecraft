package io.flybird.cubecraft.client.render.object;

import io.flybird.cubecraft.client.render.sort.DistanceComparable;
import io.flybird.starfish3d.render.multiThread.VertexArrayCompileCallable;

public interface IRenderObject extends DistanceComparable, VertexArrayCompileCallable {
    void render();
}

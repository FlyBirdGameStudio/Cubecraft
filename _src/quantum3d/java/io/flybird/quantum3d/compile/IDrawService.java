package io.flybird.quantum3d.compile;

import io.flybird.quantum3d.draw.DrawCompile;
import io.flybird.quantum3d.draw.IDrawCompile;
import io.flybird.util.container.ArrayQueue;

public interface IDrawService<V extends VertexArrayCompileCallable> {
    void startDrawing(V v);

    void startDirect(V v);

    int getResultSize();

    DrawCompile getAvailableCompile();

    IDrawCompile getAllCompile();

    int getAllResultSize();

    ArrayQueue<V> getCache();
}

package io.flybird.starfish3d.render.multiThread;

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

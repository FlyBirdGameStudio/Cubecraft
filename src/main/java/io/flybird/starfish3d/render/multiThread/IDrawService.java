package io.flybird.starfish3d.render.multiThread;

public interface IDrawService<V extends VertexArrayCompileCallable> {
    void startDrawing(V v);

    int getResultSize();

    DrawCompile getAvailableCompile();

    IDrawCompile getAllCompile();

    int getAllResultSize();
}

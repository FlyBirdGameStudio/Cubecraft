package io.flybird.starfish3d.render.multiThread;

public record EmptyDrawCompile(VertexArrayCompileCallable obj) implements IDrawCompile{

    @Override
    public VertexArrayCompileCallable getObject() {
        return obj;
    }
}

package io.flybird.starfish3d.render.multiThread;

import io.flybird.starfish3d.render.GLUtil;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.drawcall.IRenderCall;

public record DrawCompile(IRenderCall call, VertexArrayBuilder builder, VertexArrayCompileCallable obj) implements Comparable<DrawCompile>,IDrawCompile{
    @Override
    public int compareTo(DrawCompile o) {
        return 0;
    }

    public void draw() {
        GLUtil.assertRenderThread();
        this.call.upload(builder);
    }

    @Override
    public VertexArrayCompileCallable getObject(){
        return obj;
    }
}

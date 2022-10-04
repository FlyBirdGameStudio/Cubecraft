package com.flybirdstudio.starfish3d.render.multiThread;

import com.flybirdstudio.starfish3d.render.GLUtil;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayUploader;
import com.flybirdstudio.starfish3d.render.drawcall.IRenderCall;
import org.lwjgl.opengl.GL11;

public record DrawCompile(IRenderCall call, VertexArrayBuilder builder) implements Comparable<DrawCompile> {
    @Override
    public int compareTo(DrawCompile o) {
        return 0;
    }

    public void draw() {
        GLUtil.assertRenderThread();
        this.call.upload(builder);
    }
}

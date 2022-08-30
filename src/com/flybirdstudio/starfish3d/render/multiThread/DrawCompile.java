package com.flybirdstudio.starfish3d.render.multiThread;

import com.flybirdstudio.starfish3d.render.GLUtil;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayUploader;
import org.lwjgl.opengl.GL11;

public record DrawCompile(Integer list, VertexArrayBuilder builder) implements Comparable<DrawCompile> {
    @Override
    public int compareTo(DrawCompile o) {
        return 0;
    }

    public void draw() {
        GLUtil.assertRenderThread();
        GL11.glNewList(list,GL11.GL_COMPILE);
        VertexArrayUploader.uploadPointer(builder);
        GL11.glEndList();
    }
}

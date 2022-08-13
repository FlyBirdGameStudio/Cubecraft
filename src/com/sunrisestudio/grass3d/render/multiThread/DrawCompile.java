package com.sunrisestudio.grass3d.render.multiThread;

import com.sunrisestudio.grass3d.render.GLUtil;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayUploader;
import com.sunrisestudio.grass3d.render.draw.VertexArrayUploader;
import org.lwjgl.opengl.GL11;

public record DrawCompile(Integer list, IVertexArrayBuilder builder) implements Comparable<DrawCompile> {
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

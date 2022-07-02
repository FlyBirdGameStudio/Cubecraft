package com.SunriseStudio.cubecraft.util.grass3D.render.multiThread;

import com.SunriseStudio.cubecraft.util.grass3D.render.GLUtil;
import com.SunriseStudio.cubecraft.util.grass3D.render.draw.IVertexArrayBuilder;
import com.SunriseStudio.cubecraft.util.grass3D.render.draw.IVertexArrayUploader;
import org.lwjgl.opengl.GL11;

public record DrawCompile(Integer list, IVertexArrayBuilder builder) implements Comparable<DrawCompile> {
    @Override
    public int compareTo(DrawCompile o) {
        return 0;
    }

    public void draw() {
        GLUtil.assertRenderThread();
        GL11.glNewList(list,GL11.GL_COMPILE);
        IVertexArrayUploader.createNewPointedUploader().upload(builder);
        GL11.glEndList();
    }
}

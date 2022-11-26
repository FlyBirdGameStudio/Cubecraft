package io.flybird.starfish3d.render.drawcall;

import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.draw.VertexArrayUploader;
import org.lwjgl.opengl.GL11;

public class ListRenderCall implements IRenderCall{
    int list;

    @Override
    public void call() {
        GL11.glCallList(this.list);
    }

    @Override
    public void upload(VertexArrayBuilder builder) {
        GL11.glNewList(this.list,GL11.GL_COMPILE);
        VertexArrayUploader.uploadPointer(builder);
        GL11.glEndList();
    }

    @Override
    public void allocate() {
        this.list=GL11.glGenLists(1);
    }

    @Override
    public void free() {
        GL11.glDeleteLists(this.list,1);
    }
}

package io.flybird.starfish3d.render.drawcall;

import io.flybird.starfish3d.render.GLUtil;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.draw.VertexArrayUploader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class VBORenderCall implements IRenderCall {
    int vbo;
    int drawMode;
    int count;

    @Override
    public void call() {
        if(count>0) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,this.vbo);

            GL11.glTexCoordPointer(3, GL11.GL_DOUBLE, 13*8 ,0);
            GL11.glColorPointer(4, GL11.GL_DOUBLE, 13*8 ,24);
            GL11.glNormalPointer(GL11.GL_DOUBLE, 13*8 ,56);
            GL11.glVertexPointer(3, GL11.GL_DOUBLE, 13*8,80);

            GLUtil.allEnableClientState();
            GL11.glDrawArrays(drawMode, 0, count);
            GLUtil.allDisableClientState();

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
        }
    }

    @Override
    public void upload(VertexArrayBuilder builder) {
        VertexArrayUploader.uploadVBO(builder,vbo);
        this.drawMode=builder.getDrawMode();
        this.count=builder.getVertexCount();
    }

    @Override
    public void allocate() {
        this.vbo= GL15.glGenBuffers();
    }

    @Override
    public void free() {
        GL15.glDeleteBuffers(this.vbo);
    }
}

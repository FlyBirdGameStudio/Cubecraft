package com.sunrisestudio.grass3d.render.draw;

import com.sunrisestudio.util.LogHandler;
import com.sunrisestudio.util.container.BufferBuilder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import java.nio.DoubleBuffer;

public class VertexArrayUploader {
    private static final LogHandler logHandler=LogHandler.create("vertex_array_uploader","grass3D");

    private static final DoubleBuffer vertex = BufferUtils.createDoubleBuffer(131072);
    private static final DoubleBuffer normal = BufferUtils.createDoubleBuffer(131072);
    private static final DoubleBuffer tc = BufferUtils.createDoubleBuffer(131072);
    private static final DoubleBuffer color = BufferUtils.createDoubleBuffer(131072);

    private static final DoubleBuffer raw=BufferUtils.createDoubleBuffer(1048576);

    public static void uploadPointer(IVertexArrayBuilder arr) {
        logHandler.checkGLError("pre_upload_pointer");

        //VertexArrayFormat format = arr.getFormat();

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);

        //int all= format.colorSize()+ format.vertexSize()+ format.texCoordSize()+(format.useNormal()?3:0);

        BufferBuilder.fillBuffer(tc,arr.getTexCoordArray());
        BufferBuilder.fillBuffer(vertex,arr.getVertexArray());
        BufferBuilder.fillBuffer(color,arr.getColorArray());
        BufferBuilder.fillBuffer(normal,arr.getNormalArray());

        GL11.glTexCoordPointer(3,GL11.GL_DOUBLE,0,MemoryUtil.memAddress(tc));
        GL11.glColorPointer(4, GL11.GL_DOUBLE,0,MemoryUtil.memAddress(color));
        GL11.glNormalPointer(GL11.GL_DOUBLE,0, MemoryUtil.memAddress(normal));
        GL11.glVertexPointer(3,GL11.GL_DOUBLE,0, MemoryUtil.memAddress(vertex));

        GL11.glDrawArrays(GL11.GL_QUADS,0,arr.getVertexCount());

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);

        logHandler.checkGLError("post_upload_pointer");
    }

        public static void uploadVBO (IVertexArrayBuilder arr,int vbo){
            BufferBuilder.fillBuffer(raw,arr.getRawArray());
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, raw, vbo);
        }

}

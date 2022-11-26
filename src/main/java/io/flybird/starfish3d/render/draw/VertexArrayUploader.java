package io.flybird.starfish3d.render.draw;

import io.flybird.util.logging.LogHandler;
import io.flybird.util.container.BufferUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.DoubleBuffer;

public class VertexArrayUploader {
    private static final LogHandler logHandler = LogHandler.create("Grass3D/VertexUploader");

    private static final DoubleBuffer vertex = BufferUtils.createDoubleBuffer(131072);
    private static final DoubleBuffer normal = BufferUtils.createDoubleBuffer(131072);
    private static final DoubleBuffer tc = BufferUtils.createDoubleBuffer(131072);
    private static final DoubleBuffer color = BufferUtils.createDoubleBuffer(131072);
    private static final DoubleBuffer raw = BufferUtils.createDoubleBuffer(1048576);

    private static int uploadedCount;

    public static void uploadPointer(VertexArrayBuilder arr) {
        uploadedCount+=arr.vertexCount;
        logHandler.checkGLError("pre_upload_pointer");

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);

        BufferUtil.fillBuffer(tc, arr.getTexCoordArray());
        BufferUtil.fillBuffer(vertex, arr.getVertexArray());
        BufferUtil.fillBuffer(color, arr.getColorArray());
        BufferUtil.fillBuffer(normal, arr.getNormalArray());

        GL11.glTexCoordPointer(3, GL11.GL_DOUBLE, 0, MemoryUtil.memAddress(tc));
        GL11.glColorPointer(4, GL11.GL_DOUBLE, 0, MemoryUtil.memAddress(color));
        GL11.glNormalPointer(GL11.GL_DOUBLE, 0, MemoryUtil.memAddress(normal));
        GL11.glVertexPointer(3, GL11.GL_DOUBLE, 0, MemoryUtil.memAddress(vertex));

        GL11.glDrawArrays(arr.getDrawMode(), 0, arr.getVertexCount());

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);

        logHandler.checkGLError("post_upload_pointer");
    }

    public static void uploadVBO(VertexArrayBuilder arr, int vbo) {
        uploadedCount+=arr.vertexCount;
        BufferUtil.fillBuffer(raw, arr.getRawArray());
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, raw, 35044);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
    }



    public static int getUploadedCount() {
        return uploadedCount;
    }

    public static void resetUploadCount(){
        uploadedCount=0;
    }

    public static void drawArrays(int drawMode, int i, int count) {
        GL11.glDrawArrays(drawMode, i, count);
        uploadedCount+=count;
    }
}

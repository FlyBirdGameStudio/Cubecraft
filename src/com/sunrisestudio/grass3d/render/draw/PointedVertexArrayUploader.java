package com.sunrisestudio.grass3d.render.draw;

import com.sunrisestudio.util.LogHandler;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

/**
 * this uploader upload every channel using vertexPointer().
 * this uploader should work with{@link ChanneledVertexArrayBuilder}.still compatible with any type of builder,but will slightly slower.
 *
 */
public class PointedVertexArrayUploader extends IVertexArrayUploader {
    LogHandler logHandler=LogHandler.create("vertex_uploader","client");

    @Override
    public void upload(IVertexArrayBuilder builder) {
        logHandler.checkGLError("pre_upload");
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);

        GL11.glTexCoordPointer(3,GL11.GL_DOUBLE,0,MemoryUtil.memAddress(builder.getTexCoordBuffer()));
        GL11.glColorPointer(4, GL11.GL_DOUBLE,0,MemoryUtil.memAddress(builder.getColorBuffer()));
        GL11.glNormalPointer(GL11.GL_DOUBLE,0, MemoryUtil.memAddress(builder.getNormalBuffer()));
        GL11.glVertexPointer(3,GL11.GL_DOUBLE,0, MemoryUtil.memAddress( builder.getVertexBuffer()));

        GL11.glDrawArrays(GL11.GL_QUADS, 0, builder.getVertexCount());

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        logHandler.checkGLError("post_upload");
    }
}

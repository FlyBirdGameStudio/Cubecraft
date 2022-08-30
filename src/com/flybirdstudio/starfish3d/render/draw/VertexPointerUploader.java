package com.flybirdstudio.starfish3d.render.draw;

import com.flybirdstudio.util.LogHandler;
import com.flybirdstudio.util.container.BufferBuilder;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.DoubleBuffer;

/**
 * this uploader upload every channel using vertexPointer().
 * this uploader should work with{@link VertexArrayBuilder}.still compatible with any type of builder,but will slightly slower.
 *
 */
public class VertexPointerUploader extends IVertexArrayUploader {
    LogHandler logHandler=LogHandler.create("vertex_uploader","client");

    @Override
    public void upload(VertexArrayBuilder builder) {

        DoubleBuffer tc= BufferBuilder.from(builder.getTexCoordArray());
        DoubleBuffer vertex= BufferBuilder.from(builder.getVertexArray());
        DoubleBuffer normal=BufferBuilder.from(builder.getNormalArray());
        DoubleBuffer color=BufferBuilder.from(builder.getColorArray());

        logHandler.checkGLError("pre_upload");
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);

        GL11.glTexCoordPointer(3,GL11.GL_DOUBLE,0,MemoryUtil.memAddress(tc));
        GL11.glColorPointer(4, GL11.GL_DOUBLE,0,MemoryUtil.memAddress(color));
        GL11.glNormalPointer(GL11.GL_DOUBLE,0, MemoryUtil.memAddress(normal));
        GL11.glVertexPointer(3,GL11.GL_DOUBLE,0, MemoryUtil.memAddress(vertex));

        GL11.glDrawArrays(GL11.GL_QUADS, 0, builder.getVertexCount());

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        logHandler.checkGLError("post_upload");

        tc=null;
        vertex=null;
        color=null;
        normal=null;
    }


}

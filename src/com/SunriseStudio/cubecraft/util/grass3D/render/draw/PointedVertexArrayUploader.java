package com.SunriseStudio.cubecraft.util.grass3D.render.draw;

import com.SunriseStudio.cubecraft.util.LogHandler;
import org.lwjgl.opengl.GL11;

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

        GL11.glTexCoordPointer(3,0,builder.getTexCoordBuffer());
        GL11.glColorPointer(4, 0,builder.getColorBuffer());
        GL11.glNormalPointer(0, builder.getNormalBuffer());
        GL11.glVertexPointer(3,0, builder.getVertexBuffer());

        GL11.glDrawArrays(GL11.GL_QUADS, 0, builder.getVertexCount());

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        logHandler.checkGLError("post_upload");
    }
}

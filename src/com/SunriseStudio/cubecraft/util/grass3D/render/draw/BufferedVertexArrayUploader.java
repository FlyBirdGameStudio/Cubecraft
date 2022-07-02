package com.SunriseStudio.cubecraft.util.grass3D.render.draw;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.*;


public class BufferedVertexArrayUploader extends IVertexArrayUploader{

    public BufferedVertexArrayUploader() {}

    public void upload(IVertexArrayBuilder builder){
        int buffer=GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,buffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,builder.getRawBuffer(),GL15.GL_STATIC_DRAW);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);

        GL11.glTexCoordPointer(3,GL11.GL_FLOAT,13,0L);
        GL11.glColorPointer(4, GL11.GL_FLOAT, 13, 12L);
        GL11.glNormalPointer(GL11.GL_FLOAT, 13, 28L);
        GL11.glVertexPointer(3,GL11.GL_FLOAT,13,40L);

        GL11.glDrawArrays(GL11.GL_QUADS, 0, builder.getVertexCount());

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        GL15.glDeleteQueries(buffer);
    }
}
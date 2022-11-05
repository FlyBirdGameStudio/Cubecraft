package io.flybird.starfish3d.render.textures;

import io.flybird.util.ImageUtil;
import io.flybird.util.container.BufferUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class Texture2D extends Texture {
    public Texture2D(boolean ms, boolean mip) {
        super(ms, mip);
    }

    @Override
    public void load(ITextureImage image) {
        BufferedImage img = image.getAsImage();
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.bind();
        ByteBuffer buffer=ImageUtil.getByteFromBufferedImage_RGBA(img);
        GL11.glTexImage2D(this.getBindingType(),0,GL11.GL_RGBA,width,height,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,buffer);
        BufferUtil.free(buffer);
        logHandler.checkGLError("load");
    }

    @Override
    public int getBindingType() {
        if (this.multiSample) {
            return GL32.GL_TEXTURE_2D_MULTISAMPLE;
        } else {
            return GL11.GL_TEXTURE_2D;
        }
    }
}

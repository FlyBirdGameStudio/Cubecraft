package com.sunrisestudio.grass3d.render.textures;

import com.sunrisestudio.cubecraft.client.resources.ResourceManager;
import com.sunrisestudio.cubecraft.client.resources.ImageUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import java.awt.image.BufferedImage;

public class Texture2D extends Texture {
    public Texture2D(boolean ms, boolean mip) {
        super(ms, mip);
    }

    @Override
    public void load(String path) {
        BufferedImage img = ResourceManager.instance.getImage(path);
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.bind();
        GL11.glTexImage2D(this.getType(),0,GL11.GL_RGBA,width,height,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE, ImageUtil.getByteFromBufferedImage_RGBA(img));
        logHandler.checkGLError("load");
    }

    @Override
    public int getType() {
        if (this.multiSample) {
            return GL32.GL_TEXTURE_2D_MULTISAMPLE;
        } else {
            return GL11.GL_TEXTURE_2D;
        }
    }

    @Override
    public int getBindingType() {
        return getType();
    }
}

package com.sunrisestudio.grass3d.render.textures;

import com.sunrisestudio.cubecraft.resources.ResourcePacks;
import com.sunrisestudio.cubecraft.resources.ResourceUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Texture2D extends Texture {

    public Texture2D(boolean ms, boolean mip) {
        super(ms, mip);
    }

    public static HashMap<String,Texture2D>globals=new HashMap<>();

    public static Texture2D getGlobalTexture(String id) {
        return globals.getOrDefault(id,new Texture2D(false,false));
    }

    public static void createGlobalTexture(String id,Texture2D texture2D){
        globals.put(id,texture2D);
    }

    @Override
    public void load(String path) {
        BufferedImage img = ResourcePacks.instance.getImage(path);
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.bind();
        GL11.glTexImage2D(this.getType(),0,GL11.GL_RGBA,width,height,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE, ResourceUtil.getImageBufferFromStream(img));
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

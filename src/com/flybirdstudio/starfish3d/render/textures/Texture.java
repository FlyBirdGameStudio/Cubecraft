package com.flybirdstudio.starfish3d.render.textures;

import com.flybirdstudio.util.LogHandler;
import org.lwjgl.opengl.*;

public abstract class Texture {
    protected int glId;
    protected int width;
    protected int height;
    protected final boolean multiSample;
    protected final boolean mipMap;
    protected final LogHandler logHandler=LogHandler.create("texture","client");

    public Texture(boolean multiSample,boolean mipMap){
        this.multiSample=multiSample;
        this.mipMap=mipMap;
    }

    //operation
    public void bind() {
        GL11.glEnable(this.getBindingType());
        GL11.glBindTexture(this.getType(),this.glId);
        logHandler.checkGLError("texture_binding");
    }

    public void unbind(){
        GL11.glDisable(this.getBindingType());
        logHandler.checkGLError("texture_unbind");
    }

    public abstract int getBindingType();

    public void createMipMap() {
        this.bind();
        GL30.glGenerateMipmap(this.getType());
        logHandler.checkGLError("create_mipmap");
    }

    //load
    public void generateTexture(){
        this.glId=GL11.glGenTextures();
        this.bind();
        if(mipMap) {
            GL11.glTexParameteri(this.getType(), GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        }else{
            GL11.glTexParameteri(this.getType(), GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        }
        GL11.glTexParameteri(this.getType(), GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        logHandler.checkGLError("generate_texture");
    }

    public abstract void load(String path);

    public void buildMipmap(){
        if(this.mipMap){
            this.bind();
            GL30.glGenerateMipmap(this.getType());
        }
    }

    //meta
    public abstract int getType();

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}

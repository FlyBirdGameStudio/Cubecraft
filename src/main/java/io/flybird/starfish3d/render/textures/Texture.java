package io.flybird.starfish3d.render.textures;

import io.flybird.util.logging.LogHandler;
import org.lwjgl.opengl.*;

public abstract class Texture {
    protected int glId;
    protected int width;
    protected int height;
    protected final boolean multiSample;
    protected final boolean mipMap;
    protected final LogHandler logHandler=LogHandler.create("Starfish3D/Texture-"+this);

    public Texture(boolean multiSample,boolean mipMap){
        this.multiSample=multiSample;
        this.mipMap=mipMap;
    }

    //operation
    public void bind() {
        GL11.glEnable(this.getBindingType());
        GL11.glBindTexture(this.getBindingType(),this.glId);
        logHandler.checkGLError("texture_binding");
    }

    public void unbind(){
        GL11.glDisable(this.getBindingType());
        logHandler.checkGLError("texture_unbind");
    }

    public abstract int getBindingType();

    //load
    public void generateTexture(){
        this.glId=GL11.glGenTextures();
        this.bind();
        if(mipMap) {
            GL11.glTexParameteri(this.getBindingType(), GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        }else{
            GL11.glTexParameteri(this.getBindingType(), GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        }
        GL11.glTexParameteri(this.getBindingType(), GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        logHandler.checkGLError("generate_texture");
    }

    public abstract void load(ITextureImage image);

    //meta
    //public abstract int getType();

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}

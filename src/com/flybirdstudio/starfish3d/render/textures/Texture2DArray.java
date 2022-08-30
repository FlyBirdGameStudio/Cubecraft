package com.flybirdstudio.starfish3d.render.textures;

import com.flybirdstudio.cubecraft.client.resources.ImageUtil;
import com.flybirdstudio.cubecraft.client.resources.ResourceManager;
import org.lwjgl.opengl.*;

import java.awt.image.BufferedImage;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class Texture2DArray extends Texture{
    public HashMap<String,Integer> textureMapping=new HashMap<>();
    public int prevLayer=0;
    private final int count;

    public Texture2DArray(boolean ms, boolean mip, int count, int width, int height) {
        super(ms,mip);
        this.width=width;
        this.height=height;
        this.count=count;
    }

    @Override
    public void generateTexture(){
        super.generateTexture();
        this.bind();
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL12.glTexImage3D(this.getType(),1,GL11.GL_RGBA,width,height,count,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        logHandler.checkGLError("generate_texture");
    }

    @Override
    public void load(String path) {
        BufferedImage img= ResourceManager.instance.getImage(path);
        int width=img.getWidth();
        int height=img.getHeight();
        this.bind();
        GL12.glTexSubImage3D(getType(),1,0,0,prevLayer,width,height,1,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,ImageUtil.getByteFromBufferedImage_RGBA(img));
        this.textureMapping.put(path,prevLayer);
        this.unbind();
        logHandler.checkGLError("load");
        prevLayer++;
    }

    @Override
    public int getType() {
        if(this.multiSample){
            return GL32.GL_TEXTURE_2D_MULTISAMPLE_ARRAY;
        }else{
            return GL30.GL_TEXTURE_2D_ARRAY;
        }
    }

    @Override
    public int getBindingType() {
        return GL30.GL_TEXTURE_2D_ARRAY;
    }

    public int getLayer(String texture) {
        return this.textureMapping.get(texture);
    }

    public boolean contains(String s) {
        return this.textureMapping.containsKey(s);
    }
}

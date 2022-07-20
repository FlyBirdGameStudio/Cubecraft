package com.sunrisestudio.grass3d.render.textures;

import com.sunrisestudio.cubecraft.resources.ResourcePacks;
import com.sunrisestudio.util.container.buffer.BufferBuilder;
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
        BufferedImage img= ResourcePacks.instance.getImage(path);
        int width=img.getWidth();
        int height=img.getHeight();

        int[] rawPixels =new int[width*height];
        byte[] newPixels = new byte[rawPixels.length * 4];
        img.getRGB(0, 0, width , height, rawPixels, 0, width);
        for (int i = 0; i < rawPixels.length; ++i) {
            int a = rawPixels[i] >> 24 & 0xFF;
            int r = rawPixels[i] >> 16 & 0xFF;
            int g = rawPixels[i] >> 8 & 0xFF;
            int b = rawPixels[i] & 0xFF;
            newPixels[i * 4] = (byte)r;
            newPixels[i * 4 + 1] = (byte)g;
            newPixels[i * 4 + 2] = (byte)b;
            newPixels[i * 4 + 3] = (byte)a;
        }
        this.bind();
        ByteBuffer buf=BufferBuilder.getB(newPixels);
        GL12.glTexSubImage3D(getType(),1,0,0,prevLayer,width,height,1,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,buf);
        buf=null;

        this.textureMapping.put(path,prevLayer);
        this.unbind();
        logHandler.checkGLError("load");
    }

    public void setPrevLayer(int prevLayer) {
        this.prevLayer = prevLayer;
    }

    public void buildMipmap(){
        createMipMap();
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
}

package io.flybird.starfish3d.render.textures;

import io.flybird.util.ImageUtil;
import io.flybird.util.container.BufferUtil;
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
        GL12.glTexImage3D(this.getBindingType(),1,GL11.GL_RGBA,width,height,count,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        logHandler.checkGLError("generate_texture");
    }

    @Override
    public void load(ITextureImage image) {
        BufferedImage img= image.getAsImage();
        int width=img.getWidth();
        int height=img.getHeight();
        this.bind();
        ByteBuffer buffer= ImageUtil.getByteFromBufferedImage_RGBA(img);
        GL12.glTexSubImage3D(getBindingType(),1,0,0,prevLayer,width,height,1,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,buffer);
        BufferUtil.free(buffer);
        this.textureMapping.put(image.getName(),prevLayer);
        this.unbind();
        logHandler.checkGLError("load");
        prevLayer++;
    }

    @Override
    public int getBindingType() {
        if(this.multiSample){
            return GL32.GL_TEXTURE_2D_MULTISAMPLE_ARRAY;
        }else{
            return GL30.GL_TEXTURE_2D_ARRAY;
        }
    }

    public int getLayer(String texture) {
        return this.textureMapping.get(texture);
    }

    public boolean contains(String s) {
        return this.textureMapping.containsKey(s);
    }
}

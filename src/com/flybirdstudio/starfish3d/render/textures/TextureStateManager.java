package com.flybirdstudio.starfish3d.render.textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class TextureStateManager {
    public static void setTextureClamp(Texture t,boolean clamp){
        t.bind();
        if (clamp) {
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
        }
        else {
            GL11.glTexParameteri(3553, 10242, 10497);
            GL11.glTexParameteri(3553, 10243, 10497);
        }
        t.unbind();
    }

    public static void setTextureMipMap(Texture t,boolean mipmap){
        t.bind();
        GL11.glTexParameteri(t.getBindingType(), GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_NEAREST);
        GL11.glTexParameteri(t.getBindingType(), GL11.GL_TEXTURE_MIN_FILTER, mipmap?GL11.GL_LINEAR_MIPMAP_LINEAR:GL11.GL_NEAREST);
        if(mipmap){
            GL30.glGenerateMipmap(t.getBindingType());
        }
        t.unbind();
    }


}

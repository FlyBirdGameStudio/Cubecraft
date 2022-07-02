package com.SunriseStudio.cubecraft.resources.textures;


import com.SunriseStudio.cubecraft.resources.ResourcePacks;
import com.SunriseStudio.cubecraft.resources.ResourceUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

public class TextureManagerDirect {
    private static HashMap<String, ImageData> idMap = new HashMap();
    public static void bind(String file) {
        if(idMap.containsKey(file)){
            GL11.glBindTexture(3553,idMap.get(file).id);
        }else{
            try {
                load(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static ImageData getData(String file) {
        return idMap.getOrDefault(file, null);
    }

    public static class ImageData {
        public int id;
        public int width;
        public  int height;
    }


    public static int load(String resourceName) throws IOException {
        IntBuffer ib = BufferUtils.createIntBuffer(1);
        ib.clear();
        GL11.glGenTextures(ib);
        int id = ib.get(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_NEAREST_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        BufferedImage img= ResourcePacks.instance.getImage(resourceName);

        int w = img.getWidth();
        int h = img.getHeight();
        ImageData imgData=new ImageData();



        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w,h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, ResourceUtil.getImageBufferFromStream(img));
        GL30.glGenerateMipmap(3553);
        imgData.width=w;
        imgData.height=h;
        imgData.id=id;
        idMap.put(resourceName,imgData);
        return id;
    }
}



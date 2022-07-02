package com.SunriseStudio.cubecraft.resources.textures;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Textures {
    private HashMap<String, Integer> idMap = new HashMap();

    public int loadTexture(String resourceName, int mode) {
        try {
            if (this.idMap.containsKey(resourceName)) {
                return this.idMap.get(resourceName);
            }
            IntBuffer ib = BufferUtils.createIntBuffer(1);
            ib.clear();
            GL11.glGenTextures(ib);
            int id = ib.get(0);
            this.idMap.put(resourceName, id);
            GL11.glBindTexture(3553, id);
            GL11.glTexParameteri(3553, 10241, mode);
            GL11.glTexParameteri(3553, 10240, mode);
            BufferedImage img = ImageIO.read(Textures.class.getResourceAsStream(resourceName));
            int w = img.getWidth();
            int h = img.getHeight();
            ByteBuffer pixels = BufferUtils.createByteBuffer(w * h * 4);
            int[] rawPixels = new int[w * h];
            byte[] newPixels = new byte[w * h * 4];
            img.getRGB(0, 0, w, h, rawPixels, 0, w);
            for (int i = 0; i < rawPixels.length; ++i) {
                int a = rawPixels[i] >> 24 & 0xFF;
                int r = rawPixels[i] >> 16 & 0xFF;
                int g = rawPixels[i] >> 8 & 0xFF;
                int b = rawPixels[i] & 0xFF;
                newPixels[i * 4 + 0] = (byte)r;
                newPixels[i * 4 + 1] = (byte)g;
                newPixels[i * 4 + 2] = (byte)b;
                newPixels[i * 4 + 3] = (byte)a;
            }
            pixels.put(newPixels);
            pixels.position(0).limit(newPixels.length);
            GLU.gluBuild2DMipmaps(3553, 6408, w, h, 6408, 5121, pixels);
            return id;
        }
        catch (IOException e) {
            throw new RuntimeException("!!");
        }
    }
}

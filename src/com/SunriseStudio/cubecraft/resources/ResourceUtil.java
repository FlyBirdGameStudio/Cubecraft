package com.SunriseStudio.cubecraft.resources;

import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class ResourceUtil {
    public static ByteBuffer getImageBufferFromStream(BufferedImage img){
        ByteBuffer pixels = BufferUtils.createByteBuffer(16777216);
        int w=img.getWidth();
        int h=img.getHeight();
        int[] rawPixels = new int[w * h];
        byte[] newPixels = new byte[w * h * 4];
        img.getRGB(0, 0, w, h, rawPixels, 0, w);
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
        pixels.put(newPixels);
        pixels.position(0);
        return pixels;
    }
}

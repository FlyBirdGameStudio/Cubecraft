package com.flybirdstudio.starfish3d.render.textures;

import com.flybirdstudio.cubecraft.client.resources.ImageUtil;
import com.flybirdstudio.cubecraft.client.resources.ResourceManager;
import com.flybirdstudio.util.container.CollectionUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class Texture2DTileMap extends Texture2D {
    public static final int MAX_SUPPORTED_COUNT = 1048576;
    public static final int ROWS = 64;
    public static final int COLUMNS=1024;
    final int tileSize;
    final HashMap<String, Integer> mapping = new HashMap<>();
    int id;
    int count;

    public Texture2DTileMap(boolean ms, boolean mip, int tileSize, String... names) {
        super(ms, mip);
        this.tileSize = tileSize;
        if (names.length > MAX_SUPPORTED_COUNT) {
            throw new IllegalArgumentException("given size out of supported size(%d/%d)".formatted(names.length, MAX_SUPPORTED_COUNT));
        }
        this.count = names.length;
        int i = 0;
        for (String s : names) {
            this.mapping.put(s, i);
            i++;
        }
    }

    @Override
    public int getBindingType() {
        return this.multiSample ? GL32.GL_TEXTURE_2D_MULTISAMPLE : GL11.GL_TEXTURE_2D;
    }

    @Override
    public void generateTexture() {
        super.generateTexture();
    }

    public void load(String cacheLoc){
        this.bind();
        int w = ROWS * tileSize*3;
        int h = this.count / ROWS * tileSize*3;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        CollectionUtil.iterateMap(this.mapping, (key, item) -> {
            BufferedImage img2=ResourceManager.instance.getImage(key);
            if (img2 != null) {
                if(img2.getWidth()==tileSize&&img2.getHeight()==tileSize){
                    img.getGraphics().drawImage(img2, getTextureXPos(key), getTextureYPos(key), null);
                    img.getGraphics().drawImage(img2, getTextureXPos(key)+tileSize, getTextureYPos(key), null);
                    img.getGraphics().drawImage(img2, getTextureXPos(key)-tileSize, getTextureYPos(key), null);
                    img.getGraphics().drawImage(img2, getTextureXPos(key), getTextureYPos(key)+tileSize, null);
                    img.getGraphics().drawImage(img2, getTextureXPos(key), getTextureYPos(key)-tileSize, null);
                }
            }
        });
        GL11.glTexImage2D(this.getBindingType(), 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, ImageUtil.getByteFromBufferedImage_RGBA(img));
        logHandler.checkGLError("load");
        File f=new File(cacheLoc);
        try {
            f.createNewFile();
            ImageIO.write(img,"png",f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reload(String name) {
        if (this.mapping.containsKey(name)) {
            BufferedImage img = ResourceManager.instance.getImage(name);
            this.bind();
            GL11.glTexSubImage2D(3553, 0,
                    getTextureXPos(name),
                    getTextureYPos(name),
                    tileSize, tileSize,
                    GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, ImageUtil.getByteFromBufferedImage_RGBA(img));
            logHandler.checkGLError("load");
        }
    }

    public int getTextureXGrid(String name) {
        if (this.mapping.containsKey(name)) {
            return this.mapping.get(name) % ROWS;
        } else {
            return -1;
        }
    }

    public int getTextureYGrid(String name) {
        if (this.mapping.containsKey(name)) {
            return this.mapping.get(name) / ROWS;
        } else {
            return -1;
        }
    }


    public int getTextureXPos(String name) {
        if (this.mapping.containsKey(name)) {
            return this.mapping.get(name) % ROWS * this.tileSize*3+this.tileSize;
        } else {
            return -1;
        }
    }

    public int getTextureYPos(String name) {
        if (this.mapping.containsKey(name)) {
            return this.mapping.get(name) / ROWS * this.tileSize*3+this.tileSize;
        } else {
            return -1;
        }
    }


    public float exactTextureU(String name, float relative) {
        if (this.mapping.containsKey(name)) {
            int i = getTextureXGrid(name);
            return (i + (relative+1)/3) / (float) ROWS;
        } else {
            return relative;
        }
    }

    public float exactTextureV(String name, float relative) {
        if (this.mapping.containsKey(name)) {
            int i = getTextureYGrid(name);
            return (i + (relative+1)/3)/ (this.count / ROWS);
        } else {
            return relative;
        }
    }
}

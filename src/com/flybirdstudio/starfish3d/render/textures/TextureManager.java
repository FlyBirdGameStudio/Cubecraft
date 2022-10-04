package com.flybirdstudio.starfish3d.render.textures;


import com.flybirdstudio.cubecraft.client.resources.ImageUtil;
import com.flybirdstudio.cubecraft.client.resources.ResourceManager;
import com.flybirdstudio.util.container.ArrayQueue;
import com.flybirdstudio.util.container.Pair;
import com.flybirdstudio.util.math.MathHelper;
import com.flybirdstudio.util.task.TaskProgressUpdateListener;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TextureManager {
    private TextureContainer<Texture2D> texture2DContainer = new TextureContainer<>();
    private TextureContainer<Texture2DTileMap> texture2DTileMapContainer = new TextureContainer<>();

    public TextureContainer<Texture2D> getTexture2DContainer() {
        return texture2DContainer;
    }

    public TextureContainer<Texture2DTileMap> getTexture2DTileMapContainer() {
        return texture2DTileMapContainer;
    }

    public Texture2D createTexture2D(String path, boolean ms, boolean mip) {
        Texture2D texture = new Texture2D(ms, mip);
        texture.generateTexture();
        texture.load(path);
        this.getTexture2DContainer().set(path, texture);
        return texture;
    }

    public Texture2DTileMap createTexture2DTileMap(String id, boolean ms, boolean mip,int tileSize, TaskProgressUpdateListener listener,int loadStart,int loadEnd, String... names) {
        Texture2DTileMap texture2DTileMap = new Texture2DTileMap(ms, mip, tileSize, names);
        texture2DTileMap.generateTexture();
        texture2DTileMap.load(listener,loadStart,loadEnd);
        this.getTexture2DTileMapContainer().set(id, texture2DTileMap);
        return texture2DTileMap;
    }

    public Texture2D[] loadBatch(TextureLoadingConfig[] cfgs, int threads, TaskProgressUpdateListener listener, int loadStart, int loadEnd) {
        ExecutorService service = Executors.newFixedThreadPool(threads);
        Texture2D[] textures = new Texture2D[cfgs.length];
        ArrayQueue<Pair<ByteBuffer, Vector3i>> patched = new ArrayQueue<>();

        int receiveRequirementCount = 0;
        //allocate
        for (int i = 0; i < cfgs.length; i++) {
            if (cfgs[i] != null) {
                textures[i] = new Texture2D(cfgs[i].ms(), cfgs[i].mip());
                textures[i].generateTexture();
                receiveRequirementCount++;
            }
        }

        //load
        for (int i = 0; i < cfgs.length; i++) {
            int finalI = i;
            service.submit(() -> {
                if (cfgs[finalI] != null) {
                    TextureLoadingConfig cfg = cfgs[finalI];
                    BufferedImage img = ResourceManager.instance.getImage(cfg.path());
                    textures[finalI].width = img.getWidth();
                    textures[finalI].height = img.getHeight();
                    patched.add(new Pair<>(ImageUtil.getByteFromBufferedImage_RGBA(img), new Vector3i(img.getWidth(), img.getHeight(), textures[finalI].glId)));
                    listener.onProgressStageChanged("batchLoadingTexture:%s(%d/%d)".formatted(cfg.path(), finalI, cfgs.length));
                }
            });
        }

        //upload
        int previous = 0;
        long last = System.currentTimeMillis();
        while (previous < receiveRequirementCount) {
            List<Pair<ByteBuffer, Vector3i>> results = patched.pollAll(threads);
            for (Pair<ByteBuffer, Vector3i> patchResult : results) {
                previous++;
                int prog = (int) MathHelper.scale(previous, loadStart, loadEnd, 0, receiveRequirementCount);
                listener.onProgressChange(prog);
                if (System.currentTimeMillis() - last > 40) {
                    last = System.currentTimeMillis();
                    listener.refreshScreen();
                }
                GL11.glBindTexture(3553, patchResult.t2().z);
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, patchResult.t2().x, patchResult.t2().y, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, patchResult.t1());
            }
        }
        return textures;
    }
}

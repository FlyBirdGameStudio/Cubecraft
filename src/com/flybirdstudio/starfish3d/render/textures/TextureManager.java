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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TextureManager {
    private HashMap<String, Texture2D> texture2d = new HashMap<>();

    public Texture2D create2DTexture(String path, boolean multiSample, boolean mipmap) {
        if (!this.texture2d.containsKey(path)) {
            Texture2D object = new Texture2D(multiSample, mipmap);
            object.generateTexture();
            object.load(path);
            this.texture2d.put(path, object);
            return object;
        }
        return this.texture2d.get(path);
    }

    public Texture2D get2DTexture(String path) {
        return this.texture2d.get(path);
    }

    public void bind2dTexture(String path) {
        texture2d.get(path).bind();
    }

    public void unBind2dTexture(String path) {
        this.texture2d.get(path).unbind();
    }


    private HashMap<String, Texture2DArray> textureArray2d = new HashMap<>();

    public Texture2DArray create2DTextureArray(String id, boolean multiSample, boolean mipmap) {
        if (!this.texture2d.containsKey(id)) {
            Texture2DArray object = new Texture2DArray(multiSample, mipmap, 256, 16, 16);
            object.generateTexture();
            object.load(id);
            this.textureArray2d.put(id, object);
            return object;
        }
        return this.textureArray2d.get(id);
    }

    public Texture2DArray get2DArrayTexture(String path) {
        return this.textureArray2d.get(path);
    }

    public void bind2DArrayTexture(String path) {
        textureArray2d.get(path).bind();
    }

    public void unBind2DArrayTexture(String path) {
        this.texture2d.get(path).unbind();
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
                    listener.onProgressStageChanged("BatchingFontTexture:" + cfg.path());
                }
            });
        }

        //upload
        int previous = 0;
        while (previous < receiveRequirementCount) {
            listener.refreshScreen();
            List<Pair<ByteBuffer, Vector3i>> results = patched.pollAll(threads);
            for (Pair<ByteBuffer, Vector3i> patchResult : results) {
                previous++;
                int prog = (int) MathHelper.scale(previous, loadStart, loadEnd, 0, receiveRequirementCount);
                listener.onProgressChange(prog);
                GL11.glBindTexture(3553, patchResult.t2().z);
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, patchResult.t2().x, patchResult.t2().y, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, patchResult.t1());
            }
        }
        return textures;
    }
}

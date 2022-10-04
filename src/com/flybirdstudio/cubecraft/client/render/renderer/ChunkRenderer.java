package com.flybirdstudio.cubecraft.client.render.renderer;

import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.client.render.object.RenderChunk;
import com.flybirdstudio.cubecraft.client.render.object.RenderChunkPos;
import com.flybirdstudio.cubecraft.registery.Registry;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.entity.humanoid.Player;
import com.flybirdstudio.starfish3d.render.Camera;
import com.flybirdstudio.starfish3d.render.GLUtil;
import com.flybirdstudio.starfish3d.render.culling.ProjectionMatrixFrustum;
import com.flybirdstudio.starfish3d.render.multiThread.DrawCompile;
import com.flybirdstudio.starfish3d.render.multiThread.MultiRenderCompileService;
import com.flybirdstudio.starfish3d.render.textures.Texture2D;
import com.flybirdstudio.starfish3d.render.textures.TextureStateManager;
import com.flybirdstudio.util.ColorUtil;
import com.flybirdstudio.util.LogHandler;
import com.flybirdstudio.util.container.ArrayQueue;
import com.flybirdstudio.util.container.BufferBuilder;
import com.flybirdstudio.util.container.CollectionUtil;
import com.flybirdstudio.util.container.HashMapSet;
import com.flybirdstudio.util.math.MathHelper;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChunkRenderer extends IWorldRenderer {
    public Texture2D terrain = new Texture2D(false, true);
    public LogHandler logHandler = LogHandler.create("ChunkRenderer", "client");
    private final ProjectionMatrixFrustum frustum = new ProjectionMatrixFrustum(this.camera);
    public HashMapSet<RenderChunkPos, RenderChunk> chunks = new HashMapSet<>();
    public ArrayQueue<RenderChunkPos> updateQueue = new ArrayQueue<>();
    public MultiRenderCompileService<RenderChunk> updateService = new MultiRenderCompileService<>(GameSetting.instance.getValueAsInt("client.render.chunk.drawThread", 1));

    public ChunkRenderer(IWorld w, Player p, Camera c) {
        super(w, p, c);
        terrain.generateTexture();
        terrain.load("/resource/textures/blocks/terrain.png");
        TextureStateManager.setTextureMipMap(terrain,true);
        TextureStateManager.setTextureClamp(terrain,true);
    }

    public int allCount;
    public int visibleCount;
    public int updateCount;


    @Override
    public void render(float interpolationTime) {
        allCount = 0;
        visibleCount = 0;
        updateCount = 0;

        this.camera.setUpGlobalCamera();
        this.frustum.calculateFrustum();
        this.updateChunks();
        if (this.camera.isPositionChanged() || this.camera.isRotationChanged()) {
            this.checkForChunkAdd();
            CollectionUtil.iterateMap(this.chunks.map, (key, item) -> {
                item.visible=ChunkRenderer.this.camera.objectDistanceSmallerThan(new Vector3d(item.x * 16, item.y * 16, item.z * 16), GameSetting.instance.getValueAsInt("client.render.terrain.renderDistance", 4) * 16) && this.frustum.aabbVisible(RenderChunk.getAABBFromPos(item.getKey(), camera))&&item.isNotEmpty();
            });
        }
        this.drawChunks();
        updateCount = this.updateQueue.size();
    }

    private void drawChunks() {
        GLUtil.enableBlend();
        GLUtil.enableAA();
        int d=GameSetting.instance.getValueAsInt("client.render.terrain.renderDistance", 114514);
        GLUtil.setupFog(d*d, BufferBuilder.from(ColorUtil.int1Float1ToFloat4(world.getWorldInfo().fogColor(),1)));
        GL11.glEnable(GL11.GL_FOG);
        Registry.getTextureManager().getTexture2DTileMapContainer().bind("cubecraft:terrain");
        CollectionUtil.iterateMap(this.chunks.map, (key, item) -> {
            if (item.visible) {
                GL11.glPushMatrix();
                this.camera.setupObjectCamera(new Vector3d(new Vector3d(item.x * 16, item.y * 16, item.z * 16)));
                item.render();
                visibleCount++;
                GL11.glPopMatrix();
            }
            allCount++;
        });
        Registry.getTextureManager().getTexture2DTileMapContainer().unbind("cubecraft:terrain");
        this.logHandler.checkGLError("draw_chunks");
        GL11.glDisable(GL11.GL_FOG);
    }

    private void updateChunks() {
        this.updateQueue.removeIf(
                c -> !this.camera.objectDistanceSmallerThan(new Vector3d(c.x() * 16, c.y() * 16, c.z() * 16), GameSetting.instance.getValueAsInt("client.render.terrain.renderDistance", 4) * 16)
                        && !this.frustum.aabbVisible(RenderChunk.getAABBFromPos(c, this.camera))
        );

        int maxUPD = GameSetting.instance.getValueAsInt("client.render.chunk.maxUpdate", 2);

        //offer update
        if (updateQueue.size() >= maxUPD) {
            List<RenderChunkPos> updatePositionList = this.updateQueue.pollAll(maxUPD);
            for (RenderChunkPos pos : updatePositionList) {
                RenderChunk chunk = this.chunks.get(pos);
                if (chunk == null) {
                    chunk = new RenderChunk(world, pos.x(), pos.y(), pos.z());
                    this.chunks.add(chunk);
                }
                this.updateService.startDrawing(chunk);
            }
        }

        //receive chunk update and draw

        logHandler.checkGLError("pre_draw");
        if (this.updateService.getResultSize() > 0) {
            for (DrawCompile d : this.updateService.get()) {
                if (d != null) {
                    d.draw();
                }
            }
        }
        logHandler.checkGLError("post_draw");

    }

    //try to add chunk in distance but not exist
    public void checkForChunkAdd() {
        int d = GameSetting.instance.getValueAsInt("client.render.terrain.renderDistance", 4);
        ArrayList<RenderChunkPos> adds = new ArrayList<>();
        long playerCX = (long) (this.camera.getPosition().x / 16);
        long playerCZ = (long) (this.camera.getPosition().z / 16);
        long playerCY = (long) (this.camera.getPosition().y / 16);
        for (long cx = playerCX - 1 - d; cx <= playerCX + d; cx++) {
            for (long cz = playerCZ - 1 - d; cz <= playerCZ + d; cz++) {
                for (long cy = playerCY - 1 - d; cy <= playerCY + d; cy++) {
                    RenderChunkPos p = new RenderChunkPos(cx, cy, cz);
                    if (
                            camera.objectDistanceSmallerThan(new Vector3d(cx * 16, cy * 16, cz * 16), d * 16) &&
                                    this.frustum.aabbVisible(RenderChunk.getAABBFromPos(p, this.camera))
                    ) {
                        adds.add(p);
                    }
                }
            }
        }
        adds.sort((o1, o2) -> -Double.compare(o1.distanceTo(player), o2.distanceTo(player)));
        adds.removeIf(renderChunkPos -> chunks.contains(renderChunkPos));
        for (RenderChunkPos p : adds) {
            updateQueue.add(p);
        }

        Iterator<RenderChunk> iterator = this.chunks.map.values().iterator();
        while (iterator.hasNext()) {
            RenderChunk c = iterator.next();
            if (!this.camera.objectDistanceSmallerThan(new Vector3d(c.x * 16, c.y * 16, c.z * 16), GameSetting.instance.getValueAsInt("client.render.chunk.loadingDistance", 4) * 16)) {
                c.destroy();
                iterator.remove();
            }
        }
    }

    public void setUpdate(long x, long y, long z) {
        RenderChunkPos pos = new RenderChunkPos(x, y, z);
        this.updateService.startDrawing(this.chunks.get(pos));
        updateQueue.remove(pos);
    }

    @Override
    public void blockChanged(long x, long y, long z) {
        if (MathHelper.getRelativePosInChunk(x, 16) == 0) {
            setUpdate(
                    MathHelper.getChunkPos(x, 16) - 1,
                    MathHelper.getChunkPos(y, 16),
                    MathHelper.getChunkPos(z, 16)
            );
        }
        if (MathHelper.getRelativePosInChunk(x, 16) == 15) {
            setUpdate(
                    MathHelper.getChunkPos(x, 16) + 1,
                    MathHelper.getChunkPos(y, 16),
                    MathHelper.getChunkPos(z, 16)
            );
        }
        if (MathHelper.getRelativePosInChunk(y, 16) == 0) {
            setUpdate(
                    MathHelper.getChunkPos(x, 16),
                    MathHelper.getChunkPos(y, 16) - 1,
                    MathHelper.getChunkPos(z, 16)
            );
        }
        if (MathHelper.getRelativePosInChunk(y, 16) == 15) {
            setUpdate(
                    MathHelper.getChunkPos(x, 16),
                    MathHelper.getChunkPos(y, 16) + 1,
                    MathHelper.getChunkPos(z, 16)
            );
        }
        if (MathHelper.getRelativePosInChunk(z, 16) == 0) {
            setUpdate(
                    MathHelper.getChunkPos(x, 16),
                    MathHelper.getChunkPos(y, 16),
                    MathHelper.getChunkPos(z, 16) - 1
            );
        }
        if (MathHelper.getRelativePosInChunk(z, 16) == 15) {
            setUpdate(
                    MathHelper.getChunkPos(x, 16),
                    MathHelper.getChunkPos(y, 16),
                    MathHelper.getChunkPos(z, 16) + 1
            );
        }
        setUpdate(
                MathHelper.getChunkPos(x, 16),
                MathHelper.getChunkPos(y, 16),
                MathHelper.getChunkPos(z, 16)
        );
    }
}




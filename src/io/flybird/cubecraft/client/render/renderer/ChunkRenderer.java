package io.flybird.cubecraft.client.render.renderer;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.client.render.model.RenderType;
import io.flybird.cubecraft.client.render.object.RenderChunk;
import io.flybird.cubecraft.client.render.object.RenderChunkPos;
import io.flybird.cubecraft.event.block.BlockChangeEvent;
import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.starfish3d.render.Camera;
import io.flybird.starfish3d.render.GLUtil;
import io.flybird.starfish3d.render.culling.ProjectionMatrixFrustum;
import io.flybird.starfish3d.render.multiThread.*;
import io.flybird.starfish3d.render.textures.Texture2D;
import io.flybird.util.ColorUtil;
import io.flybird.util.LogHandler;
import io.flybird.util.container.ArrayQueue;
import io.flybird.util.container.HashMapSet;
import io.flybird.util.event.EventHandler;
import io.flybird.util.event.EventListener;
import io.flybird.util.math.MathHelper;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChunkRenderer extends IWorldRenderer implements EventListener {
    public Texture2D terrain = new Texture2D(false, true);
    public LogHandler logHandler = LogHandler.create("ChunkRenderer", "game");
    private final ProjectionMatrixFrustum frustum = new ProjectionMatrixFrustum(this.camera);
    public HashMapSet<RenderChunkPos, RenderChunk> chunks = new HashMapSet<>();
    public ArrayQueue<RenderChunkPos> updateQueue = new ArrayQueue<>();

    public ArrayList<RenderChunk> callListAlpha = new ArrayList<>();

    public ArrayList<RenderChunk> callListTransParent = new ArrayList<>();

    public IDrawService<RenderChunk> updateService;

    public ChunkRenderer(IWorld w, Player p, Camera c, GameSetting setting) {
        super(w, p, c, setting);
        if (GameSetting.instance.getValueAsBoolean("client.render.terrain.async_chunk_compile", false)) {
            this.updateService = new AsyncRenderCompileService<>();
        } else {
            this.updateService = new MultiRenderCompileService<>("cubecraft:client_chunk", GameSetting.instance.getValueAsInt("client.render.chunk.drawThread", 1));
        }
        w.getEventBus().registerEventListener(this);
    }

    public int allCount;
    public int visibleCount;
    public int transVisibleCount;
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
        }
        this.drawChunks();

        updateCount = this.updateQueue.size();
        allCount = chunks.size();
    }

    private void drawChunks() {
        transVisibleCount = 0;
        visibleCount = 0;
        GLUtil.enableBlend();
        GLUtil.enableAA();
        int d = GameSetting.instance.getValueAsInt("client.render.terrain.renderDistance", 4);
        if (GameSetting.instance.getValueAsBoolean("client.render.fog", true)) {
            GL11.glEnable(GL11.GL_FOG);
            GLUtil.setupFog(d * d, ColorUtil.int1Float1ToFloat4(world.getWorldInfo().fogColor(), 1));
        }
        Registry.getTextureManager().getTexture2DTileMapContainer().bind("cubecraft:terrain");

        this.drawChunk(RenderType.ALPHA, this.callListAlpha, d);

        GL11.glDepthMask(false);
        this.drawChunk(RenderType.TRANSPARENT, this.callListTransParent, d);
        GL11.glDepthMask(true);

        Registry.getTextureManager().getTexture2DTileMapContainer().unbind("cubecraft:terrain");
        this.logHandler.checkGLError("draw_chunks");
        GL11.glDisable(GL11.GL_FOG);
    }

    public void drawChunk(RenderType type, List<RenderChunk> callList, int dist) {
        for (RenderChunk chunk : callList) {
            if (
                    this.camera.objectDistanceSmallerThan(chunk.getKey().clipToWorldPosition(), dist * 16)
                            && this.frustum.aabbVisible(chunk.getVisibleArea(this.camera))
                            && chunk.isFilled(type)
            ) {
                GL11.glPushMatrix();
                this.camera.setupObjectCamera(new Vector3d(new Vector3d(chunk.x * 16, chunk.y * 16, chunk.z * 16)));
                chunk.render(type);
                visibleCount++;
                GL11.glPopMatrix();
            }
        }
    }

    private void updateChunks() {
        this.updateQueue.removeIf(
                c -> !this.camera.objectDistanceSmallerThan(new Vector3d(c.x() * 16, c.y() * 16, c.z() * 16), GameSetting.instance.getValueAsInt("client.render.terrain.renderDistance", 4) * 16)
                        && !this.frustum.aabbVisible(RenderChunk.getAABBFromPos(c, this.camera))
        );

        int maxUPD = GameSetting.instance.getValueAsInt("client.render.chunk.maxUpdate", 2);

        //offer update
        if (updateQueue.size() > 0) {
            List<RenderChunkPos> updatePositionList = this.updateQueue.pollAll(this.updateQueue.size());
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

        //draw available compile
        logHandler.checkGLError("pre_draw");
        for (int i = 0; i < (maxUPD); i++) {
            if (this.updateService.getResultSize() > 0) {
                DrawCompile compile = this.updateService.getAvailableCompile();
                if (compile != null) {
                    compile.draw();
                }
            }
        }

        //check all compile and set call list
        while (this.updateService.getAllResultSize() > 0) {
            IDrawCompile compile = this.updateService.getAllCompile();
            if (compile != null) {
                RenderChunk chunk = ((RenderChunk) compile.getObject());
                this.checkCallList(chunk, RenderType.ALPHA, this.callListAlpha);
                this.checkCallList(chunk, RenderType.TRANSPARENT, this.callListTransParent);
            }
        }
        logHandler.checkGLError("post_draw");
    }

    public void checkCallList(RenderChunk chunk, RenderType type, List<RenderChunk> callList) {
        if (chunk.isFilled(type)) {
            if (!callList.contains(chunk)) {
                callList.add(chunk);
            }
        } else {
            callList.remove(chunk);
        }
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

    @EventHandler
    public void blockChanged(BlockChangeEvent e) {
        long x = e.x(), y = e.y(), z = e.z();
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

    public void refresh() {
        this.chunks.clear();
        this.checkForChunkAdd();
    }
}
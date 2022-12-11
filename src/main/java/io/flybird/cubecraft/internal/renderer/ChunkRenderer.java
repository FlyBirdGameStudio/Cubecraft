package io.flybird.cubecraft.internal.renderer;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.client.render.model.RenderType;
import io.flybird.cubecraft.client.render.object.*;
import io.flybird.cubecraft.client.render.renderer.IWorldRenderer;
import io.flybird.cubecraft.client.render.renderer.LevelRenderer;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.cubecraft.world.event.block.BlockChangeEvent;
import io.flybird.starfish3d.platform.Window;
import io.flybird.starfish3d.render.Camera;
import io.flybird.starfish3d.render.GLUtil;
import io.flybird.starfish3d.render.culling.ProjectionMatrixFrustum;
import io.flybird.starfish3d.render.multiThread.*;
import io.flybird.starfish3d.render.textures.Texture2D;
import io.flybird.util.container.CollectionUtil;
import io.flybird.util.container.keymap.KeyMap;
import io.flybird.util.event.EventHandler;
import io.flybird.util.event.EventListener;
import io.flybird.util.logging.LogHandler;
import io.flybird.util.math.MathHelper;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChunkRenderer extends IWorldRenderer implements EventListener {
    public Texture2D terrain = new Texture2D(false, true);
    public LogHandler logHandler = LogHandler.create("Client/ChunkRenderer");
    private final ProjectionMatrixFrustum frustum = new ProjectionMatrixFrustum(this.camera);
    public KeyMap<RenderChunkPos, RenderChunk> chunks = new KeyMap<>();
    public ArrayList<RenderChunk> callListAlpha = new ArrayList<>();
    public ArrayList<RenderChunk> callListTransParent = new ArrayList<>();

    public IDrawService<RenderChunk> updateService;

    public int allCount;
    public int visibleCount;
    public int transVisibleCount;
    public int updateCount;

    public ChunkRenderer(Window window, IWorld world, Player player, Camera cam, GameSetting setting) {
        super(window, world, player, cam, setting);
        world.getEventBus().registerEventListener(this);
        if (setting.getValueAsBoolean("client.render.terrain.async_compile", false)) {
            this.updateService = new AsyncRenderCompileService<>();
        } else {
            this.updateService = new MultiRenderCompileService<>(setting.getValueAsInt("client.render.terrain.draw_thread", 1));
        }
    }


    @Override
    public void render(float interpolationTime) {
        this.camera.setUpGlobalCamera(this.window);
        this.frustum.calculateFrustum();
        this.updateChunks();
        if (this.camera.isPositionChanged() || this.camera.isRotationChanged()) {
            this.checkForChunkAdd();
        }
        this.drawChunks();

        Registries.DEBUG_INFO.putI("cubecraft:chunk_render/all",chunks.size());

        Registries.DEBUG_INFO.putI("cubecraft:chunk_render/update",this.updateService.getCache().size());
    }

    private void drawChunks() {
        transVisibleCount = 0;
        visibleCount = 0;
        int d = setting.getValueAsInt("client.render.terrain.renderDistance", 4);

        LevelRenderer.setRenderState(this.setting, world);

        AtomicInteger counter=new AtomicInteger();

        Registries.TEXTURE.getTexture2DTileMapContainer().bind("cubecraft:terrain");
        this.drawChunk(RenderType.ALPHA, this.callListAlpha, d,counter);
        GL11.glDepthMask(false);
        Registries.DEBUG_INFO.putI("cubecraft:chunk_render/visible_alpha",counter.get());
        counter.set(0);
        this.drawChunk(RenderType.TRANSPARENT, this.callListTransParent, d,counter);
        Registries.DEBUG_INFO.putI("cubecraft:chunk_render/visible_transparent",counter.get());
        GL11.glDepthMask(true);

        Registries.TEXTURE.getTexture2DTileMapContainer().unbind("cubecraft:terrain");

        GLUtil.checkGLError("draw_chunks");

        LevelRenderer.closeState(this.setting);
    }

    public void drawChunk(RenderType type, List<RenderChunk> callList, int dist, AtomicInteger counter) {
        for (RenderChunk chunk : callList) {
            if (
                    this.camera.objectDistanceSmallerThan(chunk.getKey().clipToWorldPosition(), dist * 16)
                            && this.frustum.aabbVisible(chunk.getVisibleArea(this.camera))
                            && chunk.isFilled(type)
            ) {
                GL11.glPushMatrix();
                this.camera.setupObjectCamera(new Vector3d(new Vector3d(chunk.x * 16, chunk.y * 16, chunk.z * 16)));
                chunk.render(type);
                counter.addAndGet(1);
                GL11.glPopMatrix();
            }
        }
    }

    private void updateChunks() {
        int d = setting.getValueAsInt("client.render.terrain.renderDistance", 4);

        if (this.updateService.getCache().size() > 0) {
            try {
                this.updateService.getCache().sort((o1, o2) -> {
                    if (o1 == null) {
                        return -1;
                    }
                    if (o2 == null) {
                        return 1;
                    }
                    if (o1 == o2) {
                        return 0;
                    }
                    if (!frustum.aabbVisible(o2.getVisibleArea(camera))) {
                        return 1;
                    }
                    if (!frustum.aabbVisible(o1.getVisibleArea(camera))) {
                        return -1;
                    }
                    return -Double.compare(o1.distanceTo(player), o2.distanceTo(player));
                });
                this.updateService.getCache().removeIf(c -> {
                    if (c == null) {
                        return true;
                    }
                    return !camera.objectDistanceSmallerThan(c.getKey().clipToWorldPosition(), d * 16);
                });
            }catch (Exception ignored) {}
        }

        //draw available compile
        GLUtil.checkGLError("pre_draw");
        for (int i = 0; i < (this.setting.getValueAsInt("client.render.chunk.maxUpdate", 2)); i++) {
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
        GLUtil.checkGLError("post_draw");
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
        ArrayList<RenderChunkPos> adds = new ArrayList<>();
        int d = this.setting.getValueAsInt("client.render.terrain.renderDistance", 4);
        long playerCX = (long) (this.camera.getPosition().x / 16);
        long playerCZ = (long) (this.camera.getPosition().z / 16);
        long playerCY = (long) (this.camera.getPosition().y / 16);
        for (long cx = playerCX - 1 - d; cx <= playerCX + d; cx++) {
            for (long cz = playerCZ - 1 - d; cz <= playerCZ + d; cz++) {
                for (long cy = playerCY - 1 - d; cy <= playerCY + d; cy++) {
                    RenderChunkPos pos = new RenderChunkPos(cx, cy, cz);
                    if (this.frustum.aabbVisible(RenderChunk.getAABBFromPos(pos, this.camera))) {
                        RenderChunk chunk = this.chunks.get(pos);
                        if (chunk == null) {
                            chunk = new RenderChunk(world, pos.x(), pos.y(), pos.z());
                            this.chunks.add(chunk);
                            adds.add(pos);
                        }
                    }
                }
            }
        }
        for (RenderChunkPos p : adds) {
            RenderChunk chunk = this.chunks.get(p);
            this.updateService.startDrawing(chunk);
        }


        Iterator<RenderChunk> iterator = this.chunks.map.values().iterator();
        while (iterator.hasNext()) {
            RenderChunk c = iterator.next();
            if (!this.camera.objectDistanceSmallerThan(new Vector3d(c.x * 16, c.y * 16, c.z * 16), this.setting.getValueAsInt("client.render.chunk.loadingDistance", 4) * 16)) {
                c.destroy();
                iterator.remove();
            }
        }
    }

    public void setUpdate(long x, long y, long z) {
        RenderChunkPos pos = new RenderChunkPos(x, y, z);
        this.updateService.startDrawing(this.chunks.get(pos));
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

    @Override
    public void refresh() {
        CollectionUtil.iterateMap(this.chunks.map, (key, item) -> {
            GL11.glPushMatrix();
            item.destroy();
            GL11.glPopMatrix();
        });
        this.callListAlpha.clear();
        this.callListTransParent.clear();
        this.chunks.clear();
        this.checkForChunkAdd();
    }
}
package com.flybirdstudio.cubecraft.client.render.object;

import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.client.render.model.RenderType;
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import com.flybirdstudio.cubecraft.registery.Registry;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.cubecraft.world.chunk.ChunkLoadLevel;
import com.flybirdstudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.flybirdstudio.cubecraft.world.entity.Entity;
import com.flybirdstudio.starfish3d.render.Camera;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;
import com.flybirdstudio.starfish3d.render.drawcall.IRenderCall;
import com.flybirdstudio.starfish3d.render.multiThread.DrawCompile;
import com.flybirdstudio.util.container.keyMap.KeyGetter;
import com.flybirdstudio.util.math.AABB;
import com.flybirdstudio.util.timer.Timer;
import org.lwjgl.opengl.GL11;

public class RenderChunk implements KeyGetter<RenderChunkPos>, IRenderObject {
    public long x, y, z;
    public IWorld world;
    private final IRenderCall renderList_terrain;
    private final IRenderCall renderList_transparent;
    public boolean visible;
    private boolean empty;

    public RenderChunk(IWorld w, long x, long y, long z) {
        this.world = w;
        this.x = x;
        this.y = y;
        this.z = z;

        boolean vbo=GameSetting.instance.getValueAsBoolean("client.render.terrain.useVBO",false);
        this.renderList_terrain=IRenderCall.create(vbo);
        this.renderList_terrain.allocate();
        this.renderList_transparent=IRenderCall.create(vbo);
        this.renderList_transparent.allocate();
    }


    //render
    @Override
    public void render() {
        this.renderList_terrain.call();
        GL11.glDepthMask(false);
        this.renderList_transparent.call();
        GL11.glDepthMask(true);
    }

    @Override
    public DrawCompile[] compile() {
        Timer.startTiming();
        world.loadChunkAndNear(this.x, this.y, this.z, new ChunkLoadTicket(ChunkLoadLevel.None_TICKING, 10));
        VertexArrayBuilder builder = new VertexArrayBuilder(131072);
        this.empty = true;
        builder.begin();
        this.compileBlocks(RenderType.ALPHA, builder);
        builder.end();
        VertexArrayBuilder builder2 = new VertexArrayBuilder(131072);
        builder2.begin();
        this.compileBlocks(RenderType.TRANSPARENT, builder2);
        builder2.end();
        return new DrawCompile[]{new DrawCompile(renderList_terrain, builder), new DrawCompile(renderList_transparent, builder2)};
    }

    private void compileBlocks(RenderType renderType, VertexArrayBuilder builder) {
        for (long cx = 0; cx < 16; ++cx) {
            for (long cy = 0; cy < 16; ++cy) {
                for (long cz = 0; cz < 16; ++cz) {
                    BlockState bs = world.getBlockState(cx + x * 16, cy + y * 16, cz + z * 16);
                    IBlockRenderer renderer = Registry.getBlockRendererMap().get(bs.getId());
                    if (renderer != null) {
                        this.empty = false;
                        renderer.render(bs, world, renderType, cx, cy, cz, cx + x * 16, cy + y * 16, cz + z * 16, builder);
                    }
                }
            }
        }
    }


    //pos
    @Override
    public RenderChunkPos getKey() {
        return new RenderChunkPos(this.x, this.y, this.z);
    }

    @Override
    public final double distanceTo(Entity target) {
        double x = Math.abs(target.x - this.x * 16);
        double y = Math.abs(target.y - this.y * 16);
        double z = Math.abs(target.z - this.z * 16);
        return x * y * z;
    }

    //others
    public void destroy() {
        this.renderList_terrain.free();
        this.renderList_transparent.free();
    }

    public boolean isNotEmpty() {
        return !empty;
    }

    public static AABB getAABBFromPos(RenderChunkPos renderChunkPos, Camera camera) {
        return new AABB(
                (renderChunkPos.x() * 16 - camera.getPosition().x),
                (renderChunkPos.y() * 16 - camera.getPosition().y),
                (renderChunkPos.z() * 16 - camera.getPosition().z),
                (renderChunkPos.x() * 16 + 16 - camera.getPosition().x),
                (renderChunkPos.y() * 16 + 16 - camera.getPosition().y),
                (renderChunkPos.z() * 16 + 16 - camera.getPosition().z)
        );
    }
}

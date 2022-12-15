package io.flybird.cubecraft.client.render.object;

import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.render.IRenderType;
import io.flybird.cubecraft.client.render.model.RenderType;
import io.flybird.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.cubecraft.world.chunk.ChunkLoadLevel;
import io.flybird.cubecraft.world.chunk.ChunkLoadTicket;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.starfish3d.render.Camera;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.drawcall.IRenderCall;
import io.flybird.starfish3d.render.multiThread.DrawCompile;
import io.flybird.starfish3d.render.multiThread.EmptyDrawCompile;
import io.flybird.starfish3d.render.multiThread.IDrawCompile;
import io.flybird.util.container.keymap.KeyGetter;
import io.flybird.util.math.AABB;
import io.flybird.util.math.MathHelper;

public class RenderChunk implements KeyGetter<RenderChunkPos>, IRenderObject {
    public final long x;
    public final long y;
    public final long z;
    public final IWorld world;
    private final IRenderCall renderList_terrain;
    private final IRenderCall renderList_transparent;
    private boolean isAlphaFilled;
    private boolean isTransparentFilled;

    public RenderChunk(IWorld w, long x, long y, long z) {
        this.world = w;
        this.x = x;
        this.y = y;
        this.z = z;

        boolean vbo = Registries.CLIENT.getGameSetting().getValueAsBoolean("client.render.terrain.useVBO", false);
        this.renderList_terrain = IRenderCall.create(vbo);
        this.renderList_terrain.allocate();
        this.renderList_transparent = IRenderCall.create(vbo);
        this.renderList_transparent.allocate();
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

    //render
    @Override
    public void render(IRenderType type) {
        switch (((RenderType) type)) {
            case ALPHA -> this.renderList_terrain.call();
            case TRANSPARENT -> this.renderList_transparent.call();
        }
    }

    public boolean isFilled(IRenderType type) {
        return switch (((RenderType) type)) {
            case ALPHA -> this.isAlphaFilled;
            case TRANSPARENT -> this.isTransparentFilled;
        };
    }

    @Override
    public IDrawCompile[] compile() {
        //todo:fuck you!

        VertexArrayBuilder builder = new VertexArrayBuilder(131072);
        builder.begin();
        this.isAlphaFilled = this.compileBlocks(RenderType.ALPHA, builder);
        builder.end();

        VertexArrayBuilder builder2 = new VertexArrayBuilder(131072);
        builder2.begin();
        this.isTransparentFilled = this.compileBlocks(RenderType.TRANSPARENT, builder2);
        builder2.end();

        return new IDrawCompile[]{
                this.isAlphaFilled ? new DrawCompile(renderList_terrain, builder, this) : new EmptyDrawCompile(this),
                this.isTransparentFilled ? new DrawCompile(renderList_transparent, builder2, this) : new EmptyDrawCompile(this)
        };
    }

    private boolean compileBlocks(RenderType renderType, VertexArrayBuilder builder) {
        for (long cx = 0; cx < 16; ++cx) {
            for (long cy = 0; cy < 16; ++cy) {
                this.world.loadChunkAndNear(this.x, MathHelper.getChunkPos(this.y, 8), this.z, new ChunkLoadTicket(ChunkLoadLevel.None_TICKING, 10));
                for (long cz = 0; cz < 16; ++cz) {
                    if (!world.isAllNearMatch(cx + x * 16, cy + y * 16, cz + z * 16, blockState -> blockState.getBlock().isSolid())) {
                        BlockState bs = world.getBlockState(cx + x * 16, cy + y * 16, cz + z * 16);
                        IBlockRenderer renderer = ClientRegistries.BLOCK_RENDERER.get(bs.getId());
                        if (renderer != null) {
                            renderer.renderBlock(bs, renderType, world, cx, cy, cz, cx + x * 16, cy + y * 16, cz + z * 16, builder);
                        }
                    }
                }
            }
        }
        return builder.getVertexCount() > 0;
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
        double d = x * y * z;
        if (d < x) {
            return x;
        }
        if (d < y) {
            return y;
        }
        return Math.max(d, z);
    }

    //others
    public void destroy() {
        this.renderList_terrain.free();
        this.renderList_transparent.free();
    }

    public AABB getVisibleArea(Camera camera) {
        return getAABBFromPos(this.getKey(), camera);
    }
}

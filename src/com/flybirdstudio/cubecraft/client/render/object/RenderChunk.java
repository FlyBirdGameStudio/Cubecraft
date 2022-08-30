package com.flybirdstudio.cubecraft.client.render.object;

import com.flybirdstudio.cubecraft.client.render.model.RenderType;
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import com.flybirdstudio.cubecraft.registery.Registery;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.cubecraft.world.chunk.ChunkLoadLevel;
import com.flybirdstudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.flybirdstudio.cubecraft.world.entity.Entity;
import com.flybirdstudio.starfish3d.render.Camera;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;
import com.flybirdstudio.starfish3d.render.multiThread.DrawCompile;
import com.flybirdstudio.util.container.keyMap.KeyGetter;
import com.flybirdstudio.util.math.AABB;
import org.lwjgl.opengl.GL11;

public class RenderChunk implements KeyGetter<RenderChunkPos>, IRenderObject {
    public long x, y, z;
    public IWorld world;
    private final int renderList_terrain = GL11.glGenLists(1);
    private final int renderList_transparent = GL11.glGenLists(1);
    public boolean visible;
    private boolean empty;

    public RenderChunk(IWorld w, long x, long y, long z) {
        this.world = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }


    //render
    @Override
    public void render() {
        GL11.glCallList(this.renderList_terrain);
        GL11.glDepthMask(false);
        GL11.glCallList(this.renderList_transparent);
        GL11.glDepthMask(true);
    }

    @Override
    public DrawCompile[] compile() {
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
                    IBlockRenderer renderer = Registery.getBlockRendererMap().get(bs.getId());
                    if (renderer != null) {
                        this.empty = false;
                        renderer.render(bs, world, RenderType.ALPHA, cx, cy, cz, cx + x * 16, cy + y * 16, cz + z * 16, builder);
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
        GL11.glDeleteLists(this.renderList_terrain, 1);
        GL11.glDeleteLists(this.renderList_transparent, 1);
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

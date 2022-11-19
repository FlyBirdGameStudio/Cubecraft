package io.flybird.cubecraft.client.render.object;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.client.render.IRenderType;
import io.flybird.cubecraft.client.render.model.RenderType;
import io.flybird.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import io.flybird.cubecraft.register.Registry;
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
import io.flybird.util.container.keyMap.KeyGetter;
import io.flybird.util.math.AABB;
import org.lwjgl.opengl.GL11;

public class RenderChunk implements KeyGetter<RenderChunkPos>, IRenderObject {
    public long x, y, z;
    public IWorld world;
    private final IRenderCall renderList_terrain;
    private boolean isAlphaFilled;

    private final IRenderCall renderList_transparent;
    private boolean isTransparentFilled;

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

    public boolean isAlphaFilled() {
        return isAlphaFilled;
    }

    public boolean isFilled(){
        return isAlphaFilled||isTransparentFilled;
    }

    //render
    @Override
    public void render(IRenderType type) {
        switch (((RenderType) type)){
            case ALPHA -> this.renderList_terrain.call();
            case TRANSPARENT -> this.renderList_transparent.call();
        }
    }

    public boolean isFilled(IRenderType type){
        return switch (((RenderType) type)){
            case ALPHA -> this.isAlphaFilled;
            case TRANSPARENT -> this.isTransparentFilled;
        };
    }


    public void renderTransParent(){
        if (this.isTransparentFilled){
            this.renderList_transparent.call();
        }
    }

    @Override
    public IDrawCompile[] compile() {
        if(GameSetting.instance.getValueAsBoolean("client.render.terrain.over_distance_load",false)){
            this.world.loadChunkAndNear(this.x,this.y,this.z,new ChunkLoadTicket(ChunkLoadLevel.None_TICKING,20));
        }
        VertexArrayBuilder builder = new VertexArrayBuilder(131072);
        builder.begin();
        this.isAlphaFilled=this.compileBlocks(RenderType.ALPHA, builder);
        builder.end();

        VertexArrayBuilder builder2 = new VertexArrayBuilder(131072);
        builder2.begin();
        this.isTransparentFilled=this.compileBlocks(RenderType.TRANSPARENT, builder2);
        builder2.end();

        return new IDrawCompile[]{
                this.isAlphaFilled?new DrawCompile(renderList_terrain, builder,this):new EmptyDrawCompile(this),
                this.isTransparentFilled?new DrawCompile(renderList_transparent, builder2,this):new EmptyDrawCompile(this)
        };
    }

    private boolean compileBlocks(RenderType renderType, VertexArrayBuilder builder) {
        for (long cx = 0; cx < 16; ++cx) {
            for (long cy = 0; cy < 16; ++cy) {
                for (long cz = 0; cz < 16; ++cz) {
                    BlockState bs = world.getBlockState(cx + x * 16, cy + y * 16, cz + z * 16);
                    IBlockRenderer renderer = Registry.getBlockRendererMap().get(bs.getId());
                    if (renderer != null) {
                        renderer.renderBlock(bs,renderType,world,cx,cy,cz,cx + x * 16, cy + y * 16, cz + z * 16,builder);
                    }
                }
            }
        }
        return builder.getVertexCount()>0;
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

    public AABB getVisibleArea(Camera camera) {
        return getAABBFromPos(this.getKey(),camera);
    }
}

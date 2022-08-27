package com.sunrisestudio.cubecraft.client.render.object;

import com.sunrisestudio.cubecraft.client.render.model.RenderType;
import com.sunrisestudio.cubecraft.world.World;
import com.sunrisestudio.cubecraft.world.chunk.ChunkLoadLevel;
import com.sunrisestudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.grass3d.render.Camera;
import com.sunrisestudio.grass3d.render.draw.ChanneledVertexArrayBuilder;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;
import com.sunrisestudio.grass3d.render.multiThread.DrawCompile;
import com.sunrisestudio.util.container.keyMap.KeyGetter;
import com.sunrisestudio.util.math.AABB;
import org.lwjgl.opengl.GL11;

public class RenderChunk implements KeyGetter<RenderChunkPos>,IRenderObject{
    public long x,y,z;
    public World world;
    private final int renderList_terrain =GL11.glGenLists(1);
    private final int renderList_transparent=GL11.glGenLists(1);
    private boolean empty;

    public RenderChunk(World w, long x, long y, long z){
        this.world=w;
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public static AABB getAABBFromPos(RenderChunkPos renderChunkPos, Camera camera) {
        return new AABB(
                (renderChunkPos.x()*16-camera.getPosition().x),
                (renderChunkPos.y()*16-camera.getPosition().y),
                (renderChunkPos.z()*16-camera.getPosition().z),
                (renderChunkPos.x()*16+16-camera.getPosition().x),
                (renderChunkPos.y()*16+16-camera.getPosition().y),
                (renderChunkPos.z()*16+16-camera.getPosition().z)
        );
    }



    //-----update-----
    @Override
    public void render(){
        GL11.glCallList(this.renderList_terrain);
        GL11.glDepthMask(false);
        GL11.glCallList(this.renderList_transparent);
        GL11.glDepthMask(true);
    }

    @Override
    public DrawCompile[] compile() {
        world.loadChunkAndNear(this.x,this.y,this.z,new ChunkLoadTicket(ChunkLoadLevel.None_TICKING,10));
        IVertexArrayBuilder builder=new ChanneledVertexArrayBuilder(131072);
        builder.begin();
        this.empty=true;
        for (long cx = 0; cx < 16; ++cx) {
            for (long cy = 0; cy <16; ++cy) {
                for (long cz = 0; cz < 16; ++cz) {
                    if(world.getBlockState(cx+x*16,cy+y*16,cz+z*16).getId()!="cubecraft:air"){
                        this.empty=false;
                    }
                    world.getBlockState(cx+x*16,cy+y*16,cz+z*16).render(world,cx+x*16,cy+y*16,cz+z*16,cx,cy,cz,builder, RenderType.ALPHA);
                }
            }
        }
        builder.end();
        IVertexArrayBuilder builder2=new ChanneledVertexArrayBuilder(131072);
        builder2.begin();
        for (long cx = 0; cx < 16; ++cx) {
            for (long cy = 0; cy <16; ++cy) {
                for (long cz = 0; cz < 16; ++cz) {
                    world.getBlockState(cx+x*16,cy+y*16,cz+z*16).render(world,cx+x*16,cy+y*16,cz+z*16,cx,cy,cz,builder, RenderType.TRANSPARENT);
                }
            }
        }
        builder2.end();
        return new DrawCompile[]{
                new DrawCompile(renderList_terrain,builder),
                new DrawCompile(renderList_transparent,builder2),
        };
    }


//-----data-----
    @Override
    public RenderChunkPos getKey() {
        return new RenderChunkPos(this.x,this.y,this.z);
    }


//-----position-----

    @Override
    public final double distanceTo(Entity target) {
        double x = Math.abs(target.x - this.x * 16);
        double y = Math.abs(target.y - this.y * 16);
        double z = Math.abs(target.z - this.z * 16);
        return x*y*z;
    }

    public void destroy() {
        GL11.glDeleteLists(this.renderList_terrain,1);
        GL11.glDeleteLists(this.renderList_transparent,1);
    }

    public boolean isNotEmpty() {
        return !empty;
    }
}

package com.SunriseStudio.cubecraft.render.object;

import com.SunriseStudio.cubecraft.util.grass3D.render.culling.ICuller;
import com.SunriseStudio.cubecraft.util.grass3D.render.draw.ChanneledVertexArrayBuilder;
import com.SunriseStudio.cubecraft.util.grass3D.render.draw.IVertexArrayBuilder;
import com.SunriseStudio.cubecraft.util.collections.keyMap.KeyGetter;
import com.SunriseStudio.cubecraft.util.grass3D.render.draw.IVertexArrayUploader;
import com.SunriseStudio.cubecraft.world.entity._Entity;
import com.SunriseStudio.cubecraft.util.math.AABB;
import com.SunriseStudio.cubecraft.world.Level;
import com.SunriseStudio.cubecraft.world.block.Tile;
import org.lwjgl.opengl.GL11;

public class RenderChunk implements KeyGetter<RenderChunkPos>,IRenderObject{
    public long x,y,z;
    public Level world;

    private final AABB visibleArea;
    private final int renderList=GL11.glGenLists(2);

    public RenderChunk(Level w, long x, long y, long z){
        this.world=w;
        this.x=x;
        this.y=y;
        this.z=z;
        this.visibleArea=new AABB(x*16,y*16,z*16,x*16+16,y*16+16,z*16+16);
        this.compileVisibleArea();
    }


//-----update-----
    @Override
    public void render(){
        GL11.glCallList(this.renderList);
    }

    @Override
    public IVertexArrayBuilder compile() {
        IVertexArrayBuilder builder=new ChanneledVertexArrayBuilder(524288);
        builder.begin();
        for (long cx = 0; cx < 16; ++cx) {
            for (long cy = 0; cy <16; ++cy) {
                for (long cz = 0; cz < 16; ++cz) {
                    Tile renderTile = Tile.tiles[RenderChunk.this.world.getTile(cx+x*16, cy+y* 16L, cz+z*16 )];
                    if (renderTile != null){
                        renderTile.render(builder, RenderChunk.this.world, 0, cx, cy, cz,cx+x*16, cy+y*16, cz+z*16);
                    }
                }
            }
        }
        builder.end();
        return builder;
    }


//-----data-----
    @Override
    public int getList() {
        return this.renderList;
    }

    @Override
    public AABB getVisibleArea() {
        return this.visibleArea;
    }

    @Override
    public RenderChunkPos getKey() {
        return new RenderChunkPos(this.x,this.y,this.z);
    }


//-----position-----
    //@Override
    //public boolean equalsAnother(UUIDGetter<RenderChunkPos> uid) {
        //return this.getUID().compare(uid.getUID());
    //}


    @Override
    public final double distanceTo(_Entity target) {
        double x = Math.abs(target.x - this.x * 16);
        double y = Math.abs(target.y - this.y * 16);
        double z = Math.abs(target.z - this.z * 16);
        return x*y*z;
    }

    @Override
    public boolean isVisible(ICuller culler) {
        boolean visible;
        GL11.glPushMatrix();
        visible= culler.listVisible(this.renderList+1);
        GL11.glPopMatrix();
        return visible;
    }

    public void compileVisibleArea(){
        double x0 = 0;
        double x1 = 16;
        double y0 = 0;
        double y1 = 16;
        double z0 = 0;
        double z1 = 16;
        ChanneledVertexArrayBuilder vertexBuilder = new ChanneledVertexArrayBuilder(1000);
        vertexBuilder.begin();
        vertexBuilder.color(1,1,1,0.8f);
        vertexBuilder.vertex(x0, y0, z1);
        vertexBuilder.vertex(x0, y0, z0);
        vertexBuilder.vertex(x1, y0, z0);
        vertexBuilder.vertex(x1, y0, z1);

        vertexBuilder.vertex(x1, y1, z1);
        vertexBuilder.vertex(x1, y1, z0);
        vertexBuilder.vertex(x0, y1, z0);
        vertexBuilder.vertex(x0, y1, z1);

        vertexBuilder.vertex(x0, y1, z0);
        vertexBuilder.vertex(x1, y1, z0);
        vertexBuilder.vertex(x1, y0, z0);
        vertexBuilder.vertex(x0, y0, z0);

        vertexBuilder.vertex(x0, y1, z1);
        vertexBuilder.vertex(x0, y0, z1);
        vertexBuilder.vertex(x1, y0, z1);
        vertexBuilder.vertex(x1, y1, z1);

        vertexBuilder.vertex(x0, y1, z1);
        vertexBuilder.vertex(x0, y1, z0);
        vertexBuilder.vertex(x0, y0, z0);
        vertexBuilder.vertex(x0, y0, z1);

        vertexBuilder.vertex(x1, y0, z1);
        vertexBuilder.vertex(x1, y0, z0);
        vertexBuilder.vertex(x1, y1, z0);
        vertexBuilder.vertex(x1, y1, z1);

        vertexBuilder.vertex(x0, y1-0.001, z1);
        vertexBuilder.vertex(x0, y1-0.001, z0);
        vertexBuilder.vertex(x1, y1-0.001, z0);
        vertexBuilder.vertex(x1, y1-0.001, z1);

        vertexBuilder.vertex(x1, y0+0.001, z1);
        vertexBuilder.vertex(x1, y0+0.001, z0);
        vertexBuilder.vertex(x0, y0+0.001, z0);
        vertexBuilder.vertex(x0, y0+0.001, z1);

        vertexBuilder.vertex(x0, y1, z1-0.001);
        vertexBuilder.vertex(x1, y1, z1-0.001);
        vertexBuilder.vertex(x1, y0, z1-0.001);
        vertexBuilder.vertex(x0, y0, z1-0.001);

        vertexBuilder.vertex(x0, y1, z0+0.001);
        vertexBuilder.vertex(x0, y0, z0+0.001);
        vertexBuilder.vertex(x1, y0, z0+0.001);
        vertexBuilder.vertex(x1, y1, z0+0.001);

        vertexBuilder.vertex(x1-0.001, y1, z1);
        vertexBuilder.vertex(x1-0.001, y1, z0);
        vertexBuilder.vertex(x1-0.001, y0, z0);
        vertexBuilder.vertex(x1-0.001, y0, z1);

        vertexBuilder.vertex(x0+0.001, y0, z1);
        vertexBuilder.vertex(x0+0.001, y0, z0);
        vertexBuilder.vertex(x0+0.001, y1, z0);
        vertexBuilder.vertex(x0+0.001, y1, z1);

        vertexBuilder.end();

        GL11.glNewList(this.renderList+1,GL11.GL_COMPILE);
        IVertexArrayUploader.createNewPointedUploader().upload(vertexBuilder);
        GL11.glEndList();
    }


}

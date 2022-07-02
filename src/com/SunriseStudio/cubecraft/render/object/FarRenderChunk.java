package com.SunriseStudio.cubecraft.render.object;

/*public class FarRenderChunk implements IRenderObject {
    public boolean empty;
    public long x;
    public int y;
    public long z;
    public Level world;
    public int renderList;
    public AABB visibleArea;
    public int levelOfDetail=8;
    private final VertexArrayBuilder chunkBuilder=new VertexArrayBuilder(4096);

    public FarRenderChunk(Level w, long x, int y, long z){
        this.world=w;
        this.x=x;
        this.y=y;
        this.z=z;
        this.renderList= GL11.glGenLists(1);
        this.visibleArea=new AABB(x,y,z,x+16,y+16,z+16);
    }

    @Override
    public double distanceTo(Entity target) {
        double x = Math.abs(target.x - this.x * 16);
        double y = Math.abs(target.y - this.y * 16);
        double z = Math.abs(target.z - this.z * 16);
        return x*y*z;
    }


    @Override
    public void update(){
        this.chunkBuilder.begin();
        for (int cx = 0; cx < 16; ++cx) {
            for (int cy = 0; cy <16; ++cy) {
                for (int cz = 0; cz < 16; ++cz) {
                    Tile renderTile = Tile.tiles[FarRenderChunk.this.world.getTile(cx+x*16, cy+y*16, cz+z*16 )];
                    if (renderTile != null){
                        FarRenderChunk.this.empty = false;
                        renderTile.render(this.chunkBuilder, FarRenderChunk.this.world, 0, cx, cy, cz,cx+x*16, cy+y*16, cz+z*16);
                    }
                }
            }
        }
        this.chunkBuilder.end();
    }

    @Override
    public AABB getVisibleArea() {
        return this.visibleArea;
    }

    @Override
    public VertexArrayBuilder getVertexArrayBuilder() {
        return this.chunkBuilder;
    }

    @Override
    public int getRenderList() {
        return this.renderList;
    }

    public void render(double camX,double camY,double camZ){
        GL11.glPushMatrix();
        GL11.glTranslated(x*16-camX,y*16-camY,z*16-camZ);
        GL11.glCallList(this.renderList);
        GL11.glPopMatrix();
    }

}*/

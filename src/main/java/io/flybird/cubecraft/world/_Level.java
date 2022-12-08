package io.flybird.cubecraft.world;

import io.flybird.cubecraft.world.block.Tile;

@Deprecated
public class _Level {

    public boolean setTile(long x, int y, long z, int type) {
        return true;
    }
    public boolean isLit(long x, long y, long z) {
        return true;
    }
    public int getTile(long x, long y, long z){
        return 0;
    }
    public boolean isSolidTile(long x, long y, long z) {
        if(getTile(x,y,z)==0)return false;
        return Tile.tiles[getTile(x, y, z)].isSolid();
    }
}

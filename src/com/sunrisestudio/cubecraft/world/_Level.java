package com.sunrisestudio.cubecraft.world;

import com.sunrisestudio.cubecraft.world.block.Tile;
import com.sunrisestudio.cubecraft.world.entity._Entity;
import com.sunrisestudio.util.math.AABB;
import java.util.ArrayList;

@Deprecated
public class _Level {
    public boolean containsAnyLiquid(AABB box) {
        int x0 = (int)Math.floor(box.x0);
        int x1 = (int)Math.floor(box.x1 + 1.0f);
        int y0 = (int)Math.floor(box.y0);
        int y1 = (int)Math.floor(box.y1 + 1.0f);
        int z0 = (int)Math.floor(box.z0);
        int z1 = (int)Math.floor(box.z1 + 1.0f);
        for (int x = x0; x < x1; ++x) {
            for (int y = y0; y < y1; ++y) {
                for (int z = z0; z < z1; ++z) {
                    Tile tile = Tile.tiles[this.getTile(x, y, z)];
                    if (tile == null || tile.getLiquidType() <= 0) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsLiquid(AABB box, int liquidId) {
        long x0 = (long)Math.floor(box.x0);
        long x1 = (long)Math.floor(box.x1 + 1.0f);
        long y0 = (long)Math.floor(box.y0);
        long y1 = (long)Math.floor(box.y1 + 1.0f);
        long z0 = (long)Math.floor(box.z0);
        long z1 = (long)Math.floor(box.z1 + 1.0f);
        for (long x = x0; x < x1; ++x) {
            for (long y = y0; y < y1; ++y) {
                for (long z = z0; z < z1; ++z) {
                    Tile tile = Tile.tiles[this.getTile(x, y, z)];
                    if (tile == null || tile.getLiquidType() != liquidId) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean setTile(long x, int y, long z, int type) {
        return true;
    }
    public boolean setTileNoUpdate(long x, int y, long z, int type) {
        setTile(x,y,z,type);
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

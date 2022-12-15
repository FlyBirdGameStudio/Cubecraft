package io.flybird.cubecraft.world;


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
        return false;
    }
}

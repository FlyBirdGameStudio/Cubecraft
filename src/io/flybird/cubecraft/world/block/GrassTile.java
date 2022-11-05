package io.flybird.cubecraft.world.block;

import io.flybird.cubecraft.world._Level;

import java.util.Random;

@Deprecated
public class GrassTile
extends Tile {
    protected GrassTile(int id) {
        super(id);
        this.tex = 3;
        this.setTicking(true);
    }

    @Override
    protected int getTexture(int face) {
        if (face == 1) {
            return 0;
        }
        if (face == 0) {
            return 2;
        }
        return 3;
    }

    @Override
    public void tick(_Level world, int x, int y, int z, Random random) {
        if (random.nextInt(4) != 0) {
            return;
        }
        if (!world.isLit(x, y + 1, z)) {
            world.setTile(x, y, z, dirt.id);
        } else {
            for (int i = 0; i < 4; ++i) {
                int zt;
                int yt;
                int xt = x + random.nextInt(3) - 1;
                if (world.getTile(xt, yt = y + random.nextInt(5) - 3, zt = z + random.nextInt(3) - 1) != dirt.id || !world.isLit(xt, yt + 1, zt)) continue;
                world.setTile(xt, yt, zt, grass.id);
            }
        }
    }

}

package io.flybird.cubecraft.world.block;

import io.flybird.cubecraft.world._Level;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.util.math.AABB;

import java.util.Random;

@Deprecated
public class LiquidTile
extends Tile {
    protected int liquidType;
    protected int calmTileId;
    protected int tileId;
    protected int spreadSpeed = 1;

    protected LiquidTile(int id, int liquidType) {
        super(id);
        this.liquidType = liquidType;
        this.tex = 14;
        if (liquidType == 2) {
            this.tex = 30;
        }
        if (liquidType == 1) {
            this.spreadSpeed = 8;
        }
        if (liquidType == 2) {
            this.spreadSpeed = 2;
        }
        this.tileId = id;
        this.calmTileId = id + 1;
        float dd = 0f;
        this.setShape(0.0f, 0.0f - dd, 0.0f, 1.0f, 1.0f - dd, 1.0f);
        this.setTicking(true);
    }

    @Override
    public void tick(_Level world, int x, int y, int z, Random random) {
        this.updateWater(world, x, y, z, 0);
    }

    public boolean updateWater(_Level world, int x, int y, int z, int depth) {
        boolean hasChanged = false;
        while (world.getTile(x, --y, z) == 0) {
            boolean change = world.setTile(x, y, z, this.tileId);
            if (change) {
                hasChanged = true;
            }
            if (!change || this.liquidType == 2) break;
        }
        ++y;
        if (this.liquidType == 1 || !hasChanged) {
            hasChanged |= this.checkWater(world, x - 1, y, z, depth);
            hasChanged |= this.checkWater(world, x + 1, y, z, depth);
            hasChanged |= this.checkWater(world, x, y, z - 1, depth);
            hasChanged |= this.checkWater(world, x, y, z + 1, depth);
        }
        if (!hasChanged) {
            world.setTileNoUpdate(x, y, z, this.calmTileId);
        }
        return hasChanged;
    }

    private boolean checkWater(_Level world, int x, int y, int z, int depth) {
        boolean changed;
        boolean hasChanged = false;
        int type = world.getTile(x, y, z);
        if (type == 0 && (changed = world.setTile(x, y, z, this.tileId)) && depth < this.spreadSpeed) {
            hasChanged |= this.updateWater(world, x, y, z, depth + 1);
        }
        return hasChanged;
    }

    @Override
    protected boolean shouldRenderFace(_Level world, long x, long y, long z, int layer, int face) {
        return (!(world.getTile(x,y,z)==water.id))&&(!world.isSolidTile(x,y,z));
    }

    @Override
    public void renderFace(VertexArrayBuilder vertexBuilder, long x, long y, long z, int face) {
        super.renderFace(vertexBuilder, x, y, z, face);
        super.renderBackFace(vertexBuilder,x,y,z,face);
    }

    @Override
    public boolean mayPick() {
        return false;
    }

    @Override
    public AABB getAABB(long x, int y, long z) {
        return null;
    }

    @Override
    public boolean blocksLight() {
        return true;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public int getLiquidType() {
        return this.liquidType;
    }
}

package com.sunrisestudio.cubecraft.world.block;

import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;

import com.sunrisestudio.cubecraft.world.entity.particle.Particle;
import com.sunrisestudio.cubecraft.world.entity.particle.ParticleEngine;
import com.sunrisestudio.cubecraft.world._Level;
import com.sunrisestudio.util.math.AABB;

import java.util.Random;
@Deprecated
public class Tile {
    public static final Tile[] tiles = new Tile[256];
    public static final boolean[] shouldTick = new boolean[256];
    public static final Tile grass = new GrassTile(2);
    public static final Tile dirt = new Tile(3, 2);
    public static final Tile water = new LiquidTile(8, 1);
    public int tex;
    public final int id;
    protected double xx0;
    protected double yy0;
    protected double zz0;
    protected double xx1;
    protected double yy1;
    protected double zz1;

    protected Tile(int id) {
        Tile.tiles[id] = this;
        this.id = id;
        this.setShape(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    protected void setTicking(boolean tick) {
        Tile.shouldTick[this.id] = tick;
    }

    protected void setShape(double x0, double y0, double z0, double x1, double y1, double z1) {
        this.xx0 = x0;
        this.yy0 = y0;
        this.zz0 = z0;
        this.xx1 = x1;
        this.yy1 = y1;
        this.zz1 = z1;
    }

    protected Tile(int id, int tex) {
        this(id);
        this.tex = tex;
    }

    public void render(IVertexArrayBuilder vertexBuilder, _Level world, int layer, long x, long y, long z, long ax, long ay, long az) {
        byte c1 = -1;
        byte c2 = -52;
        byte c3 = -103;

        if (this.shouldRenderFace(world, ax, ay - 1, az, layer, 0)) {
            vertexBuilder.colorB(c1, c1, c1);
            this.renderFace(vertexBuilder, x, y, z, 0);
        }
        if (this.shouldRenderFace(world, ax, ay + 1, az, layer, 1)) {
            vertexBuilder.colorB(c1, c1, c1);
            this.renderFace(vertexBuilder, x, y, z, 1);
        }
        if (this.shouldRenderFace(world, ax, ay, az - 1, layer, 2)) {
            vertexBuilder.colorB(c2, c2, c2);
            this.renderFace(vertexBuilder, x, y, z, 2);
        }
        if (this.shouldRenderFace(world,ax, ay, az + 1, layer, 3)) {
            vertexBuilder.colorB(c2, c2, c2);
            this.renderFace(vertexBuilder, x, y, z, 3);
        }
        if (this.shouldRenderFace(world, ax - 1, ay, az, layer, 4)) {
            vertexBuilder.colorB(c3, c3, c3);
            this.renderFace(vertexBuilder, x, y, z, 4);
        }
        if (this.shouldRenderFace(world, ax + 1, ay, az, layer, 5)) {
            vertexBuilder.colorB(c3, c3, c3);
            this.renderFace(vertexBuilder, x, y, z, 5);
        }

    }

    protected boolean shouldRenderFace(_Level world, long x, long y, long z, int layer, int face) {
        return !world.isSolidTile(x, y, z);
    }

    protected int getTexture(int face) {
        return this.tex;
    }

    public void renderFace(IVertexArrayBuilder vertexBuilder, long x, long y, long z, int face) {
        int tex = this.getTexture(face);
        int xt = tex % 16 * 16;
        int yt = tex / 16 * 16;
        float u0 = (float)xt / 256.0f;
        float u1 = ((float)xt + 15.99f) / 256.0f;
        float v0 = (float)yt / 256.0f;
        float v1 = ((float)yt + 15.99f) / 256.0f;
        double x0 = (x + this.xx0);
        double x1 = (x + this.xx1);
        double y0 = (y + this.yy0);
        double y1 = (y + this.yy1);
        double z0 =(z + this.zz0);
        double z1 = (z + this.zz1);
        if (face == 0) {
            vertexBuilder.vertexUV(x0, y0, z1, u0, v1);
            vertexBuilder.vertexUV(x0, y0, z0, u0, v0);
            vertexBuilder.vertexUV(x1, y0, z0, u1, v0);
            vertexBuilder.vertexUV(x1, y0, z1, u1, v1);
            return;
        }
        if (face == 1) {
            vertexBuilder.vertexUV(x1, y1, z1, u1, v1);
            vertexBuilder.vertexUV(x1, y1, z0, u1, v0);
            vertexBuilder.vertexUV(x0, y1, z0, u0, v0);
            vertexBuilder.vertexUV(x0, y1, z1, u0, v1);
            return;
        }
        if (face == 2) {
            vertexBuilder.vertexUV(x0, y1, z0, u1, v0);
            vertexBuilder.vertexUV(x1, y1, z0, u0, v0);
            vertexBuilder.vertexUV(x1, y0, z0, u0, v1);
            vertexBuilder.vertexUV(x0, y0, z0, u1, v1);
            return;
        }
        if (face == 3) {
            vertexBuilder.vertexUV(x0, y1, z1, u0, v0);
            vertexBuilder.vertexUV(x0, y0, z1, u0, v1);
            vertexBuilder.vertexUV(x1, y0, z1, u1, v1);
            vertexBuilder.vertexUV(x1, y1, z1, u1, v0);
            return;
        }
        if (face == 4) {
            vertexBuilder.vertexUV(x0, y1, z1, u1, v0);
            vertexBuilder.vertexUV(x0, y1, z0, u0, v0);
            vertexBuilder.vertexUV(x0, y0, z0, u0, v1);
            vertexBuilder.vertexUV(x0, y0, z1, u1, v1);
            return;
        }
        if (face == 5) {
            vertexBuilder.vertexUV(x1, y0, z1, u0, v1);
            vertexBuilder.vertexUV(x1, y0, z0, u1, v1);
            vertexBuilder.vertexUV(x1, y1, z0, u1, v0);
            vertexBuilder.vertexUV(x1, y1, z1, u0, v0);
        }
    }

    public void renderBackFace(IVertexArrayBuilder vertexBuilder, long x, long y, long z, int face) {
        int tex = this.getTexture(face);
        int xt = tex % 16 * 16;
        int yt = tex / 16 * 16;
        float u0 = (float)xt / 256.0f;
        float u1 = ((float)xt + 15.99f) / 256.0f;
        float v0 = (float)yt / 256.0f;
        float v1 = ((float)yt + 15.99f) / 256.0f;
        double x0 = (x + this.xx0);
        double x1 = (x + this.xx1);
        double y0 = (y + this.yy0);
        double y1 = (y + this.yy1);
        double z0 =(z + this.zz0);
        double z1 = (z + this.zz1);
        if (face == 1) {
            vertexBuilder.vertexUV(x0, y1-0.001, z1, u0, v1);
            vertexBuilder.vertexUV(x0, y1-0.001, z0, u0, v0);
            vertexBuilder.vertexUV(x1, y1-0.001, z0, u1, v0);
            vertexBuilder.vertexUV(x1, y1-0.001, z1, u1, v1);
            return;
        }
        if (face == 0) {
            vertexBuilder.vertexUV(x1, y0+0.001, z1, u1, v1);
            vertexBuilder.vertexUV(x1, y0+0.001, z0, u1, v0);
            vertexBuilder.vertexUV(x0, y0+0.001, z0, u0, v0);
            vertexBuilder.vertexUV(x0, y0+0.001, z1, u0, v1);
            return;
        }
        if (face == 3) {
            vertexBuilder.vertexUV(x0, y1, z1-0.001, u1, v0);
            vertexBuilder.vertexUV(x1, y1, z1-0.001, u0, v0);
            vertexBuilder.vertexUV(x1, y0, z1-0.001, u0, v1);
            vertexBuilder.vertexUV(x0, y0, z1-0.001, u1, v1);
            return;
        }
        if (face == 2) {
            vertexBuilder.vertexUV(x0, y1, z0+0.001, u0, v0);
            vertexBuilder.vertexUV(x0, y0, z0+0.001, u0, v1);
            vertexBuilder.vertexUV(x1, y0, z0+0.001, u1, v1);
            vertexBuilder.vertexUV(x1, y1, z0+0.001, u1, v0);
            return;
        }
        if (face == 5) {
            vertexBuilder.vertexUV(x1-0.001, y1, z1, u1, v0);
            vertexBuilder.vertexUV(x1-0.001, y1, z0, u0, v0);
            vertexBuilder.vertexUV(x1-0.001, y0, z0, u0, v1);
            vertexBuilder.vertexUV(x1-0.001, y0, z1, u1, v1);
            return;
        }
        if (face == 4) {
            vertexBuilder.vertexUV(x0+0.001, y0, z1, u0, v1);
            vertexBuilder.vertexUV(x0+0.001, y0, z0, u1, v1);
            vertexBuilder.vertexUV(x0+0.001, y1, z0, u1, v0);
            vertexBuilder.vertexUV(x0+0.001, y1, z1, u0, v0);
        }
    }

    public final AABB getTileAABB(int x, int y, int z) {
        return new AABB(x, y, z, x + 1, y + 1, z + 1);
    }

    public AABB getAABB(long x, int y, long z) {
        return new AABB(x, y, z, x + 1, y + 1, z + 1);
    }

    public boolean blocksLight() {
        return true;
    }

    public boolean isSolid() {
        return true;
    }

    public boolean mayPick() {
        return true;
    }

    public void tick(_Level world, int x, int y, int z, Random random) {
    }

    public void destroy(_Level world, int x, int y, int z, ParticleEngine particleEngine) {
        int SD = 4;
        for (int xx = 0; xx < SD; ++xx) {
            for (int yy = 0; yy < SD; ++yy) {
                for (int zz = 0; zz < SD; ++zz) {
                    float xp = (float)x + ((float)xx + 0.5f) / (float)SD;
                    float yp = (float)y + ((float)yy + 0.5f) / (float)SD;
                    float zp = (float)z + ((float)zz + 0.5f) / (float)SD;
                    particleEngine.add(new Particle(world, xp, yp, zp, xp - (float)x - 0.5f, yp - (float)y - 0.5f, zp - (float)z - 0.5f, this.tex));
                }
            }
        }
    }

    public int getLiquidType() {
        return 0;
    }

    public void neighborChanged(_Level world, int x, int y, int z, int type) {
    }
}

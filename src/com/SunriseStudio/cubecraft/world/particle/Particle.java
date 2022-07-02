package com.SunriseStudio.cubecraft.world.particle;

import com.SunriseStudio.cubecraft.util.grass3D.render.draw.IVertexArrayBuilder;
import com.SunriseStudio.cubecraft.world.entity._Entity;
import com.SunriseStudio.cubecraft.world.Level;

public class Particle
extends _Entity {
    private float xd;
    private float yd;
    private float zd;
    public int tex;
    private float uo;
    private float vo;
    private int age = 0;
    private int lifetime = 0;
    private float size;

    public Particle(Level world, float x, float y, float z, float xa, float ya, float za, int tex) {
        super(world);
        this.tex = tex;
        this.setSize(0.2f, 0.2f);
        this.heightOffset = this.bbHeight / 2.0f;
        this.setPos(x, y, z);
        this.xd = xa + (float)(Math.random() * 2.0 - 1.0) * 0.4f;
        this.yd = ya + (float)(Math.random() * 2.0 - 1.0) * 0.4f;
        this.zd = za + (float)(Math.random() * 2.0 - 1.0) * 0.4f;
        float speed = (float)(Math.random() + Math.random() + 1.0) * 0.15f;
        float dd = (float)Math.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
        this.xd = this.xd / dd * speed * 0.4f;
        this.yd = this.yd / dd * speed * 0.4f + 0.1f;
        this.zd = this.zd / dd * speed * 0.4f;
        this.uo = (float)Math.random() * 3.0f;
        this.vo = (float)Math.random() * 3.0f;
        this.size = (float)(Math.random() * 0.5 + 0.5);
        this.lifetime = (int)(4.0 / (Math.random() * 0.9 + 0.1));
        this.age = 0;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
        this.yd = (float)((double)this.yd - 0.04);
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.98f;
        this.yd *= 0.98f;
        this.zd *= 0.98f;
        if (this.onGround) {
            this.xd *= 0.7f;
            this.zd *= 0.7f;
        }
    }

    public void render(IVertexArrayBuilder t, float a, float xa, float ya, float za, float xa2, float za2) {
        float u0 = ((float)(this.tex % 16) + this.uo / 4.0f) / 16.0f;
        float u1 = u0 + 0.015609375f;
        float v0 = ((float)(this.tex / 16) + this.vo / 4.0f) / 16.0f;
        float v1 = v0 + 0.015609375f;
        float r = 0.1f * this.size;
        double x = this.xo + (this.x - this.xo) * a;
        double y = this.yo + (this.y - this.yo) * a;
        double z = this.zo + (this.z - this.zo) * a;
        t.vertexUV(x - xa * r - xa2 * r, y - ya * r, z - za * r - za2 * r, u0, v1);
        t.vertexUV(x - xa * r + xa2 * r, y + ya * r, z - za * r + za2 * r, u0, v0);
        t.vertexUV(x + xa * r + xa2 * r, y + ya * r, z + za * r + za2 * r, u1, v0);
        t.vertexUV(x + xa * r - xa2 * r, y - ya * r, z + za * r - za2 * r, u1, v1);
    }
}

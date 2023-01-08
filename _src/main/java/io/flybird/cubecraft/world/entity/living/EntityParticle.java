package io.flybird.cubecraft.world.entity.living;

import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.entity.item.Item;
import io.flybird.quantum3d.draw.VertexArrayBuilder;
import io.flybird.util.math.AABB;
import io.flybird.util.math.HitBox;
import org.joml.Vector3d;

public abstract class EntityParticle extends Entity {
    protected final String tex;
    protected final float uo;
    protected final float vo;
    protected int age;
    protected final int lifetime;
    protected final double size;

    public EntityParticle(IWorld world, float x, float y, float z, float xa, float ya, float za, String tex) {
        super(world);
        this.tex = tex;
        this.setPos(x, y, z);
        this.xd = xa + (float)(Math.random() * 2.0 - 1.0) * 0.4f;
        this.yd = ya + (float)(Math.random() * 2.0 - 1.0) * 0.4f;
        this.zd = za + (float)(Math.random() * 2.0 - 1.0) * 0.4f;
        float speed = (float)(Math.random() + Math.random() + 1.0) * 0.15f;
        float dd = (float)Math.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
        this.xd = this.xd / dd * speed * 0.4f;
        this.yd = this.yd / dd * speed * 0.4f + 0.1f;
        this.zd = this.zd / dd * speed * 0.4f;
        this.uo = (float) (Math.random() * 3.0f);
        this.vo = (float) (Math.random() * 3.0f);
        this.size = (Math.random() * 0.5 + 0.5);
        this.lifetime = (int) (4.0 / (Math.random() * 0.9 + 0.1));
        this.age = 0;
    }

    @Override
    public AABB getCollisionBoxSize() {
        return new AABB(-0.1,-0.1,-0.1,0.1,0.1,0.1);
    }

    @Override
    public HitBox<Entity, IWorld>[] getSelectionBoxes() {
        return new HitBox[0];
    }

    @Override
    public Vector3d getCameraPosition() {
        return new Vector3d(0,0,0);
    }

    @Override
    public int getHealth() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Item[] getDrop() {
        return new Item[0];
    }

    @Override
    public void tick() {
        this.age++;
        this.yd = (this.yd - 0.04);
        this.move(xd,yd,zd);
        this.xd *= 0.98f;
        this.yd *= 0.98f;
        this.zd *= 0.98f;
        if (this.onGround) {
            this.xd *= 0.7f;
            this.zd *= 0.7f;
        }
    }

    public abstract void render(VertexArrayBuilder builder, float a, float xa, float ya, float za, float xa2, float za2);

    public int getLifetime() {
        return lifetime;
    }
}

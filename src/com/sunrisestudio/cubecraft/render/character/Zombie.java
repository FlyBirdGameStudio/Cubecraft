package com.sunrisestudio.cubecraft.render.character;
import com.sunrisestudio.grass3d.render.textures.Textures;
import com.sunrisestudio.cubecraft.world.entity._Entity;
import org.lwjgl.opengl.GL11;
import com.sunrisestudio.cubecraft.world._Level;

public class Zombie
extends _Entity {
    public float rot;
    public float timeOffs;
    public float speed;
    public float rotA;
    private static ZombieModel zombieModel = new ZombieModel();
    private Textures textures;

    public Zombie(_Level world, Textures textures, double x, double y, double z) {
        super(world);
        this.textures = textures;
        this.rotA = (float)(Math.random() + 1.0) * 0.01f;
        this.setPos(x, y, z);
        this.timeOffs = (float)Math.random() * 1239813.0f;
        this.rot = (float)(Math.random() * Math.PI * 2.0);
        this.speed = 1.0f;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        float xa = 0.0f;
        float ya = 0.0f;
        if (this.y < -100.0f) {
            this.remove();
        }
        this.rot += this.rotA;
        this.rotA = (float)((double)this.rotA * 0.99);
        this.rotA = (float)((double)this.rotA + (Math.random() - Math.random()) * Math.random() * Math.random() * (double)0.08f);
        xa = (float)Math.sin(this.rot);
        ya = (float)Math.cos(this.rot);
        if (this.onGround && Math.random() < 0.08) {
            this.yd = 0.5f;
        }
        this.moveRelative(xa, ya, this.onGround ? 0.1f : 0.02f);
        this.yd = (float)((double)this.yd - 0.08);
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.91f;
        this.yd *= 0.98f;
        this.zd *= 0.91f;
        if (this.onGround) {
            this.xd *= 0.7f;
            this.zd *= 0.7f;
        }
    }

    @Override
    public void render(float a) {
        GL11.glEnable(3553);
        GL11.glBindTexture(3553, this.textures.loadTexture("/assets/char.png", 9728));
        GL11.glPushMatrix();
        double time = (double)System.nanoTime() / 1.0E9 * 10.0 * (double)this.speed + (double)this.timeOffs;
        float size = 0.058333334f;
        double yy = -Math.abs(Math.sin(time * 0.6662)) * 5.0 - 23.0;
        GL11.glTranslated(this.xo + (this.x - this.xo) * a, this.yo + (this.y - this.yo) * a, this.zo + (this.z - this.zo) * a);
        GL11.glScalef(1.0f, -1.0f, 1.0f);
        GL11.glScalef(size, size, size);
        GL11.glTranslated(0.0, yy, 0.0);
        float c = 57.29578f;
        GL11.glRotatef(this.rot * c + 180.0f, 0.0f, 1.0f, 0.0f);
        zombieModel.render((float)time);
        GL11.glPopMatrix();
        GL11.glDisable(3553);
    }
}

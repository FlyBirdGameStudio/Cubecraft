package io.flybird.cubecraft.world.entity.particle;

import io.flybird.starfish3d.render.draw.VertexArrayBuilder;

import io.flybird.cubecraft.world.entity._Entity;
import io.flybird.cubecraft.world._Level;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class ParticleEngine {
    protected _Level world;
    private List<Particle> particles = new ArrayList<>();


    public ParticleEngine(_Level world ) {
        this.world = world;
    }

    public void add(Particle p) {
        this.particles.add(p);
    }

    public void tick() {
        for (int i = 0; i < this.particles.size(); ++i) {
            Particle p = this.particles.get(i);
            p.tick();
            if (!p.removed) continue;
            this.particles.remove(i--);
        }
    }

    public void render(_Entity player, float a, int layer) {
        if (this.particles.size() == 0) {
            return;
        }
        GL11.glEnable(3553);
        int id =0;// this.textures.loadTexture("/assets/terrain.png", 9728);
        GL11.glBindTexture(3553, id);
        float xa = -((float)Math.cos((double)player.yRot * Math.PI / 180.0));
        float za = -((float)Math.sin((double)player.yRot * Math.PI / 180.0));
        float xa2 = -za * (float)Math.sin((double)player.xRot * Math.PI / 180.0);
        float za2 = xa * (float)Math.sin((double)player.xRot * Math.PI / 180.0);
        float ya = (float)Math.cos((double)player.xRot * Math.PI / 180.0);
        VertexArrayBuilder t = new VertexArrayBuilder(16);
        GL11.glColor4f(0.8f, 0.8f, 0.8f, 1.0f);
        t.begin();
        for (int i = 0; i < this.particles.size(); ++i) {
            Particle p = this.particles.get(i);
            if (p.isLit() == (layer == 1)) continue;
            p.render(t, a, xa, ya, za, xa2, za2);
        }
        t.end();
        GL11.glDisable(3553);
    }
}

package io.flybird.cubecraft.world.entity.particle;

import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.living.EntityParticle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleEngine {
    protected final IWorld world;
    private final List<EntityParticle> particles = new ArrayList<>();

    public ParticleEngine(IWorld world) {
        this.world = world;
    }

    public void add(EntityParticle p) {
        this.particles.add(p);
        p.setWorld(this.world);
    }

    public void tick() {
        Iterator<EntityParticle> iterator = this.particles.iterator();
        while (iterator.hasNext()) {
            EntityParticle particle = iterator.next();
            particle.tick();
            if (particle.getLifetime() <= 0) {
                iterator.remove();
            }
        }
    }

    public List<EntityParticle> getParticles() {
        return particles;
    }
}

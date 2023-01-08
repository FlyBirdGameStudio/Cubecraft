package io.flybird.cubecraft.internal.renderer;

import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.render.renderer.IWorldRenderer;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.living.EntityParticle;
import io.flybird.cubecraft.world.entity.living.Player;
import io.flybird.cubecraft.world.entity.particle.ParticleEngine;
import io.flybird.quantum3d.Camera;
import io.flybird.quantum3d.draw.VertexArrayBuilder;
import io.flybird.quantum3d.platform.Window;
import io.flybird.util.GameSetting;
import io.flybird.util.container.namespace.TypeItem;
import org.lwjgl.opengl.GL11;

@TypeItem("cubecraft:particle_renderer")
public class ParticleRenderer extends IWorldRenderer {
    private ParticleEngine particleEngine;

    public ParticleRenderer(Window window, IWorld world, Player player, Camera cam, GameSetting setting) {
        super(window, world, player, cam, setting);
    }

    @Override
    public void render(float interpolationTime) {

        GL11.glEnable(3553);
        ClientRegistries.TEXTURE.getTexture2DContainer().get("cubecraft:terrain").bind();

        double yRot=this.camera.getRotation().y;
        double xRot=this.camera.getRotation().x;

        float xa = -((float)Math.cos(yRot * Math.PI / 180.0));
        float za = -((float)Math.sin(yRot * Math.PI / 180.0));
        float xa2 = -za * (float)Math.sin(xRot * Math.PI / 180.0);
        float za2 = xa * (float)Math.sin(xRot * Math.PI / 180.0);
        float ya = (float)Math.cos(xRot * Math.PI / 180.0);
        VertexArrayBuilder t = new VertexArrayBuilder(16);
        GL11.glColor4f(0.8f, 0.8f, 0.8f, 1.0f);
        t.begin();
        for (EntityParticle p : this.particleEngine.getParticles()) {
            p.render(t, (int) interpolationTime, xa, ya, za, xa2, za2);
        }
        t.end();
        GL11.glDisable(3553);
    }

    public void setParticleEngine(ParticleEngine particleEngine) {
        this.particleEngine = particleEngine;
    }
}
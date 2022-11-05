package io.flybird.cubecraft.client.render.renderer;

import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.starfish3d.render.Camera;

public class EntityRenderer extends IWorldRenderer {
    public EntityRenderer(IWorld w, Player p, Camera c) {
        super(w, p,c);
    }

    @Override
    public void render(float interpolationTime) {
        for (Entity e:this.world.getAllEntities()){
            try {
                Registry.getEntityRendererMap().get(e.getID()).render(e);
            }catch (Exception ignored){}
        }
    }
}

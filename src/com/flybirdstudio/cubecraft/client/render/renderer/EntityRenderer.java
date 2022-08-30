package com.flybirdstudio.cubecraft.client.render.renderer;

import com.flybirdstudio.cubecraft.registery.Registery;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.entity.Entity;
import com.flybirdstudio.cubecraft.world.entity.humanoid.Player;
import com.flybirdstudio.starfish3d.render.Camera;

public class EntityRenderer extends IWorldRenderer {
    public EntityRenderer(IWorld w, Player p, Camera c) {
        super(w, p,c);
    }

    @Override
    public void render(float interpolationTime) {
        for (Entity e:this.world.getAllEntities()){
            try {
                Registery.getEntityRendererMap().get(e.getID()).render(e);
            }catch (Exception ignored){}
        }
    }
}

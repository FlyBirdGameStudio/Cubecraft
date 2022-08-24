package com.sunrisestudio.cubecraft.client.render.renderer;

import com.sunrisestudio.cubecraft.registery.Registry;
import com.sunrisestudio.cubecraft.world.World;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;
import com.sunrisestudio.grass3d.render.Camera;

public class EntityRenderer extends IWorldRenderer {
    public EntityRenderer(World w, Player p, Camera c) {
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

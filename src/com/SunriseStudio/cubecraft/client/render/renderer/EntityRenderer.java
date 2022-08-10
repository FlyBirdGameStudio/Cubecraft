package com.sunrisestudio.cubecraft.client.render.renderer;

import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;
import com.sunrisestudio.grass3d.render.Camera;

public class EntityRenderer extends IRenderer {
    public EntityRenderer(IWorldAccess w, Player p, Camera c) {
        super(w, p,c);
    }

    @Override
    public void render(float interpolationTime) {
        //GLUtil.setupPerspectiveCamera(GameSetting.instance.fov, displayWidth, displayHeight);

        for (Entity e:this.world.getAllEntities()){
            e.render(interpolationTime);
        }
        //this.particleEngine.render(this.player,interpolationTime,0);
    }

    @Override
    public void chunkUpdate(long x, long y, long z) {

    }
}

package com.sunrisestudio.cubecraft.render.renderer;

import com.sunrisestudio.grass3d.render.Camera;
import com.sunrisestudio.cubecraft.world.entity._Entity;
import com.sunrisestudio.cubecraft.world.entity._Player;
import com.sunrisestudio.cubecraft.world._Level;

public class EntityRenderer extends IRenderer {
    public EntityRenderer(_Level w, _Player p, Camera c) {
        super(w, p,c);
    }

    @Override
    public void render(float interpolationTime) {
        //GLUtil.setupPerspectiveCamera(GameSetting.instance.fov, displayWidth, displayHeight);

        for (_Entity e:this.world.entities){
            e.render(interpolationTime);
        }
        //this.particleEngine.render(this.player,interpolationTime,0);
    }

    @Override
    public void chunkUpdate(long x, long y, long z) {

    }
}

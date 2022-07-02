package com.SunriseStudio.cubecraft.render.renderer;

import com.SunriseStudio.cubecraft.util.grass3D.render.GLUtil;
import com.SunriseStudio.cubecraft.world.entity._Entity;
import com.SunriseStudio.cubecraft.world.entity._Player;
import com.SunriseStudio.cubecraft.world.Level;
import com.SunriseStudio.cubecraft.world.particle.ParticleEngine;

public class EntityRenderer extends IWorldRenderer {
    public ParticleEngine particleEngine;
    public EntityRenderer(Level w, _Player p, ParticleEngine pe) {
        super(w, p);
        this.particleEngine=pe;
    }

    @Override
    public void render(float interpolationTime) {
        //GLUtil.setupPerspectiveCamera(GameSetting.instance.fov, displayWidth, displayHeight);
        GLUtil.setupCameraPosition(this.player.x,this.player.y,this.player.z,this.player.xRot,this.player.yRot,0,0,0,0);
        for (_Entity e:this.world.entities){
            e.render(interpolationTime);
        }
        //this.particleEngine.render(this.player,interpolationTime,0);
    }

    @Override
    public void chunkUpdate(long x, long y, long z) {

    }
}

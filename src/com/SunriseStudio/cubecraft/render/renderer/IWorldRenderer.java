package com.SunriseStudio.cubecraft.render.renderer;

import com.SunriseStudio.cubecraft.render.Camera;
import com.SunriseStudio.cubecraft.world.entity._Player;
import com.SunriseStudio.cubecraft.world.Level;

public abstract class IWorldRenderer {
    public _Player player;
    public Level world;
    public Camera camera=new Camera();
    public abstract void render(float interpolationTime);
    public abstract void chunkUpdate(long x,long y,long z);
    public IWorldRenderer(Level w, _Player p){
        this.world=w;
        this.player=p;
    }
}

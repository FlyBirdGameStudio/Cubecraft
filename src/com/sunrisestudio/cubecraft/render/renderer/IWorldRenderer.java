package com.sunrisestudio.cubecraft.render.renderer;

import com.sunrisestudio.cubecraft.render.Camera;
import com.sunrisestudio.cubecraft.world.entity._Player;
import com.sunrisestudio.cubecraft.world._Level;

public abstract class IWorldRenderer {
    public _Player player;
    public _Level world;
    public Camera camera=new Camera();
    public abstract void render(float interpolationTime);
    public abstract void chunkUpdate(long x,long y,long z);
    public IWorldRenderer(_Level w, _Player p,Camera cam){
        this.world=w;
        this.player=p;
        this.camera=cam;
    }
}

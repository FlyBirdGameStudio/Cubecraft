package com.flybirdstudio.cubecraft.client.render.renderer;

import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.entity.humanoid.Player;
import com.flybirdstudio.cubecraft.world.WorldListener;
import com.flybirdstudio.starfish3d.render.Camera;

public abstract class IWorldRenderer implements WorldListener {
    public Player player;
    public IWorld world;
    public Camera camera;
    public abstract void render(float interpolationTime);
    public IWorldRenderer(IWorld w, Player p, Camera cam){
        this.world=w;
        this.player=p;
        this.camera=cam;
        this.world.addListener(this);
    }

    @Override
    public void blockChanged(long x, long y, long z) {
        //do nothing...
    }
}

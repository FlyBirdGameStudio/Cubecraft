package com.sunrisestudio.cubecraft.client.render.renderer;

import com.sunrisestudio.cubecraft.world.World;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;
import com.sunrisestudio.cubecraft.world.WorldListener;
import com.sunrisestudio.grass3d.render.Camera;

public abstract class IWorldRenderer implements WorldListener {
    public Player player;
    public World world;
    public Camera camera;
    public abstract void render(float interpolationTime);
    public IWorldRenderer(World w, Player p, Camera cam){
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

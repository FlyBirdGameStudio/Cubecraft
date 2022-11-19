package io.flybird.cubecraft.client.render.renderer;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.cubecraft.world.WorldListener;
import io.flybird.starfish3d.render.Camera;

public abstract class IWorldRenderer implements WorldListener {
    public Player player;
    public IWorld world;
    public Camera camera;
    public GameSetting setting;
    public abstract void render(float interpolationTime);
    public IWorldRenderer(IWorld w, Player p, Camera cam, GameSetting setting){
        this.world=w;
        this.player=p;
        this.camera=cam;
        this.setting=setting;
        this.world.addListener(this);
    }

    @Override
    public void blockChanged(long x, long y, long z) {
        //do nothing...
    }
}

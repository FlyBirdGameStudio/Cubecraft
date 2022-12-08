package io.flybird.cubecraft.client.render.renderer;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.cubecraft.world.WorldListener;
import io.flybird.starfish3d.platform.Window;
import io.flybird.starfish3d.render.Camera;

public abstract class IWorldRenderer implements WorldListener {
    public Player player;
    public IWorld world;
    public Camera camera;
    public GameSetting setting;
    public abstract void render(float interpolationTime);
    public Window window;

    public IWorldRenderer(Window window, IWorld world, Player player, Camera cam, GameSetting setting){
        this.world=world;
        this.player=player;
        this.camera=cam;
        this.setting=setting;
        this.window=window;
    }

    @Override
    public void blockChanged(long x, long y, long z) {
        //do nothing...
    }

    public void refresh(){
        //do nothing also...
    }
}

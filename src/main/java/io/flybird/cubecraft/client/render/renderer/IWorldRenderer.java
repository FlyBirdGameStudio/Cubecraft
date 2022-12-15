package io.flybird.cubecraft.client.render.renderer;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.starfish3d.platform.Window;
import io.flybird.starfish3d.render.Camera;
import io.flybird.util.event.EventListener;

public abstract class IWorldRenderer implements EventListener {
    public final Player player;
    public final IWorld world;
    public final Camera camera;
    public final GameSetting setting;
    public abstract void render(float interpolationTime);
    public final Window window;

    public IWorldRenderer(Window window, IWorld world, Player player, Camera cam, GameSetting setting){
        this.world=world;
        this.player=player;
        this.camera=cam;
        this.setting=setting;
        this.window=window;
    }

    public void refresh(){
        //do nothing also...
    }
}

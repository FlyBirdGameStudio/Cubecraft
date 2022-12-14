package io.flybird.cubecraft.client.render.renderer;

import io.flybird.util.GameSetting;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.living.Player;
import io.flybird.quantum3d.platform.Window;
import io.flybird.quantum3d.Camera;
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

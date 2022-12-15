package io.flybird.cubecraft.internal.renderer;

import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.util.GameSetting;
import io.flybird.cubecraft.client.render.renderer.IWorldRenderer;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.starfish3d.platform.Window;
import io.flybird.starfish3d.render.Camera;
import io.flybird.util.logging.LogHandler;

public class EntityRenderer extends IWorldRenderer {
    private final LogHandler logHandler=LogHandler.create("Client/EntityRenderer");

    public EntityRenderer(Window window, IWorld world, Player player, Camera cam, GameSetting setting) {
        super(window, world, player, cam, setting);
    }

    @Override
    public void render(float interpolationTime) {
        int visibleCount=0;
        int allCount=0;
        for (Entity e:this.world.getAllEntities()){
            allCount++;
            visibleCount++;
            try {
                ClientRegistries.ENTITY_RENDERER.get(e.getID()).render(e);
            }catch (Exception ignored){}
        }
        Registries.DEBUG_INFO.putI("cubecraft:entity_renderer/all_entities",visibleCount);
        Registries.DEBUG_INFO.putI("cubecraft:entity_renderer/visible_entities",allCount);
    }
}

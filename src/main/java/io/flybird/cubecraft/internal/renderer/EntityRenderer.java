package io.flybird.cubecraft.internal.renderer;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.client.render.renderer.IWorldRenderer;
import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.register.RenderRegistry;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.starfish3d.render.Camera;
import io.flybird.util.logging.LogHandler;

public class EntityRenderer extends IWorldRenderer {
    private final LogHandler logHandler=LogHandler.create("Client/EntityRenderer");

    public EntityRenderer(IWorld w, Player p, Camera c, GameSetting setting) {
        super(w, p,c,setting);
    }

    @Override
    public void render(float interpolationTime) {
        int visibleCount=0;
        int allCount=0;
        for (Entity e:this.world.getAllEntities()){
            allCount++;
            visibleCount++;
            try {
                RenderRegistry.getEntityRendererMap().get(e.getID()).render(e);
            }catch (Exception ignored){}
        }
        Registry.getDebugInfoHandler().putI("cubecraft:entity_renderer/all_entities",visibleCount);
        Registry.getDebugInfoHandler().putI("cubecraft:entity_renderer/visible_entities",allCount);
    }
}

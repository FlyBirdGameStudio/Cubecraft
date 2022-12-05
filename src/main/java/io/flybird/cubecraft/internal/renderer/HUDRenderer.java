package io.flybird.cubecraft.internal.renderer;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.client.render.renderer.IWorldRenderer;
import io.flybird.cubecraft.client.render.renderer.LevelRenderer;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.starfish3d.platform.Window;
import io.flybird.starfish3d.render.Camera;
import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.util.math.HitBox;
import org.lwjgl.opengl.GL11;

public class HUDRenderer extends IWorldRenderer {
    public HUDRenderer(Window window, IWorld world, Player player, Camera cam, GameSetting setting) {
        super(window, world, player, cam, setting);
    }

    @Override
    public void render(float interpolationTime) {
        LevelRenderer.setRenderState(this.setting, world);
        this.camera.setUpGlobalCamera(this.window);
        GL11.glLineWidth(4.0f);
        if(this.player.hitResult!=null&& this.player.hitResult.aabb().getObject() instanceof BlockState sel) {
            HitBox aabb=this.player.hitResult.aabb();
            GL11.glPushMatrix();
            this.camera.setupObjectCamera(aabb.minPos());
            for (HitBox hitBox:sel.getBlock().getSelectionBox(0,0,0,sel)){
                ShapeRenderer.renderAABB(hitBox, 0xFFFFFF);
            }
            GL11.glPopMatrix();
        }
        LevelRenderer.closeState(this.setting);
    }
}

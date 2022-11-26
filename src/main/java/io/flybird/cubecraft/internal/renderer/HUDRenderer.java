package io.flybird.cubecraft.internal.renderer;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.client.render.renderer.IWorldRenderer;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.starfish3d.render.Camera;
import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.util.math.HitBox;
import org.lwjgl.opengl.GL11;

public class HUDRenderer extends IWorldRenderer {

    public HUDRenderer(IWorld w, Player p, Camera cam, GameSetting setting) {
        super(w, p, cam, setting);
    }

    @Override
    public void render(float interpolationTime) {
        this.camera.setUpGlobalCamera();
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
    }
}

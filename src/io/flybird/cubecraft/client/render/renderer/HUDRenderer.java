package io.flybird.cubecraft.client.render.renderer;

import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.starfish3d.render.Camera;
import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.util.math.HitBox;
import org.lwjgl.opengl.GL11;

public class HUDRenderer extends IWorldRenderer {
    public HUDRenderer(IWorld w, Player p, Camera cam) {
        super(w, p, cam);
    }

    @Override
    public void render(float interpolationTime) {
        this.camera.setUpGlobalCamera();
        GL11.glLineWidth(4.0f);
        if(this.player.hitResult!=null&&this.player.hitResult.aabb().getObject() instanceof BlockState) {
            HitBox aabb=this.player.hitResult.aabb();
            GL11.glPushMatrix();
            BlockState sel= (BlockState) this.player.hitResult.aabb().getObject();
            this.camera.setupObjectCamera(aabb.minPos());
            for (HitBox hitBox:sel.getSelectionBox(0,0,0))
            ShapeRenderer.renderAABB(hitBox, 0xFFFFFF);
            GL11.glPopMatrix();
        }
    }
}

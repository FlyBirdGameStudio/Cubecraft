package com.sunrisestudio.cubecraft.client.render.renderer;

import com.sunrisestudio.cubecraft.Start;
import com.sunrisestudio.cubecraft.world.World;
import com.sunrisestudio.cubecraft.world.block.BlockState;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;
import com.sunrisestudio.grass3d.render.Camera;
import com.sunrisestudio.grass3d.render.GLUtil;
import com.sunrisestudio.grass3d.render.ShapeRenderer;
import com.sunrisestudio.util.math.HitBox;
import org.lwjgl.opengl.GL11;

public class HUDRenderer extends IWorldRenderer {
    public HUDRenderer(World w, Player p, Camera cam) {
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

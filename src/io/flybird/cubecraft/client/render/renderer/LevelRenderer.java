package io.flybird.cubecraft.client.render.renderer;

import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.starfish3d.render.Camera;
import io.flybird.util.ColorUtil;
import io.flybird.util.container.CollectionUtil;
import io.flybird.starfish3d.render.GLUtil;
import io.flybird.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class LevelRenderer {

    public HashMap<String, IWorldRenderer>renderers;
    public IWorld world;
    public Player player;
    public Camera camera=new Camera();
    private EnvironmentRenderer environmentRenderer;

    public LevelRenderer(IWorld w, Player p){
        this.world=w;
        this.player=p;
        this.renderers= (HashMap<String, IWorldRenderer>) Registry.getWorldRenderers().createAll(world,player,camera);
        this.environmentRenderer=new EnvironmentRenderer(world,player,camera);
    }

    public void render(float interpolationTime) {
        float[] col= ColorUtil.int1Float1ToFloat4(world.getWorldInfo().fogColor(),1.0f);
        GL11.glClearColor(col[0],col[1],col[2],col[3]);
        //update camera position
        this.camera.setPos(
                MathHelper.linear_interpolate(this.player.xo, this.player.x,interpolationTime)+this.player.getCameraPosition().x,
                MathHelper.linear_interpolate(this.player.yo, this.player.y,interpolationTime)+this.player.getCameraPosition().y,
                MathHelper.linear_interpolate(this.player.zo, this.player.z,interpolationTime)+this.player.getCameraPosition().z);
        this.camera.setupRotation(this.player.xRot,this.player.yRot,this.player.zRot);
        this.camera.setPosRelative(0,0,0.15);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GLUtil.enableMultiSample();
        GL11.glPushMatrix();
        this.environmentRenderer.render(interpolationTime);
        GL11.glPopMatrix();

        CollectionUtil.iterateMap(this.renderers, (key, item) -> {
            GL11.glPushMatrix();
            item.render(interpolationTime);
            GL11.glPopMatrix();
        });
        GLUtil.disableMultiSample();
        GL11.glDisable(GL11.GL_CULL_FACE);

        if(this.camera.isRotationChanged()){
            this.camera.updateRotation();
        }
        if(this.camera.isPositionChanged()){
            this.camera.updatePosition();
        }
    }
}

package io.flybird.cubecraft.client.render.renderer;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.internal.renderer.EnvironmentRenderer;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.starfish3d.event.KeyPressEvent;
import io.flybird.starfish3d.platform.Keyboard;
import io.flybird.starfish3d.render.Camera;
import io.flybird.starfish3d.render.GLUtil;
import io.flybird.util.ColorUtil;
import io.flybird.util.container.CollectionUtil;
import io.flybird.util.event.EventHandler;
import io.flybird.util.event.EventListener;
import io.flybird.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class LevelRenderer implements EventListener {

    public HashMap<String, IWorldRenderer>renderers;
    public IWorld world;
    public Player player;
    public Camera camera=new Camera();
    public EnvironmentRenderer environmentRenderer;

    public LevelRenderer(IWorld w, Player p, Cubecraft client){
        client.getWindow().getEventBus().registerEventListener(this);
        this.world=w;
        this.player=p;
        this.renderers= (HashMap<String, IWorldRenderer>) Registries.WORLD_RENDERER.createAll(client.getWindow(),world,player,camera, client.getGameSetting());
        this.environmentRenderer=new EnvironmentRenderer(client.getWindow(),world,player,camera,client.getGameSetting());
    }




    public void render(float interpolationTime) {
        float[] col= ColorUtil.int1Float1ToFloat4(world.getWorldInfo().fogColor(),1.0f);
        GL11.glClearColor(col[0],col[1],col[2],col[3]);
        //update camera position
        this.camera.setPos(
                MathHelper.linearInterpolate(this.player.xo, this.player.x,interpolationTime)+this.player.getCameraPosition().x,
                MathHelper.linearInterpolate(this.player.yo, this.player.y,interpolationTime)+this.player.getCameraPosition().y,
                MathHelper.linearInterpolate(this.player.zo, this.player.z,interpolationTime)+this.player.getCameraPosition().z);
        this.camera.setupRotation(this.player.xRot,this.player.yRot,this.player.zRot);
        this.camera.setPosRelative(0,0,0.15);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPushMatrix();
        this.environmentRenderer.render(interpolationTime);
        GL11.glPopMatrix();

        CollectionUtil.iterateMap(this.renderers, (key, item) -> {
            GL11.glPushMatrix();
            item.render(interpolationTime);
            GL11.glPopMatrix();
        });
        GL11.glDisable(GL11.GL_CULL_FACE);

        if(this.camera.isRotationChanged()){
            this.camera.updateRotation();
        }
        if(this.camera.isPositionChanged()){
            this.camera.updatePosition();
        }
    }

    @EventHandler
    public void onRefresh(KeyPressEvent e){
        if(e.key()== Keyboard.KEY_F8){
            CollectionUtil.iterateMap(this.renderers, (key, item) -> item.refresh());
        }
    }



    public static void setRenderState(GameSetting setting,IWorld world) {
        GLUtil.enableBlend();
        if (setting.getValueAsInt("client.render.fxaa", 0)>0) {
            GLUtil.enableMultiSample();
        }
        int d = setting.getValueAsInt("client.render.terrain.renderDistance", 4);
        if (setting.getValueAsBoolean("client.render.fog", true)) {
            GL11.glEnable(GL11.GL_FOG);
            GLUtil.setupFog(d * d, ColorUtil.int1Float1ToFloat4(world.getWorldInfo().fogColor(), 1));
        }
    }

    public static void closeState(GameSetting setting) {
        if (setting.getValueAsBoolean("client.render.fog", true)) {
            GL11.glDisable(GL11.GL_FOG);
        }
        GLUtil.disableBlend();
        if (setting.getValueAsInt("client.render.fxaa", 0)>0) {
            GLUtil.disableMultiSample();
        }
    }
}

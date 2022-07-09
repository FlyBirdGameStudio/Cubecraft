package com.sunrisestudio.cubecraft.render.renderer;

import com.sunrisestudio.cubecraft.render.Camera;
import com.sunrisestudio.util.container.CollectionUtil;
import com.sunrisestudio.grass3d.render.GLUtil;
import com.sunrisestudio.util.math.MathHelper;
import com.sunrisestudio.cubecraft.world.entity._Player;
import com.sunrisestudio.cubecraft.world.LevelListener;
import com.sunrisestudio.cubecraft.world._Level;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

public class ContentWorldRenderer implements LevelListener {
    public static HashMap<String, Class<? extends ChunkRenderer>> rendererRegistries=new HashMap<>();
    private final EnvironmentRenderer environmentRenderer;
    public HashMap<String, IWorldRenderer>renderers=new HashMap<>();
    public _Level world;
    public _Player player;
    public Camera camera=new Camera();

    public ContentWorldRenderer(_Level w, _Player p){
        this.world=w;
        this.player=p;
        world.addListener(this);

        CollectionUtil.iterateMap(rendererRegistries, (key, item) -> {
            try {
                ContentWorldRenderer.this.renderers.put(key,item.getDeclaredConstructor(
                        _Level.class,
                        _Player.class,
                        Camera.class
                ).newInstance(
                                ContentWorldRenderer.this.world,
                                ContentWorldRenderer.this.player,
                                ContentWorldRenderer.this.camera
                        )
                );
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        this.environmentRenderer=new EnvironmentRenderer(this.world,this.player,this.camera);
    }

    public void render(float interpolationTime, int width, int height) {
        this.camera.setPos(
                MathHelper.linear_interpolate(this.player.xo, this.player.x,interpolationTime),
                MathHelper.linear_interpolate(this.player.yo, this.player.y,interpolationTime),
                MathHelper.linear_interpolate(this.player.zo, this.player.z,interpolationTime));
        this.camera.setupRotation(this.player.xRot,this.player.yRot,this.player.zRot);
        this.camera.setPosRelative(0,0,0.3);

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
    }

    @Override
    public void tileChanged(long x, int y, long z) {
        Set<String> keySet=this.renderers.keySet();
        for (String s:keySet){
            this.renderers.get(s).chunkUpdate(x/16,y/16,z/16);
        }
    }


    static {
        ContentWorldRenderer.rendererRegistries.put("cubecraft:chunk_renderer",ChunkRenderer.class);
    }
}

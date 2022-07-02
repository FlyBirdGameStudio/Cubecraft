package com.SunriseStudio.cubecraft.render.renderer;

import com.SunriseStudio.cubecraft.GameSetting;
import com.SunriseStudio.cubecraft.util.math.MathHelper;
import com.SunriseStudio.cubecraft.world.entity._Player;
import com.SunriseStudio.cubecraft.world.LevelListener;
import com.SunriseStudio.cubecraft.world.Level;
import com.SunriseStudio.cubecraft.world.particle.ParticleEngine;
import org.lwjgl.opengl.ARBDepthClamp;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.HashMap;
import java.util.Set;

public class ContentWorldRenderer implements LevelListener {
    public HashMap<String, IWorldRenderer>renderers;
    public Level world;
    public _Player player;
    public ParticleEngine particleEngine;

    public ContentWorldRenderer(Level w, _Player p){
        this.world=w;
        this.player=p;
        this.renderers=new HashMap<>();

        world.addListener(this);

        //register renderers
        this.renderers.put("openMC/renderer:environment",new EnvironmentRenderer(this.world,this.player));
        this.renderers.put("openMC/renderer:chunk",new ChunkRenderer(this.world,this.player));
        this.renderers.put("openMC/renderer:entity",new EntityRenderer(this.world,this.player,this.particleEngine));

    }

    public void render(float interpolationTime, int width, int height) {
        Set<String> keySet=this.renderers.keySet();
        GL11.glEnable(ARBDepthClamp.GL_DEPTH_CLAMP);
        GL11.glEnable(GL13.GL_MULTISAMPLE);
        GL11.glEnable(GL11.GL_CULL_FACE);
        if(GameSetting.instance.FXAA>0) {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
        }
        for (String s:keySet){
            GL11.glPushMatrix();
            this.renderers.get(s).render(interpolationTime);
            this.renderers.get(s).camera.setPos(
                    MathHelper.linear_interpolate(this.player.xo, this.player.x,interpolationTime),
                    MathHelper.linear_interpolate(this.player.yo, this.player.y,interpolationTime),
                    MathHelper.linear_interpolate(this.player.zo, this.player.z,interpolationTime));
            this.renderers.get(s).camera.setupRotation(this.player.xRot,this.player.yRot,this.player.zRot);
            this.renderers.get(s).camera.setPosRelative(0,0,0.3);

            GL11.glPopMatrix();
        }
        if(GameSetting.instance.FXAA>0) {
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
        }
        GL11.glDisable(ARBDepthClamp.GL_DEPTH_CLAMP);
        GL11.glDisable(GL13.GL_MULTISAMPLE);
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    @Override
    public void tileChanged(long x, int y, long z) {
        Set<String> keySet=this.renderers.keySet();
        for (String s:keySet){
            this.renderers.get(s).chunkUpdate(x/16,y/16,z/16);
        }
    }


}

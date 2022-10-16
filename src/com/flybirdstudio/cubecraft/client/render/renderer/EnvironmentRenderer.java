package com.flybirdstudio.cubecraft.client.render.renderer;

import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.entity.humanoid.Player;
import com.flybirdstudio.cubecraft.world.worldGen.noiseGenerator.PerlinNoise;
import com.flybirdstudio.starfish3d.render.Camera;
import com.flybirdstudio.starfish3d.render.GLUtil;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayUploader;
import com.flybirdstudio.starfish3d.render.drawcall.IRenderCall;
import com.flybirdstudio.starfish3d.render.drawcall.ListRenderCall;
import com.flybirdstudio.util.ColorUtil;
import com.flybirdstudio.util.container.BufferBuilder;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class EnvironmentRenderer extends IWorldRenderer {
    public static final int SKY_SIZE = 256;
    private static final int CLOUD_SIZE = 96;
    private static final double CLOUD_HEIGHT = 384;
    private final IRenderCall sky=new ListRenderCall();
    private final int cloudList = GL11.glGenLists(5);
    private final PerlinNoise noise = new PerlinNoise(new Random(world.getSeed()), 12);

    public EnvironmentRenderer(IWorld w, Player p, Camera c) {
        super(w, p, c);
        this.sky.allocate();
        updateSky();
        updateCloud();
    }

    @Override
    public void render(float interpolationTime) {
        GLUtil.enableBlend();
        int d = GameSetting.instance.getValueAsInt("client.render.terrain.renderDistance", 4) * 16 + 1024;

        GLUtil.setupFog(d / 6, BufferBuilder.from(ColorUtil.int1Float1ToFloat4(world.getWorldInfo().fogColor(), 1.0f)));
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        this.camera.setUpGlobalCamera();
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glDisable(GL11.GL_FOG);
        this.sky.call();
        GL11.glEnable(GL11.GL_FOG);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        int d2 = d * 2;
        if (GameSetting.instance.getValueAsInt("client.render.environment.cloud.quality", 1) > 0) {
            for (long i = (long) (this.player.x - d2 - world.getTime() * 0.05) / CLOUD_SIZE; i < (this.player.x + d2 - world.getTime() * 0.05) / CLOUD_SIZE; i += 1) {
                for (long j = (long) (this.player.z - d2) / CLOUD_SIZE; j < (this.player.z + d2) / CLOUD_SIZE; j += 1) {
                    if (noise.getValue(i * 256, j * 256) > 16) {
                        GL11.glPushMatrix();
                        camera.setupObjectCamera(new Vector3d(i * CLOUD_SIZE + world.getTime() * 0.05, CLOUD_HEIGHT, j * CLOUD_SIZE));
                        GL11.glCallList(this.cloudList);
                        if (GameSetting.instance.getValueAsInt("client.render.environment.cloud.quality", 1) > 1) {
                            if (noise.getValue((i - 1) * 256, j * 256) <= 16) {
                                GL11.glCallList(cloudList + 3);
                            }
                            if (noise.getValue((i + 1) * 256, j * 256) <= 16) {
                                GL11.glCallList(cloudList + 4);
                            }
                            if (noise.getValue(i * 256, (j - 1) * 256) <= 16) {
                                GL11.glCallList(cloudList + 1);
                            }
                            if (noise.getValue(i * 256, (j + 1) * 256) <= 16) {
                                GL11.glCallList(cloudList + 2);
                            }
                        }
                        GL11.glPopMatrix();
                    }
                }
            }
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GLUtil.disableBlend();
    }

    public void updateSky() {
        int d2 = GameSetting.instance.getValueAsInt("client.render.terrain.renderDistance", 4);
        VertexArrayBuilder v = new VertexArrayBuilder(1048576,GL11.GL_TRIANGLES);
        v.begin();
        v.color(world.getWorldInfo().skyColor());

        float   cx  =   0;
        float   cy  =   d2*16;
        float   cz  =   0;
        float   r   =   d2*16*32;

        for(int i = 0; i < 360; ++i) {
            float x     =  (float)Math.cos((double)i * Math.PI / 180) * r + cx;
            float y     =  (float)Math.sin((double)i * Math.PI / 180) * r + cy;

            float x1    =  (float)Math.cos((double)(i + 1) * Math.PI / 180) * r + cx;
            float y1    =  (float)Math.sin((double)(i + 1) * Math.PI / 180) * r + cy;
            v.color(world.getWorldInfo().skyColor());
            v.vertex(cx, cy+d2*64, cz);
            v.color(world.getWorldInfo().fogColor());
            v.vertex(x, cy, y);
            v.vertex(x1, cy, y1);
        }
        v.end();
        this.sky.upload(v);
    }


    public void updateCloud() {
        VertexArrayBuilder vertexBuilder = new VertexArrayBuilder(24);
        double x0 = (0);
        double x1 = (CLOUD_SIZE);
        double y0 = (0);
        double y1 = (32);
        double z0 = (0);
        double z1 = (CLOUD_SIZE);


        vertexBuilder.begin();
        float[] col = ColorUtil.int1Float1ToFloat4(world.getWorldInfo().cloudColor(), 0.8f);
        vertexBuilder.color(col[0], col[1], col[2], col[3]);
        vertexBuilder.vertex(x0, y0, z1);
        vertexBuilder.vertex(x0, y0, z0);
        vertexBuilder.vertex(x1, y0, z0);
        vertexBuilder.vertex(x1, y0, z1);

        vertexBuilder.vertex(x1, y1, z1);
        vertexBuilder.vertex(x1, y1, z0);
        vertexBuilder.vertex(x0, y1, z0);
        vertexBuilder.vertex(x0, y1, z1);
        vertexBuilder.end();
        GL11.glNewList(this.cloudList, GL11.GL_COMPILE);
        VertexArrayUploader.uploadPointer(vertexBuilder);
        GL11.glEndList();


        vertexBuilder.begin();
        vertexBuilder.vertex(x0, y1, z0);
        vertexBuilder.vertex(x1, y1, z0);
        vertexBuilder.vertex(x1, y0, z0);
        vertexBuilder.vertex(x0, y0, z0);
        vertexBuilder.end();
        GL11.glNewList(this.cloudList + 1, GL11.GL_COMPILE);
        VertexArrayUploader.uploadPointer(vertexBuilder);
        GL11.glEndList();

        vertexBuilder.begin();
        vertexBuilder.vertex(x0, y1, z1);
        vertexBuilder.vertex(x0, y0, z1);
        vertexBuilder.vertex(x1, y0, z1);
        vertexBuilder.vertex(x1, y1, z1);
        vertexBuilder.end();
        GL11.glNewList(this.cloudList + 2, GL11.GL_COMPILE);
        VertexArrayUploader.uploadPointer(vertexBuilder);
        GL11.glEndList();

        vertexBuilder.begin();
        vertexBuilder.vertex(x0, y1, z1);
        vertexBuilder.vertex(x0, y1, z0);
        vertexBuilder.vertex(x0, y0, z0);
        vertexBuilder.vertex(x0, y0, z1);
        vertexBuilder.end();
        GL11.glNewList(this.cloudList + 3, GL11.GL_COMPILE);
        VertexArrayUploader.uploadPointer(vertexBuilder);
        GL11.glEndList();

        vertexBuilder.begin();
        vertexBuilder.vertex(x1, y0, z1);
        vertexBuilder.vertex(x1, y0, z0);
        vertexBuilder.vertex(x1, y1, z0);
        vertexBuilder.vertex(x1, y1, z1);
        vertexBuilder.end();
        GL11.glNewList(this.cloudList + 4, GL11.GL_COMPILE);
        VertexArrayUploader.uploadPointer(vertexBuilder);
        GL11.glEndList();
    }



}

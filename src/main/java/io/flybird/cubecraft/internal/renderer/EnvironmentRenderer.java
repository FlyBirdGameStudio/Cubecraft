package io.flybird.cubecraft.internal.renderer;

import io.flybird.util.GameSetting;
import io.flybird.cubecraft.client.render.renderer.IWorldRenderer;
import io.flybird.cubecraft.client.render.renderer.LevelRenderer;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.worldGen.noiseGenerator.PerlinNoise;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.starfish3d.platform.Window;
import io.flybird.starfish3d.render.Camera;
import io.flybird.starfish3d.render.GLUtil;
import io.flybird.starfish3d.render.culling.ProjectionMatrixFrustum;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.draw.VertexArrayUploader;
import io.flybird.starfish3d.render.drawcall.IRenderCall;
import io.flybird.starfish3d.render.drawcall.ListRenderCall;
import io.flybird.util.ColorUtil;
import io.flybird.util.math.AABB;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class EnvironmentRenderer extends IWorldRenderer {
    private static final int CLOUD_SIZE = 96;
    private static final double CLOUD_HEIGHT = 384;
    private final IRenderCall sky=new ListRenderCall();
    private final int cloudList = GL11.glGenLists(5);
    private final PerlinNoise noise = new PerlinNoise(new Random(world.getSeed()), 12);
    private final ProjectionMatrixFrustum frustum = new ProjectionMatrixFrustum(this.camera);

    public EnvironmentRenderer(Window window, IWorld world, Player player, Camera cam, GameSetting setting) {
        super(window, world, player, cam, setting);
        this.sky.allocate();
        this.updateCloud();
        this.updateSky();
    }


    @Override
    public void render(float interpolationTime) {
        LevelRenderer.setRenderState(this.setting, world);

        int d = this.setting.getValueAsInt("client.render.terrain.renderDistance", 4) * 16 + 1024;
        GLUtil.setupFog(d / 6, ColorUtil.int1Float1ToFloat4(world.getWorldInfo().fogColor(), 1.0f));
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        this.camera.setUpGlobalCamera(this.window);
        this.frustum.calculateFrustum();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_FOG);
        this.sky.call();

        GL11.glEnable(GL11.GL_FOG);
        int d1 = this.setting.getValueAsInt("client.render.terrain.renderDistance", 4) * 16 + 1024;
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        int d2 = d1 * 2;
        int allCloudCount=0;
        int visibleCloudCount=0;
        if (this.setting.getValueAsInt("client.render.environment.cloud.quality", 1) > 0) {
            for (long i = (long) (this.player.x - d2 - world.getTime() * 0.05) / CLOUD_SIZE; i < (this.player.x + d2 - world.getTime() * 0.05) / CLOUD_SIZE; i += 1) {
                for (long j = (long) (this.player.z - d2) / CLOUD_SIZE; j < (this.player.z + d2) / CLOUD_SIZE; j += 1) {
                    allCloudCount++;
                    double cloudX=i * CLOUD_SIZE + world.getTime() * 0.05;
                    double cloudY=CLOUD_HEIGHT;
                    double cloudZ=j * CLOUD_SIZE;
                    if (this.frustum.aabbVisible(camera.castAABB(new AABB(cloudX,cloudY,cloudZ,cloudX+CLOUD_SIZE,cloudY+24,cloudZ+CLOUD_SIZE)))) {
                        if (noise.getValue(i * 256, j * 256) > 16) {
                            visibleCloudCount++;

                            GL11.glPushMatrix();
                            camera.setupObjectCamera(new Vector3d(cloudX, cloudY, cloudZ));
                            GL11.glCallList(this.cloudList);
                            if (this.setting.getValueAsInt("client.render.environment.cloud.quality", 1) > 1) {
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
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        Registries.DEBUG_INFO.putI("cubecraft:environment_renderer/all_clouds",allCloudCount);
        Registries.DEBUG_INFO.putI("cubecraft:environment_renderer/visible_clouds",visibleCloudCount);

        LevelRenderer.closeState(this.setting);
    }

    public void updateSky() {
        int d2 = this.setting.getValueAsInt("client.render.terrain.renderDistance", 4);
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
        double y1 = (24);
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

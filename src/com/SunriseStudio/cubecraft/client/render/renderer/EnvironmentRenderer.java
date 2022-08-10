package com.sunrisestudio.cubecraft.client.render.renderer;

import com.sunrisestudio.cubecraft.GameSetting;
import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;
import com.sunrisestudio.grass3d.render.Camera;
import com.sunrisestudio.grass3d.render.draw.ChanneledVertexArrayBuilder;
import com.sunrisestudio.util.ColorUtil;
import com.sunrisestudio.util.container.BufferBuilder;
import com.sunrisestudio.grass3d.render.GLUtil;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayUploader;
import com.sunrisestudio.cubecraft.world.worldGen.noiseGenerator.PerlinNoise;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class EnvironmentRenderer extends IRenderer {
    public static final int SKY_SIZE=256;
    private static final int CLOUD_SIZE = 64;
    private static final double CLOUD_HEIGHT = 384;
    private final int skyList=GL11.glGenLists(1);
    private final int cloudList=GL11.glGenLists(5);
    private final PerlinNoise noise=new PerlinNoise(new Random(world.getSeed()),12);

    public EnvironmentRenderer(IWorldAccess w, Player p, Camera c) {
        super(w,p,c);
        updateSky();
        updateCloud();
    }

    @Override
    public void render(float interpolationTime) {
        GLUtil.enableBlend();
        int d= GameSetting.instance.renderDistance*16+1024;
        GLUtil.setupFog(d/6, BufferBuilder.from(ColorUtil.int1Float1ToFloat4(world.getWorldInfo().fogColor(),1.0f)));
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        this.camera.setUpGlobalCamera();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glCallList(this.skyList);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        for (long i = (long) (this.player.x-d- world.getTime()*0.05)/CLOUD_SIZE; i < (this.player.x+d- world.getTime()*0.05)/CLOUD_SIZE; i+=1) {
            for (long j = (long) (this.player.z-d)/CLOUD_SIZE; j < (this.player.z+d)/CLOUD_SIZE; j+=1) {
                if(noise.getValue(i*256,j*256)>16){
                    GL11.glPushMatrix();
                    camera.setupObjectCamera(new Vector3d(i*CLOUD_SIZE+ world.getTime()*0.05,CLOUD_HEIGHT,j*CLOUD_SIZE));
                    GL11.glCallList(this.cloudList);
                    if(noise.getValue((i-1)*256,j*256)<=16){
                        GL11.glCallList(cloudList+3);
                    }
                    if(noise.getValue((i+1)*256,j*256)<=16){
                        GL11.glCallList(cloudList+4);
                    }
                    if(noise.getValue(i*256,(j-1)*256)<=16){
                        GL11.glCallList(cloudList+1);
                    }
                    if(noise.getValue(i*256,(j+1)*256)<=16){
                        GL11.glCallList(cloudList+2);
                    }
                    GL11.glPopMatrix();
                }
            }
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GLUtil.disableBlend();
    }

    public void updateSky(){
        int vLength= GameSetting.instance.renderDistance*16;
        int hLength=GameSetting.instance.renderDistance*16+1024;
        ChanneledVertexArrayBuilder v=new ChanneledVertexArrayBuilder(1048576);
        v.begin();
        for (int i = -hLength; i <hLength ; i+=SKY_SIZE) {
            for (int j = -hLength; j< hLength; j+=SKY_SIZE) {
                v.color(world.getWorldInfo().skyColor());
                v.vertex(i, vLength,j+SKY_SIZE);
                v.vertex(i+SKY_SIZE, vLength, j+SKY_SIZE);
                v.vertex(i+SKY_SIZE, vLength, j);
                v.vertex(i, vLength, j);
                v.color(world.getWorldInfo().voidColor());
                v.vertex(i+SKY_SIZE,-vLength,j+SKY_SIZE);
                v.vertex(i, -vLength, j+SKY_SIZE);
                v.vertex(i, -vLength, j);
                v.vertex(i+SKY_SIZE, -vLength, j);
            }
        }

        v.color(world.getWorldInfo().fogColor());
        for (int i = -vLength; i <vLength ; i+=SKY_SIZE) {
            for (int j = -hLength; j < hLength; j += SKY_SIZE) {

                v.vertex(hLength, i + SKY_SIZE, j + SKY_SIZE);
                v.vertex(hLength, i + SKY_SIZE, j);
                v.vertex(hLength, i, j);
                v.vertex(hLength, i, j + SKY_SIZE);

                v.vertex(-hLength, i + SKY_SIZE, j + SKY_SIZE);
                v.vertex(-hLength, i + SKY_SIZE, j);
                v.vertex(-hLength, i, j);
                v.vertex(-hLength, i, j + SKY_SIZE);
            }
        }
        for (int i = -hLength; i <hLength ; i+=SKY_SIZE) {
            for (int j = -vLength; j< vLength; j+=SKY_SIZE) {
                v.vertex(i,j+SKY_SIZE,hLength);
                v.vertex(i+SKY_SIZE, j+SKY_SIZE,hLength);
                v.vertex(i+SKY_SIZE,j,hLength);
                v.vertex(i,j, hLength);

                v.vertex(i,j+SKY_SIZE,-hLength);
                v.vertex(i+SKY_SIZE, j+SKY_SIZE,-hLength);
                v.vertex(i+SKY_SIZE,j,-hLength);
                v.vertex(i,j, -hLength);
            }
        }
        v.end();
        GL11.glNewList(this.skyList,GL11.GL_COMPILE);
        IVertexArrayUploader.createNewPointedUploader().upload(v);
        GL11.glEndList();
    }

    public void updateCloud(){
        ChanneledVertexArrayBuilder vertexBuilder=new ChanneledVertexArrayBuilder(24);
        double x0 = (0);
        double x1 = (CLOUD_SIZE);
        double y0 = (0);
        double y1 = (16);
        double z0 =(0);
        double z1 = (CLOUD_SIZE);


        vertexBuilder.begin();
        float[] col=ColorUtil.int1Float1ToFloat4(world.getWorldInfo().cloudColor(),0.8f);
        vertexBuilder.color(col[0],col[1],col[2],col[3]);
        vertexBuilder.vertex(x0, y0, z1);
        vertexBuilder.vertex(x0, y0, z0);
        vertexBuilder.vertex(x1, y0, z0);
        vertexBuilder.vertex(x1, y0, z1);

        vertexBuilder.vertex(x1, y1, z1);
        vertexBuilder.vertex(x1, y1, z0);
        vertexBuilder.vertex(x0, y1, z0);
        vertexBuilder.vertex(x0, y1, z1);
        vertexBuilder.end();
        GL11.glNewList(this.cloudList,GL11.GL_COMPILE);
        IVertexArrayUploader.createNewPointedUploader().upload(vertexBuilder);
        GL11.glEndList();


        vertexBuilder.begin();
        vertexBuilder.vertex(x0, y1, z0);
        vertexBuilder.vertex(x1, y1, z0);
        vertexBuilder.vertex(x1, y0, z0);
        vertexBuilder.vertex(x0, y0, z0);
        vertexBuilder.end();
        GL11.glNewList(this.cloudList+1,GL11.GL_COMPILE);
        IVertexArrayUploader.createNewPointedUploader().upload(vertexBuilder);
        GL11.glEndList();

        vertexBuilder.begin();
        vertexBuilder.vertex(x0, y1, z1);
        vertexBuilder.vertex(x0, y0, z1);
        vertexBuilder.vertex(x1, y0, z1);
        vertexBuilder.vertex(x1, y1, z1);
        vertexBuilder.end();
        GL11.glNewList(this.cloudList+2,GL11.GL_COMPILE);
        IVertexArrayUploader.createNewPointedUploader().upload(vertexBuilder);
        GL11.glEndList();

        vertexBuilder.begin();
        vertexBuilder.vertex(x0, y1, z1);
        vertexBuilder.vertex(x0, y1, z0);
        vertexBuilder.vertex(x0, y0, z0);
        vertexBuilder.vertex(x0, y0, z1);
        vertexBuilder.end();
        GL11.glNewList(this.cloudList+3,GL11.GL_COMPILE);
        IVertexArrayUploader.createNewPointedUploader().upload(vertexBuilder);
        GL11.glEndList();

        vertexBuilder.begin();
        vertexBuilder.vertex(x1, y0, z1);
        vertexBuilder.vertex(x1, y0, z0);
        vertexBuilder.vertex(x1, y1, z0);
        vertexBuilder.vertex(x1, y1, z1);
        vertexBuilder.end();
        GL11.glNewList(this.cloudList+4,GL11.GL_COMPILE);
        IVertexArrayUploader.createNewPointedUploader().upload(vertexBuilder);
        GL11.glEndList();
    }

    @Override
    public void chunkUpdate(long x, long y, long z) {

    }
}

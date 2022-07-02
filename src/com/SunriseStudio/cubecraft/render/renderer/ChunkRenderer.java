package com.SunriseStudio.cubecraft.render.renderer;

import com.SunriseStudio.cubecraft.GameSetting;

import com.SunriseStudio.cubecraft.render.object.RenderChunkPos;

import com.SunriseStudio.cubecraft.resources.textures.Texture2D;
import com.SunriseStudio.cubecraft.render.object.RenderChunk;

import com.SunriseStudio.cubecraft.util.BufferBuilder;
import com.SunriseStudio.cubecraft.util.LogHandler;
import com.SunriseStudio.cubecraft.util.collections.ArrayQueue;

import com.SunriseStudio.cubecraft.util.collections.HashMapSet;
import com.SunriseStudio.cubecraft.util.collections.keyMap.KeyMap;

import com.SunriseStudio.cubecraft.util.grass3D.render.GLUtil;
import com.SunriseStudio.cubecraft.util.grass3D.render.culling.Frustum;
import com.SunriseStudio.cubecraft.util.grass3D.render.culling.ICuller;
import com.SunriseStudio.cubecraft.util.grass3D.render.draw.IVertexArrayUploader;
import com.SunriseStudio.cubecraft.util.grass3D.render.multiThread.DrawCompile;
import com.SunriseStudio.cubecraft.util.grass3D.render.multiThread.MultiRenderCompileService;

import com.SunriseStudio.cubecraft.render.sort.DistanceSorter;
import com.SunriseStudio.cubecraft.util.math.AABB;
import com.SunriseStudio.cubecraft.world.Level;
import com.SunriseStudio.cubecraft.world.entity._Player;
import org.joml.Vector3d;
import org.lwjgl.opengl.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class ChunkRenderer extends IWorldRenderer {
    public Texture2D terrain=new Texture2D(false,true);
    public LogHandler logHandler=LogHandler.create("ChunkRenderer","client");

    private final Frustum frustum=new Frustum();

    public HashMapSet<RenderChunkPos, RenderChunk> chunks=new HashMapSet<>();
    public ArrayQueue<RenderChunkPos> updateQueue=new ArrayQueue<>();
    public MultiRenderCompileService<RenderChunk> updateService=new MultiRenderCompileService<>(GameSetting.instance.maxDrawThread);

    private long playerGridX,playerGridY,playerGridZ;
    private double lastRotX,lastRotY,lastRotZ;

    public ChunkRenderer(Level w, _Player p) {
        super(w, p);
        terrain.generateTexture();
        terrain.load("/resource/textures/blocks/terrain.png");
        terrain.buildMipmap();
    }
    public int allCount;
    public int visibleCount;
    public int updateCount;



    @Override
    public void render(float interpolationTime){
        this.frustum.update();
        GLUtil.setupFog(GameSetting.instance.renderDistance*12, BufferBuilder.getF(world.getFogColor()));
        allCount=0;
        visibleCount=0;
        updateCount=0;

        GLUtil.setupAlphaBlend();
        this.camera.setUpGlobalCamera();

        //handle chunk update
        this.updateChunks();


        //check for chunk add and remove
        if(
                (long) (this.camera.getPosition().x/16)!=this.playerGridX||
                (long) (this.camera.getPosition().y/16)!=this.playerGridY||
                (long)(this.camera.getPosition().z/16)!=this.playerGridZ||
                (this.camera.getRotation().x)!=this.lastRotX||
                (this.camera.getRotation().y)!=this.lastRotY||
                (this.camera.getRotation().z)!=this.lastRotZ
        ) {
            this.checkForChunkAdd();
        }
        this.playerGridX= (long) (this.camera.getPosition().x/16);
        this.playerGridY= (long) (this.camera.getPosition().y/16);
        this.playerGridZ= (long) (this.camera.getPosition().z/16);
        this.lastRotX=this.camera.getRotation().x;
        this.lastRotY=this.camera.getRotation().y;
        this.lastRotZ=this.camera.getRotation().z;


        //render chunks
        this.terrain.bind();
        for (RenderChunk c:this.chunks.map.values()){
                GL11.glPushMatrix();
                this.camera.setupObjectCamera(new Vector3d(c.x * 16, c.y * 16, c.z * 16));
                if (
                        this.camera.objectDistanceSmallerThan(new Vector3d(c.x * 16, c.y * 16, c.z * 16), GameSetting.instance.renderDistance * 16)&&
                        this.frustum.aabbVisible(RenderChunk.getAABBFromPos(c.getKey(),camera))
                ) {
                    c.render();
                    visibleCount++;
                }
                GL11.glPopMatrix();
                allCount++;
        }
        this.terrain.unbind();
        updateCount=this.updateQueue.size();
    }

    private void updateChunks() {
        this.updateQueue.removeIf(c -> !this.camera.objectDistanceSmallerThan(new Vector3d(c.x() * 16, c.y() * 16, c.z() * 16), GameSetting.instance.renderDistance * 16));
        //offer update
        if(updateQueue.size()>=GameSetting.instance.maxUpdatesPreFrame) {
            List<RenderChunkPos> updatePositionList=this.updateQueue.pollAll(GameSetting.instance.maxUpdatesPreFrame);
            for (RenderChunkPos pos : updatePositionList) {
                RenderChunk chunk= this.chunks.get(pos);
                if(chunk==null){
                    chunk=new RenderChunk(world,pos.x(),pos.y(),pos.z());
                    this.chunks.add(chunk);
                }
                this.updateService.startDrawing(chunk);
            }
        }
        //receive chunk update and draw
        if (this.updateService.getResultSize() >= 1) {
           this.updateService.get().draw();
        }
    }

    /**
     * set update ut pos
     * if chunk not exist it will load the chunk
     * @param cx x chunk pos
     * @param cy y chunk pos
     * @param cz z chunk pos
     */
    public void setUpdateAt(long cx, int cy, long cz) {
        this.updateQueue.add(new RenderChunkPos(cx,cy,cz));
    }

    @Override
    public void chunkUpdate(long x, long y, long z) {
        setUpdateAt(x/16, Math.toIntExact(y / 16),z/16);
    }

    //try to add chunk in distance but not exist
    public void checkForChunkAdd(){
        ArrayList<RenderChunkPos> adds=new ArrayList<>();
        long playerCX = (long) (this.camera.getPosition().x / 16);
        long playerCZ = (long) (this.camera.getPosition().z / 16);
        int playerCY = (int) (this.camera.getPosition().y / 16);
        for (long cx = playerCX - GameSetting.instance.renderDistance; cx <= playerCX + GameSetting.instance.renderDistance; cx++) {
            for (long cz = playerCZ - GameSetting.instance.renderDistance; cz <= playerCZ + GameSetting.instance.renderDistance; cz++) {
                for (int cy = playerCY - GameSetting.instance.renderDistance; cy <= playerCY + GameSetting.instance.renderDistance; cy++) {
                    if (
                            cy >= 0 &&
                            cy < 512 / 16&&
                            camera.objectDistanceSmallerThan(new Vector3d(cx*16,cy*16,cz*16),GameSetting.instance.renderDistance*16)&&
                            !this.chunks.contains(new RenderChunkPos(cx,cy,cz))
                    ) {
                        adds.add(new RenderChunkPos(cx,cy,cz));
                    }
                }
            }
        }
        adds.sort((o1, o2) -> -Double.compare(o1.distanceTo(player), o2.distanceTo(player)));
        for (RenderChunkPos p:adds) {
            if (
                    frustum.aabbVisible(RenderChunk.getAABBFromPos(p, camera))
            ) {
                updateQueue.add(p);
            }
        }

        Iterator<RenderChunk> iterator = this.chunks.map.values().iterator();
        while (iterator.hasNext()) {
            RenderChunk c = iterator.next();
            if (!this.camera.objectDistanceSmallerThan(new Vector3d(c.x * 16, c.y * 16, c.z * 16), GameSetting.instance.renderLoadingDistance * 16)) {
                GL11.glDeleteLists(c.getList(), 2);
                iterator.remove();
            }
        }
    }
}




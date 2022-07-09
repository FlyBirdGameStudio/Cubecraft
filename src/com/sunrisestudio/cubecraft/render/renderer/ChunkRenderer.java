package com.sunrisestudio.cubecraft.render.renderer;

import com.sunrisestudio.cubecraft.GameSetting;

import com.sunrisestudio.cubecraft.render.Camera;
import com.sunrisestudio.cubecraft.render.object.RenderChunkPos;

import com.sunrisestudio.grass3d.render.textures.Texture2D;
import com.sunrisestudio.cubecraft.render.object.RenderChunk;

import com.sunrisestudio.util.container.buffer.BufferBuilder;
import com.sunrisestudio.util.LogHandler;
import com.sunrisestudio.util.container.ArrayQueue;

import com.sunrisestudio.util.container.HashMapSet;

import com.sunrisestudio.grass3d.render.GLUtil;
import com.sunrisestudio.grass3d.render.culling.Frustum;
import com.sunrisestudio.grass3d.render.multiThread.MultiRenderCompileService;

import com.sunrisestudio.cubecraft.world._Level;
import com.sunrisestudio.cubecraft.world.entity._Player;
import org.joml.Vector3d;
import org.lwjgl.opengl.*;

import java.util.*;

public class ChunkRenderer extends IWorldRenderer {
    public Texture2D terrain=new Texture2D(false,true);
    public LogHandler logHandler=LogHandler.create("ChunkRenderer","client");

    private final Frustum frustum=new Frustum();

    public HashMapSet<RenderChunkPos, RenderChunk> chunks=new HashMapSet<>();
    public ArrayQueue<RenderChunkPos> updateQueue=new ArrayQueue<>();
    public MultiRenderCompileService<RenderChunk> updateService=new MultiRenderCompileService<>(GameSetting.instance.maxDrawThread);

    private long playerGridX,playerGridY,playerGridZ;
    private double lastRotX,lastRotY,lastRotZ;

    public ChunkRenderer(_Level w, _Player p, Camera c) {
        super(w, p,c);
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

        GLUtil.enableBlend();
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
                if (
                        this.camera.objectDistanceSmallerThan(new Vector3d(c.x * 16, c.y * 16, c.z * 16),GameSetting.instance.renderDistance * 16)
                ) {

                    GL11.glPushMatrix();
                    this.camera.setupObjectCamera(new Vector3d(c.x * 16, c.y * 16, c.z * 16));
                    c.render();
                    visibleCount++;
                    GL11.glPopMatrix();
                }
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
            try {
                this.updateService.get().draw();
            }catch (NullPointerException e){
                logHandler.warning("null compile wtf?");
            }

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
        for (long cx = playerCX -1- GameSetting.instance.renderDistance; cx <= playerCX + GameSetting.instance.renderDistance; cx++) {
            for (long cz = playerCZ - 1-GameSetting.instance.renderDistance; cz <= playerCZ + GameSetting.instance.renderDistance; cz++) {
                for (int cy = playerCY - 1-GameSetting.instance.renderDistance; cy <= playerCY + GameSetting.instance.renderDistance; cy++) {
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
                    frustum.aabbVisible(RenderChunk.getAABBFromPos(p,camera))
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




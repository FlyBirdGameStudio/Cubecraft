package com.sunrisestudio.cubecraft.world.worldGen;

import com.sunrisestudio.cubecraft.Start;
import com.sunrisestudio.cubecraft.world.access.IWorldAccess;
import com.sunrisestudio.cubecraft.world.chunk.Chunk;
import com.sunrisestudio.cubecraft.world.chunk.ChunkPos;

import java.io.File;
import java.util.HashMap;

public class WorldProvider {
    public static final int REGION_GRID_SIZE=64;
    public static HashMap<String,WorldProvider> providers=new HashMap<>();

    public IWorldAccess dimension;

    public static WorldProvider getProvider(String id) {
        return providers.get(id);
    }

    /**
     * this method will try to load chunk on disk first. If result null,it will try to generate a new chunk.
     * @param pos position
     * @return a brand-new chunk
     */
    public Chunk getChunk(ChunkPos pos){
        Chunk c=loadChunk(pos);
        if(c==null){
            c=genChunk(pos);
        }
        return c;
    }

    private Chunk loadChunk(ChunkPos pos){
        long regionX=pos.x()/REGION_GRID_SIZE;
        long regionY=pos.y()/REGION_GRID_SIZE;
        long regionZ=pos.z()/REGION_GRID_SIZE;


        //for example:region(3,3,4)is"gamepath/saves/world/dim-0/3_3_4.region"
        String region=Start.getGamePath()+"/saves/"+dimension.getLevel().getName()+"/"+dimension.getID()+
                "/"+regionX+"_"+regionY+"_"+regionZ+".region";
        File regionFile=new File(region);
        return null;
    }

    private Chunk genChunk(ChunkPos pos){
        Chunk chunk=new Chunk(this.dimension,pos);

        return chunk;
    }
}
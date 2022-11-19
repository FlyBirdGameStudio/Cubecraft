package io.flybird.cubecraft.world.chunk;

import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.util.container.HashMapSet;
import io.flybird.util.container.Pair;

import java.util.HashMap;

public class ChunkProcessTask {
    private final HashMap<ChunkLoadTaskType,Integer> levels=new HashMap<>();


    private Chunk chunk;

    public ChunkProcessTask(Chunk chunk) {
        this.chunk=chunk;
        this.levels.put(ChunkLoadTaskType.BLOCK_ENTITY_PROCESS,0);
        this.levels.put(ChunkLoadTaskType.INBOUND_ENTITY_PROCESS,0);
        this.levels.put(ChunkLoadTaskType.BLOCK_RANDOM_TICK,0);
        this.levels.put(ChunkLoadTaskType.INBOUND_BLOCK_UPDATE_PROCESS,0);
    }

    public void run(){
        if(this.shouldProcess(ChunkLoadTaskType.INBOUND_BLOCK_UPDATE_PROCESS)){

        }
        if(this.shouldProcess(ChunkLoadTaskType.BLOCK_ENTITY_PROCESS)){
            for (BlockState s:this.chunk.getBlockEntityList())

            for (int x=0;x<Chunk.WIDTH;x++){
                for (int y=0;y<Chunk.HEIGHT;y++){
                    for (int z=0;z<Chunk.WIDTH;z++){
                        this.chunk.getBlockState(x,y,z).tick(
                                this.chunk.getWorld(),
                                this.chunk.getKey().toWorldPosX(x),
                                this.chunk.getKey().toWorldPosY(y),
                                this.chunk.getKey().toWorldPosZ(z)
                        );
                    }
                }
            }
        }
    }

    public boolean shouldProcess(ChunkLoadTaskType type){
        boolean process=false;
        int time=this.levels.get(type);
        if(time>0){
            process=true;
            time--;
        }
        this.levels.put(type,time);
        return process;
    }
}

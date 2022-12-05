package io.flybird.cubecraft.world.chunk;

public class ChunkLoadTicket {
    private ChunkLoadLevel chunkLoadLevel;
    private int time;

    public ChunkLoadTicket(ChunkLoadLevel loadLevel,int ticks){
        this.time=ticks;
        this.chunkLoadLevel=loadLevel;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }


    public void addToTask(ChunkProcessTask task) {
        if(this.chunkLoadLevel.containsLevel(ChunkLoadLevel.Entity_TICKING)){
            task.addTime(ChunkLoadTaskType.INBOUND_ENTITY_PROCESS,this.time);
        }
        if(this.chunkLoadLevel.containsLevel(ChunkLoadLevel.Block_Entity_TICKING)){
            task.addTime(ChunkLoadTaskType.INBOUND_ENTITY_PROCESS,this.time);
        }
        if(this.chunkLoadLevel.containsLevel(ChunkLoadLevel.Block_Random_TICKING)){
            task.addTime(ChunkLoadTaskType.BLOCK_RANDOM_TICK,this.time);
        }
        if(this.chunkLoadLevel.containsLevel(ChunkLoadLevel.Block_TICKING)){
            task.addTime(ChunkLoadTaskType.INBOUND_BLOCK_UPDATE_PROCESS,this.time);
        }
        if(this.chunkLoadLevel.containsLevel(ChunkLoadLevel.None_TICKING)){
            task.addTime(ChunkLoadTaskType.KEEP_DATA_TICK,this.time);
        }
    }

    public ChunkLoadLevel getChunkLoadLevel() {
        return chunkLoadLevel;
    }

    public void setChunkLoadLevel(ChunkLoadLevel chunkLoadLevel) {
        this.chunkLoadLevel = chunkLoadLevel;
    }
}

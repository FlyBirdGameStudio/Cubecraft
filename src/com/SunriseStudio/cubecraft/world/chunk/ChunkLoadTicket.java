package com.sunrisestudio.cubecraft.world.chunk;

import com.sunrisestudio.util.container.options.Option;

public class ChunkLoadTicket {
    private ChunkLoadLevel chunkLoadLevel;
    private int time;

    public ChunkLoadTicket(ChunkLoadLevel loadLevel,int ticks){
        this.time=ticks;
        this.chunkLoadLevel=loadLevel;
    }

    public void setChunkLoadLevel(ChunkLoadLevel chunkLoadLevel) {
        this.chunkLoadLevel = chunkLoadLevel;
    }

    public ChunkLoadLevel getChunkLoadLevel() {
        return chunkLoadLevel;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void tickTime(){
        this.time--;
    }

    public static ChunkLoadTicket fromDistance(int range,int ticks) {
        Option opt=new Option("setting.server");
        if(range< (int)opt.getOrDefault("setting.server.sim_entity_dist",8)){
            return new ChunkLoadTicket(ChunkLoadLevel.Entity_TICKING,ticks);
        } else if (range<(int)opt.getOrDefault("setting.server.sim_block_dist",12)) {
            return new ChunkLoadTicket(ChunkLoadLevel.Block_TICKING,ticks);
        }else{
            return new ChunkLoadTicket(ChunkLoadLevel.None_TICKING,ticks);
        }
    }
}

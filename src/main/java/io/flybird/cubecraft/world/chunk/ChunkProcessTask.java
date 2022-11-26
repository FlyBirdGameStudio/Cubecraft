package io.flybird.cubecraft.world.chunk;

import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.util.container.CollectionUtil;
import io.flybird.util.container.HashMapSet;
import io.flybird.util.container.Pair;

import java.util.HashMap;
import java.util.Random;

public class ChunkProcessTask {
    private final HashMap<ChunkLoadTaskType, Integer> levels = new HashMap<>();
    private final Chunk chunk;

    public ChunkProcessTask(Chunk chunk) {
        this.chunk = chunk;
        this.levels.put(ChunkLoadTaskType.BLOCK_ENTITY_PROCESS, 0);
        this.levels.put(ChunkLoadTaskType.INBOUND_ENTITY_PROCESS, 0);//process in ServerWorld
        this.levels.put(ChunkLoadTaskType.BLOCK_RANDOM_TICK, 0);
        this.levels.put(ChunkLoadTaskType.INBOUND_BLOCK_UPDATE_PROCESS, 0);//process in ServerWorld
        this.levels.put(ChunkLoadTaskType.KEEP_DATA_TICK, 0);
    }

    public void run() {
        if (this.shouldProcess(ChunkLoadTaskType.BLOCK_ENTITY_PROCESS)) {
            for (BlockState s : this.chunk.getBlockEntityList()) {
                s.tick(this.chunk.getWorld());
            }
        }
        if (this.shouldProcess(ChunkLoadTaskType.BLOCK_RANDOM_TICK)) {
            Random rand = new Random();
            for (int i = 0; i < 10; i++) {//rand tick speed qwq
                chunk.getBlockState(
                        rand.nextInt(16),
                        rand.nextInt(16),
                        rand.nextInt(16)
                ).randomTick(chunk.getWorld());
            }
        }
        shouldProcess(ChunkLoadTaskType.KEEP_DATA_TICK);
    }

    public boolean shouldProcess(ChunkLoadTaskType type) {
        boolean process = false;
        int time = this.levels.get(type);
        if (time > 0) {
            process = true;
            time--;
        }
        this.levels.put(type, time);

        return process;
    }

    public void addTime(ChunkLoadTaskType type, int time) {
        int time2 = this.levels.get(type);
        this.levels.put(type, Math.max(time, time2));
    }

    public boolean shouldLoad() {
        return this.levels.get(ChunkLoadTaskType.KEEP_DATA_TICK)>0;
    }
}

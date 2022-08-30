package com.flybirdstudio.cubecraft.world.chunk;

public enum ChunkLoadLevel {
    Entity_TICKING(2),
    Block_TICKING(1),
    None_TICKING(0);

    final int order;

    ChunkLoadLevel(int order){
        this.order=order;
    }

    public boolean containsLevel(ChunkLoadLevel loadLevel){
        return this.order>=loadLevel.order;
    }
}

package com.flybirdstudio.cubecraft.world;

import com.flybirdstudio.cubecraft.client.Cubecraft;
import com.flybirdstudio.cubecraft.event.block.BlockChangeEvent;
import com.flybirdstudio.cubecraft.event.world.ChunkLoadEvent;
import com.flybirdstudio.cubecraft.world.block.EnumFacing;
import com.flybirdstudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.flybirdstudio.cubecraft.world.chunk.ChunkPos;

public class ClientWorld extends IWorld {
    final Cubecraft client;

    public ClientWorld(LevelInfo levelInfo, Cubecraft client) {
        super(levelInfo);
        this.client = client;
    }

    @Override
    public void loadChunk(ChunkPos p, ChunkLoadTicket ticket) {
        this.getEventBus().callEvent(new ChunkLoadEvent(client.getPlayer(), p.x(), p.y(), p.z(), ticket));
    }

    @Override
    public void setBlock(long x, long y, long z, String id, EnumFacing facing) {
        super.setBlock(x, y, z, id, facing);
        this.getEventBus().callEvent(new BlockChangeEvent(x,y,z,getBlockState(x,y,z)));
    }
}

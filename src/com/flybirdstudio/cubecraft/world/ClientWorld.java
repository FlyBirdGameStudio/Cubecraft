package com.flybirdstudio.cubecraft.world;

import com.flybirdstudio.cubecraft.client.Cubecraft;
import com.flybirdstudio.cubecraft.event.block.BlockChangeEvent;
import com.flybirdstudio.cubecraft.event.world.ChunkLoadEvent;
import com.flybirdstudio.cubecraft.registery.Registery;
import com.flybirdstudio.cubecraft.world.block.BlockFacing;
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
        Registery.getClientWorldEventBus().callEvent(new ChunkLoadEvent(client.getPlayer(), p.x(), p.y(), p.z(), ticket));
    }

    @Override
    public void setBlock(long x, long y, long z, String id, BlockFacing facing) {
        super.setBlock(x, y, z, id, facing);
        Registery.getClientWorldEventBus().callEvent(new BlockChangeEvent(x,y,z,getBlockState(x,y,z)));
    }
}

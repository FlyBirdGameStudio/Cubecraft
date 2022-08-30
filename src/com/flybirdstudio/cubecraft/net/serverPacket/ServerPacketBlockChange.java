package com.flybirdstudio.cubecraft.net.serverPacket;

import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.util.net.Packet;

public record ServerPacketBlockChange(long x, long y, long z, BlockState bs)implements Packet {
    @Override
    public String getType() {
        return "cubecraft:server_block_change";
    }
}

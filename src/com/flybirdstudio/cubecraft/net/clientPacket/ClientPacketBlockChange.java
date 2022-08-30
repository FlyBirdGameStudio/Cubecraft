package com.flybirdstudio.cubecraft.net.clientPacket;

import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.util.net.Packet;

public record ClientPacketBlockChange(String uuid, long x, long y, long z, BlockState state) implements Packet {
    @Override
    public String getType() {
        return "cubecraft:client_block_change";
    }
}

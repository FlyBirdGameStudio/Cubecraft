package io.flybird.cubecraft.net.serverPacket;

import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.util.net.Packet;

public record ServerPacketBlockChange(long x, long y, long z, BlockState bs)implements Packet {
    @Override
    public String getType() {
        return "cubecraft:server_block_change";
    }
}

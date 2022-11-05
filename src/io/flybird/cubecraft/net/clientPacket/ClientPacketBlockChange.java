package io.flybird.cubecraft.net.clientPacket;

import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.util.net.Packet;

public record ClientPacketBlockChange(String uuid, long x, long y, long z, BlockState state) implements Packet {
    @Override
    public String getType() {
        return "cubecraft:client_block_change";
    }
}

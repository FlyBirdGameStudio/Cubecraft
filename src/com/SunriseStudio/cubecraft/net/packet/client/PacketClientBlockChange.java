package com.sunrisestudio.cubecraft.net.packet.client;

import com.sunrisestudio.util.container.BufferBuilder;
import com.sunrisestudio.cubecraft.world.block.BlockState;
import io.netty.buffer.ByteBuf;

public record PacketClientBlockChange(BlockState newBlockState)implements ClientPacket {
    @Override
    public String getType() {
        return "cubecraft:block_update";
    }

    @Override
    public ByteBuf serialize() {
        return BufferBuilder.fromNBT(newBlockState.getData(),128);
    }
}

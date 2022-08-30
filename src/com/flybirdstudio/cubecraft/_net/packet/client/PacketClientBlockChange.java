package com.flybirdstudio.cubecraft.client.net.packet.client;

import com.flybirdstudio.util.container.BufferBuilder;
import com.flybirdstudio.cubecraft.world.block.BlockState;
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

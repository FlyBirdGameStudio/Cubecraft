package com.sunrisestudio.cubecraft.net.packet.client;

import com.sunrisestudio.util.container.buffer.NettyBufferBuilder;
import com.sunrisestudio.cubecraft.world.block.Block;
import io.netty.buffer.ByteBuf;

public record PacketClientBlockChange(Block newBlock)implements ClientPacket {
    @Override
    public String getType() {
        return "cubecraft:block_update";
    }

    @Override
    public ByteBuf serialize() {
        return NettyBufferBuilder.fromNBT(newBlock.getData(),128);
    }
}

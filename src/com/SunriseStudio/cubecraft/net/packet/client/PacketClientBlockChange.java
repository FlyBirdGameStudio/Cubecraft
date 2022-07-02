package com.SunriseStudio.cubecraft.net.packet.client;

import com.SunriseStudio.cubecraft.net.packet.Packet;
import com.SunriseStudio.cubecraft.util.NettyBufferBuilder;
import com.SunriseStudio.cubecraft.world.block.Block;
import io.netty.buffer.ByteBuf;

public record PacketClientBlockChange(Block newBlock)implements Packet {
    @Override
    public String getType() {
        return "cubecraft:block_update";
    }

    @Override
    public ByteBuf serialize() {
        return NettyBufferBuilder.fromNBT(newBlock.getData(),128);
    }
}

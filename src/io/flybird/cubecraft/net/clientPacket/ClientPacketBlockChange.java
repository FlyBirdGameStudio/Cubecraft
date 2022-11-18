package io.flybird.cubecraft.net.clientPacket;

import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.util.net.IOByteBuffer;
import io.flybird.util.net.Packet;
import io.netty.buffer.ByteBuf;

/**
 * this packet hold a block change event to server,
 * use token access.
 */
public class ClientPacketBlockChange implements Packet {
    public String token,uuid;
    public long x,y,z;
    public BlockState state;

    public ClientPacketBlockChange(String token, String uuid, long x, long y, long z, BlockState state) {
        this.token = token;
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.state = state;
    }

    public ClientPacketBlockChange(){}

    @Override
    public String getType() {
        return "cubecraft:client_block_change";
    }

    @Override
    public void writePacketData(ByteBuf buffer) {

    }

    @Override
    public void readPacketData(ByteBuf buffer) {

    }
}

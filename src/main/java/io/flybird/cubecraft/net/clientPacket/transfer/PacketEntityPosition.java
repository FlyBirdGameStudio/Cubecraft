package io.flybird.cubecraft.net.clientPacket.transfer;

import io.flybird.cubecraft.net.PacketConstructor;
import io.flybird.cubecraft.net.clientPacket.Packet;
import io.flybird.cubecraft.world.entity.EntityLocation;
import io.netty.buffer.ByteBuf;

/**
 * this packet transform a position update.
 * it could be processed or send on both side
 */
public class PacketEntityPosition implements Packet {
    private String uuid;
    private EntityLocation newLoc=new EntityLocation(0,0,0,0,0,0);

    public PacketEntityPosition(String uuid, EntityLocation newLoc) {
        this.uuid = uuid;
        this.newLoc = newLoc;
    }

    @PacketConstructor
    public PacketEntityPosition(){}

    @Override
    public void writePacketData(ByteBuf buffer) {
        buffer.writeDouble(newLoc.x)
                .writeDouble(newLoc.y)
                .writeDouble(newLoc.z)
                .writeDouble(newLoc.xRot)
                .writeDouble(newLoc.yRot)
                .writeDouble(newLoc.zRot);
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        newLoc.x=buffer.readDouble();
        newLoc.y=buffer.readDouble();
        newLoc.z=buffer.readDouble();
        newLoc.xRot=buffer.readDouble();
        newLoc.yRot=buffer.readDouble();
        newLoc.zRot=buffer.readDouble();
    }

    public String getUuid() {
        return uuid;
    }

    public EntityLocation getNewLoc() {
        return newLoc;
    }

}
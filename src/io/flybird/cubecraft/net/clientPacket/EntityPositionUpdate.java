package io.flybird.cubecraft.net.clientPacket;

import io.flybird.cubecraft.net.PacketConstructor;
import io.flybird.cubecraft.world.entity.EntityLocation;
import io.flybird.cubecraft.net.Packet;
import io.flybird.util.file.nbt.NBTBuilder;
import io.flybird.util.file.nbt.tag.NBTTagCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

/**
 * this packet transform a position update.
 * it could be processed or send on both side
 */
public class EntityPositionUpdate implements Packet {
    private String uuid;
    private EntityLocation newLoc=new EntityLocation(0,0,0,0,0,0);
    private EntityLocation oldLoc=new EntityLocation(0,0,0,0,0,0);
    private String token;

    public EntityPositionUpdate(String token, String uuid, EntityLocation newLoc, EntityLocation oldLoc) {
        this.uuid = uuid;
        this.newLoc = newLoc;
        this.oldLoc = oldLoc;
        this.token=token;
    }

    @PacketConstructor
    public EntityPositionUpdate(){}

    @Override
    public String getType() {
        return "cubecraft:client_position_change";
    }

    @Override
    public void writePacketData(ByteBuf buffer) {
        NBTTagCompound tag=new NBTTagCompound();
        tag.setCompoundTag("newLoc",newLoc.getData());
        tag.setCompoundTag("oldLoc",oldLoc.getData());
        tag.setString("uuid",uuid);
        tag.setString("token",token);
        try {
            NBTBuilder.write(tag,new ByteBufOutputStream(buffer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        NBTTagCompound tag;
        try {
            tag = (NBTTagCompound) NBTBuilder.read(new ByteBufInputStream(buffer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.uuid=tag.getString("uuid");
        this.token=tag.getString("token");
        this.newLoc.setData(tag.getCompoundTag("newLoc"));
        this.oldLoc.setData(tag.getCompoundTag("oldLoc"));
    }

    public String getUuid() {
        return uuid;
    }

    public EntityLocation getNewLoc() {
        return newLoc;
    }

    public EntityLocation getOldLoc() {
        return oldLoc;
    }
}
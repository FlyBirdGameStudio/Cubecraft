package io.flybird.cubecraft.internal.net.packet.connect;

import io.flybird.cubecraft.network.packet.Packet;
import io.flybird.cubecraft.network.packet.PacketConstructor;
import io.flybird.cubecraft.world.LevelInfo;
import io.flybird.util.container.namespace.TypeItem;
import io.flybird.util.file.NBTBuilder;
import io.flybird.util.file.nbt.NBTTagCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.Date;

@TypeItem("cubecraft:join_world_response")
public class PacketPlayerJoinWorldResponse implements Packet {
    private String id="";
    private LevelInfo info=new LevelInfo("","",0,new Date(System.currentTimeMillis()),false);

    public PacketPlayerJoinWorldResponse(String id, LevelInfo info) {
        this.id = id;
        this.info = info;
    }

    @PacketConstructor
    public PacketPlayerJoinWorldResponse(){
    }

    @Override
    public void writePacketData(ByteBuf buffer) {
        NBTTagCompound tag=new NBTTagCompound();
        tag.setString("id",this.id);
        tag.setCompoundTag("info",this.info.getData());
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
        this.id=tag.getString("id");
        this.info.setData(tag.getCompoundTag("info"));
    }

    public LevelInfo getInfo() {
        return info;
    }

    public String getId() {
        return id;
    }
}

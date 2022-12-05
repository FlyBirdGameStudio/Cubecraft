package io.flybird.cubecraft.internal.net.packet.connect;

import io.flybird.cubecraft.auth.Session;
import io.flybird.util.network.packet.PacketConstructor;
import io.flybird.util.network.packet.Packet;
import io.flybird.cubecraft.register.Registry;
import io.flybird.util.container.namespace.TypeItem;
import io.flybird.util.file.nbt.NBTBuilder;
import io.flybird.util.file.nbt.tag.NBTTagCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

@TypeItem("cubecraft:join_request")
public class PacketPlayerJoinRequest implements Packet {
    private Session session=new Session("__DEFAULT__","cubecraft:default");

    public PacketPlayerJoinRequest(Session session) {
        this.session = session;
    }

    @PacketConstructor
    public PacketPlayerJoinRequest() {
    }

    @Override
    public void writePacketData(ByteBuf buffer) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", this.session.getType());
        tag.setCompoundTag("data", Registry.getSessionServiceMap().get(this.session.getType()).write(session));
        try {
            NBTBuilder.write(tag,new ByteBufOutputStream(buffer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        NBTTagCompound tag = null;
        try {
            tag = (NBTTagCompound) NBTBuilder.read(new ByteBufInputStream(buffer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Registry.getSessionServiceMap().get(tag.getString("type")).read(session, tag.getCompoundTag("data"));
    }

    public Session getSession() {
        return session;
    }
}

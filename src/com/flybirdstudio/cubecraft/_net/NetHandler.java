package com.flybirdstudio.cubecraft.client.net;

import com.flybirdstudio.cubecraft.client.net.packet.Packet;
import com.flybirdstudio.cubecraft.client.net.packet.RawPacket;

public class NetHandler{
    public ClientChannelHandler handler;

    public final void sendPacket(Packet p){
        this.handler.sendQueue.add(new RawPacket(p.getType(),p.serialize()));
    }


}

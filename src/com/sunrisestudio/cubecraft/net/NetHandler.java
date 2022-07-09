package com.sunrisestudio.cubecraft.net;

import com.sunrisestudio.cubecraft.net.packet.Packet;
import com.sunrisestudio.cubecraft.net.packet.RawPacket;

public class NetHandler{
    public ClientChannelHandler handler;

    public final void sendPacket(Packet p){
        this.handler.sendQueue.add(new RawPacket(p.getType(),p.serialize()));
    }


}

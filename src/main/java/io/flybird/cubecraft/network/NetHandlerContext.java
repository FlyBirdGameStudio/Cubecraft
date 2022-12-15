package io.flybird.cubecraft.network;

import io.flybird.cubecraft.network.base.NettyChannelHandler;
import io.flybird.cubecraft.network.packet.Packet;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

public record NetHandlerContext(NettyChannelHandler handler, InetSocketAddress from, ChannelHandlerContext nettyContext) {

    public void sendPacket(Packet pkt){
        this.handler.pushSend(pkt);
    }

    public void closeConnection(){
        this.nettyContext.channel().close();
    }

    public void sendDirect(Object msg){
        this.nettyContext.writeAndFlush(msg);
    }
}

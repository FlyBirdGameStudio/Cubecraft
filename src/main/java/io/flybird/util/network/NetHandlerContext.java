package io.flybird.util.network;

import io.flybird.util.network.base.NettyChannelHandler;
import io.flybird.util.network.packet.Packet;
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

package io.flybird.cubecraft.net;

import io.flybird.cubecraft.net.base.NettyChannelHandler;
import io.flybird.cubecraft.net.clientPacket.Packet;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

public record NetHandlerContext(NettyChannelHandler handler, InetSocketAddress from, InetSocketAddress host,
                                ChannelHandlerContext nettyContext) {
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

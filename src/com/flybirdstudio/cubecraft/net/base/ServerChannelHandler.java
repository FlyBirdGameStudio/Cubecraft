package com.flybirdstudio.cubecraft.net.base;

import com.sunrisestudio.cubecraft.net.packet.client.ClientPacket;
import com.sunrisestudio.cubecraft.net.packet.client.PacketClientEmpty;
import com.sunrisestudio.util.container.ArrayQueue;
import com.sunrisestudio.util.container.options.Option;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerChannelHandler extends ChannelInboundHandlerAdapter {
    public ArrayQueue<ClientPacket> packetQueue=new ArrayQueue<>();
    private final Option option=new Option("setting.server.net");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        for (int i=0;i<(Integer)option.get("setting.server.net.maxdatapack");i++) {
            if (this.packetQueue.size() <= 0) {
                ctx.writeAndFlush(this.packetQueue.poll().serialize());
            }else{
                Thread.sleep((Long) option.get("setting.server.net.afktime"));
                ctx.writeAndFlush(new PacketClientEmpty().serialize());
                break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        ctx.close();
    }
}

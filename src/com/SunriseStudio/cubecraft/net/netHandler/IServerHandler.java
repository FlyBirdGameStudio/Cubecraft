package com.SunriseStudio.cubecraft.net.netHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class IServerHandler extends ChannelInboundHandlerAdapter {

    public abstract void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception;

    public abstract void channelReadComplete(ChannelHandlerContext ctx) throws Exception;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        ctx.close();
    }
}

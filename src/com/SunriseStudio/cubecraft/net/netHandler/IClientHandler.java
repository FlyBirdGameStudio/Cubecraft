package com.SunriseStudio.cubecraft.net.netHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class IClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public abstract void channelActive(ChannelHandlerContext ctx) throws Exception;

    @Override
    public abstract void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception;
}

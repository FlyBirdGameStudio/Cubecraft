package com.SunriseStudio.cubecraft.net.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public abstract class IClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public abstract void channelActive(ChannelHandlerContext ctx) throws Exception;

    @Override
    public abstract void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception;
}

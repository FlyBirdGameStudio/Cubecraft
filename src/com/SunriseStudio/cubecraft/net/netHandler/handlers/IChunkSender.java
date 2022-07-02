package com.SunriseStudio.cubecraft.net.netHandler.handlers;

import com.SunriseStudio.cubecraft.net.netHandler.ChannelHandlerServer;
import com.SunriseStudio.cubecraft.net.netHandler.IServerHandler;
import com.SunriseStudio.cubecraft.util.collections.ArrayUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

@ChannelHandlerServer
public class IChunkSender extends IServerHandler {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer=(ByteBuf) msg;
        if(ArrayUtil.startWith("chunk_alloc".getBytes(StandardCharsets.UTF_8),buffer.array())){

        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("fuck!");
    }
}

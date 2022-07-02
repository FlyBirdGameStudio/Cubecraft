package com.SunriseStudio.cubecraft.net.handler.handlers;

import com.SunriseStudio.cubecraft.net.handler.IClientHandler;
import com.SunriseStudio.cubecraft.util.collections.ArrayUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

public class IChunkReceiver extends IClientHandler {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer=(ByteBuf) msg;
        if(ArrayUtil.startWith("chunk_receive".getBytes(StandardCharsets.UTF_8),buffer.array())){
            //read data to chunk,protobuffffffffffffffffffffffff!!!!!!!!!!!!!!!
            ctx.writeAndFlush("chunk_received");
        }
    }
}

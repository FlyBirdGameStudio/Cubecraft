package com.flybirdstudio.cubecraft.client.net.netHandler.handlers;

import com.flybirdstudio.cubecraft.client.net.ClientChannelHandler;
import com.flybirdstudio.util.container.ArrayUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

public class IChunkReceiver extends ClientChannelHandler {
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

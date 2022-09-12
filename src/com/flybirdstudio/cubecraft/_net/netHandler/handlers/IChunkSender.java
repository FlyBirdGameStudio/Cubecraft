package com.flybirdstudio.cubecraft._net.netHandler.handlers;


import com.flybirdstudio.util.container.ArrayUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;


public class IChunkSender extends ServerChannelHandler {
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

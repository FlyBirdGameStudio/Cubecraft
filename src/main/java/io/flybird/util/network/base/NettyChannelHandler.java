package io.flybird.util.network.base;

import io.flybird.util.network.event.ChannelTimingEvent;
import io.flybird.util.network.NetWorkEventBus;
import io.flybird.util.logging.LogHandler;
import io.flybird.util.container.ArrayQueue;
import io.flybird.util.network.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.SocketAddress;

public class NettyChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final LogHandler logHandler = LogHandler.create("NettyChannelHandler");
    final ArrayQueue<Packet> sending = new ArrayQueue<>();
    final Channel ch = null;

    private final NetWorkEventBus eventBus;
    private SocketAddress localAddr;

    public NettyChannelHandler(NetWorkEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) {
        ctx.channel().eventLoop().execute(new PacketReadTask(buf,this,ctx));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.channel().eventLoop().execute(new PacketSendTask(sending,ctx));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.channel().eventLoop().execute(new PacketSendTask(sending,ctx));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent idleEvent) {//超时事件
            if (idleEvent.state() == IdleState.READER_IDLE) {//读
                ctx.channel().close();
            }
        }
        if(evt instanceof ChannelTimingEvent){
            ctx.channel().eventLoop().execute(new PacketSendTask(sending,ctx));
        }
        super.userEventTriggered(ctx, evt);
    }

    public void pushSend(Packet pkt) {
        this.sending.add(pkt);
    }

    public void setLocalAddress(SocketAddress local) {
        this.localAddr = local;
    }

    public void close() {
        this.ch.close();
    }

    public NetWorkEventBus getEventBus() {
        return this.eventBus;
    }
}

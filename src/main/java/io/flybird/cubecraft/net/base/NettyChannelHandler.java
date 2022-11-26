package io.flybird.cubecraft.net.base;

import io.flybird.cubecraft.net.event.ChannelTimingEvent;
import io.flybird.cubecraft.net.NetHandlerContext;
import io.flybird.cubecraft.net.NetWorkEventBus;
import io.flybird.util.logging.LogHandler;
import io.flybird.util.container.ArrayQueue;
import io.flybird.util.container.ArrayUtil;
import io.flybird.cubecraft.net.clientPacket.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class NettyChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final LogHandler logHandler = LogHandler.create("NettyChannelHandler");
    private final ArrayQueue<Packet> sending = new ArrayQueue<>();
    Channel ch = null;

    private final NetWorkEventBus eventBus;
    private SocketAddress localAddr;

    public NettyChannelHandler(NetWorkEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) {
        buf.resetReaderIndex();
        byte headSize = buf.readByte();
        byte[] head=new byte[headSize];
        buf.readBytes(head);

        int length=buf.writerIndex()-buf.readerIndex();
        byte[] data=new byte[length];
        buf.readBytes(data);
        ByteBuf dataReader=ByteBufAllocator.DEFAULT.buffer(length).writeBytes(data);
        String type = new String(head, StandardCharsets.UTF_8);
        try {
            Packet pkt = Packet.createPacket(type);
            pkt.readPacketData(dataReader);
            this.eventBus.callEvent(pkt, new NetHandlerContext(this, (InetSocketAddress) ctx.channel().remoteAddress(), (InetSocketAddress) localAddr,ctx));
        } catch (Exception e) {
            this.logHandler.exception(e);
        }
        dataReader.release();
    }

    //todo:fix netty ByteBuf
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ch = ctx.channel();
        ch.eventLoop().schedule(
                () -> {
                    for (Packet send : this.sending.pollAll(this.sending.size())) {
                        byte[] head = send.getClass().getName().getBytes(StandardCharsets.UTF_8);
                        ByteBuf out=ByteBufAllocator.DEFAULT.ioBuffer();
                        out.writeByte(head.length);
                        out.writeBytes(head);
                        send.writePacketData(out);
                        ctx.writeAndFlush(out);

                        out.release();
                    }
                    ctx.pipeline().fireUserEventTriggered(new ChannelTimingEvent());
                    this.eventBus.callEvent(new ChannelTimingEvent(), null);
                }, 20, TimeUnit.MILLISECONDS);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {//超时事件
            IdleStateEvent idleEvent = (IdleStateEvent) evt;
            if (idleEvent.state() == IdleState.READER_IDLE) {//读
                ctx.channel().close();
            }
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
}

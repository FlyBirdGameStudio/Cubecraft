package io.flybird.cubecraft.net.base;

import io.flybird.cubecraft.net.ChannelTimingEvent;
import io.flybird.cubecraft.net.NetHandlerContext;
import io.flybird.cubecraft.net.NetWorkEventBus;
import io.flybird.cubecraft.register.Registry;
import io.flybird.util.LogHandler;
import io.flybird.util.container.ArrayQueue;
import io.flybird.util.container.ArrayUtil;
import io.flybird.cubecraft.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class NettyChannelHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private final LogHandler logHandler=LogHandler.create("NettyChannelHandler","game");
    private final InetSocketAddress addr;
    private final InetSocketAddress host;
    private final ArrayQueue<Packet> sending = new ArrayQueue<>();
    Channel ch = null;

    private final NetWorkEventBus eventBus;

    public NettyChannelHandler(InetSocketAddress server, InetSocketAddress host, NetWorkEventBus eventBus){
        this.addr=server;
        this.host=host;
        this.eventBus = eventBus;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet){
        ByteBuf buf=packet.content();
        byte[] data = buf.array();
        byte headSize = data[0];
        String type = new String(ArrayUtil.copySub(1, headSize, data), StandardCharsets.UTF_8);
        try {
            Packet pkt = Packet.createPacket(type);
            pkt.readPacketData(buf);
            this.eventBus.callEvent(pkt,new NetHandlerContext(this,packet.sender(),packet.recipient()));
        } catch (Exception e) {
            this.logHandler.error("could not create packet:"+e.getMessage());
        }
        buf.release();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ch = ctx.channel();
        ch.eventLoop().schedule(
                () -> {
                    for (Packet packet : sending.toArray(new Packet[0])) {
                        byte[] head = packet.getClass().getName().getBytes(StandardCharsets.UTF_8);
                        ByteBuf byteBuf= ByteBufAllocator.DEFAULT.ioBuffer();
                        byteBuf.writeByte(head.length);
                        byteBuf.writeBytes(head);
                        packet.writePacketData(byteBuf);
                        ctx.writeAndFlush(new DatagramPacket(byteBuf,addr,host));
                        byteBuf.release();
                    }
                    ctx.pipeline().fireUserEventTriggered(new ChannelTimingEvent());
                    Registry.getNetworkEventBus().callEvent(new ChannelTimingEvent(),null);
                }, 20, TimeUnit.MILLISECONDS);
    }
}

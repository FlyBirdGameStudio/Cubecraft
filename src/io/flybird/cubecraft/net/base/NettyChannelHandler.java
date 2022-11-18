package io.flybird.cubecraft.net.base;

import io.flybird.cubecraft.net.ChannelTimingEvent;
import io.flybird.cubecraft.net.INetHandler;
import io.flybird.cubecraft.register.Registry;
import io.flybird.util.LogHandler;
import io.flybird.util.container.ArrayQueue;
import io.flybird.util.container.ArrayUtil;
import io.flybird.util.container.BufferUtil;
import io.flybird.util.container.namespace.NameSpacedRegisterMap;
import io.flybird.util.event.EventListener;
import io.flybird.util.net.Packet;
import io.flybird.util.net.PacketDecoder;
import io.flybird.util.net.PacketEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class NettyChannelHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private final LogHandler logHandler=LogHandler.create("NettyChannelHandler","game");
    private final InetSocketAddress addr;
    private final InetSocketAddress host;
    private final ArrayQueue<Packet> sending = new ArrayQueue<>();
    Channel ch = null;

    public NettyChannelHandler(InetSocketAddress server,InetSocketAddress host){
        this.addr=server;
        this.host=host;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet){
        byte[] data = packet.content().array();
        byte headSize = data[0];
        String type = new String(ArrayUtil.copySub(1, headSize, data), StandardCharsets.UTF_8);
        try {
            Packet pkt = Packet.createPacket(type);
        } catch (Exception e) {
            this.logHandler.error("could not create packet:"+e.getMessage());
        }
        for (EventListener netHandler: Registry.getNetworkEventBus().getHandlers()){
            ((INetHandler) netHandler).setSendQueue(this.sending);
        }
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
                    }
                    ctx.pipeline().fireUserEventTriggered(new ChannelTimingEvent());
                    Registry.getNetworkEventBus().callEvent(new ChannelTimingEvent(),this.sending);
                }, 20, TimeUnit.MILLISECONDS);
    }
}

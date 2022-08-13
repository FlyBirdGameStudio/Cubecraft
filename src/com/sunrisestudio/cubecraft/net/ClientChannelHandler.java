package com.sunrisestudio.cubecraft.net;

import com.sunrisestudio.cubecraft.net.packet.Packet;
import com.sunrisestudio.cubecraft.net.packet.PacketEventListener;
import com.sunrisestudio.cubecraft.net.packet.PacketHandler;
import com.sunrisestudio.cubecraft.net.packet.RawPacket;
import com.sunrisestudio.util.container.ArrayQueue;
import com.sunrisestudio.util.container.options.Option;
import com.sunrisestudio.util.event.AnnotationPredicateCallBack;
import com.sunrisestudio.util.event.EventBus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.annotation.Annotation;
import java.util.HashMap;

public class ClientChannelHandler extends ChannelInboundHandlerAdapter {
    public ArrayQueue<RawPacket> sendQueue = new ArrayQueue<>();
    public ArrayQueue<RawPacket> receiveQueue = new ArrayQueue<>();
    private final Option option = new Option("setting.net");
    private ChannelHandlerContext ctx;
    private static final HashMap<String, Class<? extends PacketEventListener>> listenerClasses = new HashMap<>();
    private final EventBus<PacketHandler, PacketEventListener, Packet, RawPacket> eventBus = new EventBus<>(
            listenerClasses, new AnnotationPredicateCallBack<>() {
        @Override
        public String getType(PacketHandler packetHandler) {
            return packetHandler.handledType();
        }

        @Override
        public boolean instanceOf(Annotation a) {
            return a instanceof PacketHandler;
        }
    });

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.receiveQueue.add(RawPacket.unSerialize((ByteBuf) msg));
    }

    /**
     * send packet,call event
     */
    public void tick() {
        for (int i = 0; i < (Integer) option.get("setting.net.maxdatapack_send"); i++) {
            if (this.sendQueue.size() > 0) {
                this.ctx.writeAndFlush(RawPacket.serialize(this.sendQueue.poll()));
            }
        }

        for (int i = 0; i < (Integer) option.get("setting.net.maxdatapack_receive"); i++) {
            if (this.receiveQueue.size() > 0) {
                this.eventBus.callEvent(this.receiveQueue.poll());
            }
        }
    }

    /**
     * add listener class
     * @param id id
     * @param clazz class
     */
    public static void registerPacketListener(String id, Class<? extends PacketEventListener> clazz) {
        if (!listenerClasses.containsKey(id)) {
            listenerClasses.put(id, clazz);
        } else {
            throw new RuntimeException("find conflict listener registering!");
        }
    }

    /**
     *
     * @param listener
     */
    public void addListener(PacketEventListener listener){

    }
}

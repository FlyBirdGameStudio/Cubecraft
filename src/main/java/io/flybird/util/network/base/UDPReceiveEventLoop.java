package io.flybird.util.network.base;

import io.flybird.util.network.NetHandlerContext;
import io.flybird.util.network.packet.Packet;
import io.flybird.util.container.ArrayUtil;
import io.flybird.util.container.BufferUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class UDPReceiveEventLoop extends UDPEventLoop {
    //32(manifest)+128(token)+128(uuid)+packet id(2)+total_count(2)+remote addr(31)+type(64)+len(2)+data[1024]
    public static final int LENGTH = 32 + 128 + 128 + 2 + 2 + 31 + 64 + 2 + 1024;

    public UDPReceiveEventLoop(UDPPipeline udpPipeline) {
        super(udpPipeline);
    }

    @Override
    public void handle() {
        byte[] dataArr = new byte[LENGTH];
        DatagramPacket pkt = new DatagramPacket(dataArr, dataArr.length);
        try {
            this.parent.getSocket().receive(pkt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //read header
        ByteBuf data = ByteBufAllocator.DEFAULT.ioBuffer(LENGTH);
        data.writeBytes(dataArr);
        String manifest = BufferUtil.readString(data);
        String token = (String) data.readCharSequence(128, StandardCharsets.UTF_8);
        String uuid = (String) data.readCharSequence(128, StandardCharsets.UTF_8);
        int id = data.readShort();
        int total = data.readShort();
        String addr = (String) data.readCharSequence(15, StandardCharsets.UTF_8);
        int port = data.readUnsignedShort();
        InetSocketAddress remote = new InetSocketAddress(addr, port);

        if(token.startsWith("__CONNECT__")&&!this.parent.getConnectionMap().containsKey(token)){
            this.parent.getConnectionMap().put(token,remote);
        }
        if(token.startsWith("__DISCONNECT__")){
            this.parent.getConnectionMap().remove(token);
        }
        if (!Objects.equals(manifest, this.parent.getConnectionManifest()) || !this.parent.getConnectionMap().containsKey(token)) {
            return;
        }

        //read info

        String type = BufferUtil.readString(data);
        int len = data.readShort();
        byte[] dat = new byte[len];
        data.readBytes(dat);

        //process
        HashMap<String,ArrayList<Byte>> dataMapping= this.parent.getPacketCache();
        HashMap<String,ArrayList<Integer>> idMapping= this.parent.getPacketCacheL();

        if (!idMapping.containsKey(uuid)) {
            idMapping.put(uuid, new ArrayList<>());
        }
        if (!dataMapping.containsKey(uuid)) {
            dataMapping.put(uuid, new ArrayList<>());
        }

        dataMapping.get(uuid).addAll(List.of(ArrayUtil.box(dat)));
        idMapping.get(uuid).add(id);

        if (idMapping.get(uuid).size() == total) {
            byte[] arr = ArrayUtil.unBox(dataMapping.get(uuid).toArray(new Byte[0]));
            dataMapping.remove(uuid);
            idMapping.remove(uuid);
            ByteBuf pktData = ByteBufAllocator.DEFAULT.ioBuffer(arr.length);
            pktData.writeBytes(arr);
            Packet packet = this.parent.getPacketRegistry().create(type);
            packet.readPacketData(pktData);
            this.parent.getPacketEventBus().callEvent(packet, new NetHandlerContext(null, remote, null));
        }

        //release resource
        data.release();
    }
}

package io.flybird.util.net;

import io.flybird.cubecraft.net.Packet;
import io.flybird.util.container.ArrayQueue;
import io.flybird.util.container.ArrayUtil;

import io.flybird.util.container.namespace.NameSpacedRegisterMap;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;


//data format:headLength(1byte)+header(utf-8 string)+data
public class UDPSocket{
    private final NameSpacedRegisterMap<? extends PacketEncoder,?> packetEncoderRegistry;
    private final NameSpacedRegisterMap<? extends PacketDecoder,?> packetDecoderRegistry;

    private final ArrayQueue<Packet> sending=new ArrayQueue<>();
    private final ArrayQueue<Packet> receiving=new ArrayQueue<>();

    private final DatagramSocket socket;
    {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
    private String ip;
    private int port;
    private int maxSending;
    public UDPSocket(
            NameSpacedRegisterMap<? extends PacketEncoder, ?> packetEncoderRegistry,
            NameSpacedRegisterMap<? extends PacketDecoder, ?> packetDecoderRegistry,
            String ip, int port
    ) {
        this.packetEncoderRegistry = packetEncoderRegistry;
        this.packetDecoderRegistry = packetDecoderRegistry;
        this.ip = ip;
        this.port = port;
        setTimeOut(100);
    }

    public void setMaxSending(int maxSending) {
        this.maxSending = maxSending;
    }

    public void setConnect(String ip, int port){
        this.ip=ip;
        this.port=port;
        socket.disconnect();
        try {
            socket.connect(InetAddress.getByName(ip),port);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTimeOut(int ms){
        try {
            this.socket.setSoTimeout(ms);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(Packet packet){
        this.sending.add(packet);
    }

    public void tick(){
        try {
            int size=this.socket.getReceiveBufferSize();
            byte[] data=new byte[size];
            DatagramPacket receive=new DatagramPacket(data,size,InetAddress.getByName(this.ip),port);
            socket.receive(receive);
            byte headSize=data[0];
            String type=new String(ArrayUtil.copySub(1,headSize,data),StandardCharsets.UTF_8);
            this.receiving.add(this.packetDecoderRegistry.get(type).decode(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Packet> packets= sending.pollAll(this.maxSending);
        for (Packet packet:packets){
            sendDirect(packet);
        }
    }

    //yeah, use delegate
    public void sendDirect(Packet packet){
        byte[] serialized=this.packetEncoderRegistry.get(packet.getType()).encode(packet);
        byte[] head=packet.getType().getBytes(StandardCharsets.UTF_8);
        byte[] finalData=ArrayUtil.connect(new byte[]{(byte) head.length},head,serialized);
        try {
            socket.send(new DatagramPacket(finalData, finalData.length,InetAddress.getByName(this.ip),this.port));
        } catch (IOException e) {
            System.out.println(":(");
        }
    }
}

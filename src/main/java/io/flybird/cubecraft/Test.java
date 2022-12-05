package io.flybird.cubecraft;


import io.flybird.cubecraft.internal.net.packet.connect.PacketPlayerJoinResponse;
import io.flybird.cubecraft.internal.net.packet.playing.PacketChunkData;
import io.flybird.util.network.NetHandlerContext;
import io.flybird.util.network.packet.PacketEventHandler;
import io.flybird.util.network.handler.INetHandler;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.chunk.ChunkPos;
import io.flybird.util.container.namespace.TypeItem;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.StandardCharsets;

public class Test {
    static int ppsServer, ppsClient;

    public static void main(String[] args) throws Exception {
        ByteBuf out = ByteBufAllocator.DEFAULT.ioBuffer(512, 4194304);
        PacketChunkData dat=new PacketChunkData(new Chunk(new IWorld("test",null),new ChunkPos(11,45,14)));
        String type = dat.getClass().getAnnotation(TypeItem.class).value();

        byte[] head = type.getBytes(StandardCharsets.UTF_8);
        out.writeByte(head.length);
        out.writeBytes(head);
        dat.writePacketData(out);

        out.resetReaderIndex();
        byte headSize = out.readByte();
        byte[] h=new byte[headSize];
        out.readBytes(h);
        System.out.println(new String(h, StandardCharsets.UTF_8));

        PacketChunkData data=new PacketChunkData();
        data.readPacketData(out);
        Chunk c=data.getChunk();



        /*
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 11451);

        ClientNettyPipeline client = new ClientNettyPipeline();
        ClientNettyPipeline client2 = new ClientNettyPipeline();
        ServerNettyPipeline server = new ServerNettyPipeline();

        Registry.getPackets().registerItem(PacketPlayerJoinResponse.class);

        client.registerNetHandler(new ClientHandlerTest());
        client2.registerNetHandler(new ClientHandlerTest());
        server.registerNetHandler(new ServerHandlerTest());

        client.setServerAddr(address);
        client.init(4);
        client2.setServerAddr(address);
        client2.init(4);
        server.setPort(address.getPort());
        server.init(4);

        client.getHandler().pushSend(new PacketPlayerJoinResponse("client_start"));

        while (true) {
            Thread.sleep(1000);
            System.out.println(ppsClient + "/" + ppsServer);
            ppsServer = 0;
            ppsClient=0;
        }

         */
    }

    public static class ClientHandlerTest implements INetHandler {
        @PacketEventHandler
        public void onPacket(PacketPlayerJoinResponse pkt, NetHandlerContext ctx) {
            ctx.sendPacket(new PacketPlayerJoinResponse("client"));
            ppsClient += 1;
        }
    }

    public static class ServerHandlerTest implements INetHandler {
        @PacketEventHandler
        public void onPacket(PacketPlayerJoinResponse pkt, NetHandlerContext ctx) {
            ctx.sendPacket(new PacketPlayerJoinResponse("server"));
            ppsServer += 1;
        }
    }
}

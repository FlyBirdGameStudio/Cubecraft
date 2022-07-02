package com.SunriseStudio.cubecraft.net.NetUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NetMain extends Thread{
    private String IP,Port;
    private ChannelFuture ClientChannel;

    public NetMain(String... ServerIP){
        IP = ServerIP[0];Port = ServerIP[1];
    }

    @Override
    public void run() {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new NetEvents());
                        }
                    });
            ClientChannel = bootstrap.connect(IP,Integer.parseInt(Port)).sync();
            ClientChannel.channel().closeFuture().sync();
        } catch (Exception e) {

        } finally {
            nioEventLoopGroup.shutdownGracefully();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();

    }
}

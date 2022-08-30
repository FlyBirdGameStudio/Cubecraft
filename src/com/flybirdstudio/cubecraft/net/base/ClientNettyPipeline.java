package com.flybirdstudio.cubecraft.net.base;

import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.registery.Registery;
import com.flybirdstudio.util.LogHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientNettyPipeline {
    private NioEventLoopGroup eventExecutors;
    private final LogHandler logHandler=LogHandler.create("clientIO","client");

    /**
     * init netty channel,set netHandler
     * @param initialHost initial host ip
     * @param port initial port
     */
    public void init(String initialHost, int port){
        new Thread(() -> {
            this.logHandler.info("starting client io at "+initialHost+":"+port);
            this.eventExecutors = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(eventExecutors)
                        //设置客户端的通道实现类型
                        .channel(NioSocketChannel.class)
                        //使用匿名内部类初始化通道
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch){
                                //添加客户端通道的处理器
                                ch.pipeline().addLast("cubecraft:default",new NettyChannelHandler(
                                        Registery.getPacketEncoderMap(),
                                        Registery.getPacketDecoderMap(),
                                        GameSetting.instance.getValueAsInt("client.net.maxsending",16),
                                        GameSetting.instance.getValueAsInt("client.net.speed",16)
                                ));
                            }
                        });
                ChannelFuture channelFuture = bootstrap.connect(initialHost, port).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                this.logHandler.error("channel listen interrupt:"+e);
            } finally {
                this.shutdown();
            }
        },"channel_initializer_client").start();
    }

    /**
     * stop netty channel
     */
    public void shutdown(){
        this.logHandler.info("shutting down client io...");
        this.eventExecutors.shutdownGracefully();
        this.logHandler.info("client io stopped");
    }
}

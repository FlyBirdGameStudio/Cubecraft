package com.flybirdstudio.cubecraft.net.base;

import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.registery.Registery;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.*;

public class ServerNettyPipeline {
    public void initNettyPipeline(){
        new Thread(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) {
                                socketChannel.pipeline().addLast(new NettyChannelHandler(
                                        Registery.getPacketEncoderMap(),
                                        Registery.getPacketDecoderMap(),
                                        GameSetting.instance.getValueAsInt("server.net.maxsending",16),
                                        GameSetting.instance.getValueAsInt("server.net.speed",16)
                                ));
                            }
                        });
                ChannelFuture channelFuture = bootstrap.bind(GameSetting.instance.getValueAsInt("port",25585)).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        },"server_initializer").start();
    }
}

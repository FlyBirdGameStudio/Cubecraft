package com.sunrisestudio.cubecraft.net;

import com.sunrisestudio.util.container.options.Option;
import com.sunrisestudio.util.container.options.Options;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.*;

import java.util.ArrayList;

public class ServerIO {
    private final Option option;

    public ServerIO(Option option){
         this.option=option;
    }



    private final ArrayList<ServerChannelHandler> handlers=new ArrayList<>();


    public void initNettyPipeline() throws Exception {
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
                            socketChannel.pipeline().addLast(ServerIO.this.handlers.toArray(new ChannelHandler[0]));
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind((Integer) option.get("port")).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new Exception("can not init netty pipeline:"+e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    static {
        Options.setDefault("server.port",25575);
    }
}

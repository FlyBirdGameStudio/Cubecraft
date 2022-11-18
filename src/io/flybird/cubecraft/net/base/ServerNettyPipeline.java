package io.flybird.cubecraft.net.base;

import io.flybird.cubecraft.register.Registry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.*;

//todo:fit addr host
public class ServerNettyPipeline {
    public void initNettyPipeline(int port){
        new Thread(() -> {
            EventLoopGroup bossGroup=new NioEventLoopGroup();
            try
            {
                //通过NioDatagramChannel创建Channel，并设置Socket参数支持广播
                //UDP相对于TCP不需要在客户端和服务端建立实际的连接，因此不需要为连接（ChannelPipeline）设置handler
                Bootstrap b=new Bootstrap();
                b.group(bossGroup)
                        .channel(NioDatagramChannel.class)
                        .option(ChannelOption.SO_BROADCAST, true)
                        .handler(new NettyChannelHandler(null,null));
                b.bind(port).sync().channel().closeFuture().await();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally{
                bossGroup.shutdownGracefully();
            }

        },"server_initializer").start();
    }
}

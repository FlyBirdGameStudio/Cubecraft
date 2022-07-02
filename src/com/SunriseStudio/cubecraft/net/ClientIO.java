package com.SunriseStudio.cubecraft.net;

import com.SunriseStudio.cubecraft.net.handler.ChannelHandlerClient;
import com.SunriseStudio.cubecraft.net.handler.IClientHandler;
import com.SunriseStudio.cubecraft.util.ReflectHelper;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ClientIO {
    private final ArrayList<IClientHandler> handlers=new ArrayList<>();

    public void registerChannelHandlerClient(){
        try{
            this.handlers.clear();
            //get class with annotation
            List<Class<?>> result=new ArrayList<>();
            Iterator<Class<?>> it= ReflectHelper.getAllClass();
            while (it.hasNext()){
                Class<?> clazz=it.next();
                if(Arrays.stream(clazz.getAnnotations()).anyMatch(annotation -> annotation instanceof ChannelHandlerClient)){
                    this.handlers.add((IClientHandler) clazz.getConstructor().newInstance());
                }
            }
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            //fuck!
        }
    }

    public void initNettyPipeline(){
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            //创建bootstrap对象，配置参数
            Bootstrap bootstrap = new Bootstrap();
            //设置线程组
            bootstrap.group(eventExecutors)
                    //设置客户端的通道实现类型
                    .channel(NioSocketChannel.class)
                    //使用匿名内部类初始化通道
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //添加客户端通道的处理器
                            ch.pipeline().addLast(ClientIO.this.handlers.toArray(new ChannelHandler[0]));
                        }
                    });
            System.out.println("客户端准备就绪，随时可以起飞~");
            //连接服务端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6666).sync();
            //对通道关闭进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //关闭线程组
            eventExecutors.shutdownGracefully();
        }
    }
}

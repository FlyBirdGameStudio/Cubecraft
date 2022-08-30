package com.flybirdstudio.cubecraft;

import com.flybirdstudio.util.math.MathHelper;
import com.flybirdstudio.util.net.UDPServerThread;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Test {
    public static void startClient() throws IOException {
        byte[] sMsgbyte = "用户名：sysker;密码：128398908".getBytes();
        InetAddress serverIp = InetAddress.getByName("localhost");
       // DatagramPacket packet = new DatagramPacket(sMsgbyte, sMsgbyte.length, serverIp, 8800);
        DatagramSocket socket = null;
        Scanner sc=new Scanner(System.in);
        while (true) {
            System.out.println("请输入：");
            String msg = sc.nextLine();

            if ("exit".equals(msg)){
                System.out.println("退出成功");
                socket.close();
                break;
            }

            //2.创建一个数据包对象封装数据
            byte[] buffer = msg.getBytes();
            DatagramPacket packet = new DatagramPacket( buffer,buffer.length, InetAddress.getLocalHost(),3333);

            //3.发送数据出去
            socket.send(packet);
        }

        byte[] getMsg = new byte[1024];
        DatagramPacket getPacket = new DatagramPacket(getMsg, getMsg.length);
        socket.receive(getPacket);
        System.out.println("我是客户端，服务器说："+new String(getMsg,0,getPacket.getLength()));
        socket.close();
    }

    public static void startServer() throws Exception {
        System.out.println("-----启动服务端------");
        //1.创建接收端对象：注册端口
        DatagramSocket socket = new DatagramSocket(65535);

        //2.创建一个数据包对象接收数据
        byte[] buf = new byte[1024 * 64];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        while (true) {
            //3.等待接收数据
            socket.receive(packet);

            //4.取出数据
            int len = packet.getLength();
            String rs = new String(buf, 0, len);
            System.out.println("收到了ip为：" + packet.getAddress() + " 端口号为：" + packet.getPort() + "的消息：" + rs);
        }
    }

    public static void main2(String[] args) throws Exception {
        int clientCount = 0;

        DatagramSocket socket = new DatagramSocket(8800);

        System.out.println("***服务器正在启动，等待客户端连接***");

        byte[] msg = new byte[1024];
        DatagramPacket getPacket =null;
        while(clientCount<10) {
            try {
                getPacket = new DatagramPacket(msg, msg.length);
                socket.receive(getPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            UDPServerThread udpServerThread = new UDPServerThread(socket, getPacket, msg);
            udpServerThread.start();
            clientCount++;
            System.out.println("客户端数量" + clientCount);
        }
    }

    public static void main(String args[]) throws Exception {
        new Thread(() -> {
            try {
                main2(args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                try {
                    startClient();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}

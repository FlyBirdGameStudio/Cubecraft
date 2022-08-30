package com.flybirdstudio.util.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServerThread extends Thread {
    DatagramSocket socket = null;
    DatagramPacket getPacket = null;
    byte []msg = null;
    public UDPServerThread(DatagramSocket socket, DatagramPacket getPacket, byte []msg){
        this.socket = socket;
        this.getPacket = getPacket;
        this.msg = msg;
    }

    public void run(){
        System.out.println("我是服务器，客户端说："+new String(msg,0,getPacket.getLength()));
        System.out.println("客户端的IP地址："+getPacket.getAddress());
        byte[] sendMsg = "欢迎您！".getBytes();

        DatagramPacket sendPacket = new DatagramPacket(
                sendMsg, sendMsg.length, getPacket.getAddress(), getPacket.getPort());
        try {
            socket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}   
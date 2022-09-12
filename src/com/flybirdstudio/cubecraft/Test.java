package com.flybirdstudio.cubecraft;

import com.flybirdstudio.util.ColorUtil;
import com.flybirdstudio.util.math.MathHelper;
import com.flybirdstudio.util.net.UDPServerThread;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Test {
    public static void main(String args[]) throws Exception {
        for (float x=0;x<=1.1;x+=0.1){
            System.out.println(x+"/"+ColorUtil.float3toInt1(x,x,x));
        }
    }
}

package com.SunriseStudio.cubecraft.util.math;

public class MathHelper {
    public static double linear_interpolate(double x,double y,double t){
        return x + (y - x) * t;
    }

    public static double smooth_interpolate(double x,double y,double t){
        double a=clamp((t-x)/(y-x),0,1);
        return t*t*(3-2*t);
    }

    public static double clamp(double x,double max,double min){
        if(x>max){
            x=max;
        }
        if(x<min){
            x=min;
        }
        return x;
    }

    public static double step(double x,double step){
        if(x<step){
            return 0.0;
        }else{
            return 1.0;
        }
    }
    public static long rand3(long n, long n2, long n3) {
        long l = (n * 3129871) ^ n3 * 116129781L ^ n2;
        l = l * l * 42317861L + l * 11L;
        return l >> 16;
    }

    public static long rand2(long n,long n2){
        long n3=1145141919810L;
        long l = (n * 3129871) ^ n3 * 116129781L ^ n2;
        l = l * l * 42317861L + l * 11L;
        return l >> 16;
    }
}

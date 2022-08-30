package com.flybirdstudio.util.math;

import org.joml.Vector3d;

public class MathHelper {
    public static double linear_interpolate(double x,double y,double t){
        return x + (y - x) * t;
    }

    public static Vector3d linear_interpolate(Vector3d x,Vector3d y,double t){
        return new Vector3d(
                linear_interpolate(x.x,y.x,t),
                linear_interpolate(x.y,y.y,t),
                linear_interpolate(x.z,y.z,t)
        );
    }


    public static double smooth_interpolate(double x,double y,double t){
        return smoothstep(x,y,t);
    }

    static double smoothstep(double t1, double t2, double x) {
        // Scale, bias and saturate x to 0..1 range
        // 还记得么？在remap算法中接触过
        x = clamp((x - t1) / (t2 - t1), 0.0, 1.0);
        // Evaluate polynomial
        return x * x * (3 - 2 * x);
    }

    public static double clamp(double x, double max, double min){
        if(x>max){
            x=max;
        }
        if(x<min){
            x=min;
        }
        return x;
    }

    public static double step(double x, double step){
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
        long homooooooooooooooo=1145141919810L;
        long l = (n * 3129871) ^ homooooooooooooooo * 116129781L ^ n2;
        l = l * l * 42317861L + l * 11L;
        return l >> 16;
    }

    public static double scale(double x, double outputMin, double outputMax, double inputMin, double inputMax){
        return (x- inputMin)/(inputMax - inputMin)*(outputMax - outputMin)+ outputMin;
    }

    public static long getChunkPos(long world,long aspect){
        int fix=world<0?1:0;
        return world/aspect-fix;
    }

    public static long getRelativePosInChunk(long world,long aspect){
        long a=getChunkPos(world, aspect)*aspect;
        return world-a-(world<0?1:0);
    }

    public static long floor(double d) {
        final long n = (long)d;
        if (d < n) {
            return n - 1;
        }
        return n;
    }

    public static Vector3d getIntermediateWithXValue(final Vector3d self,final Vector3d vec3D, double float2) {
        final double n = vec3D.x - self.x;
        final double n2 = vec3D.y - self.y;
        final double n3 = vec3D.z - self.z;
        if (n * n < 1.0E-7f) {
            return null;
        }
        if ((float2 = (float2 - self.x) / n) < 0.0f || float2 > 1.0f) {
            return null;
        }
        return new Vector3d(self.x + n * float2, self.y + n2 * float2, self.z + n3 * float2);
    }

    public static Vector3d getIntermediateWithYValue(final Vector3d self,final Vector3d vec3D, double float2) {
        final double n = vec3D.x - self.x;
        final double n2 = vec3D.y - self.y;
        final double n3 = vec3D.z - self.z;
        if (n2 * n2 < 1.0E-7f) {
            return null;
        }
        if ((float2 = (float2 - self.y) / n2) < 0.0f || float2 > 1.0f) {
            return null;
        }
        return new Vector3d(self.x + n * float2, self.y + n2 * float2, self.z + n3 * float2);
    }

    public static Vector3d getIntermediateWithZValue(final Vector3d self,final Vector3d vec3D, double float2) {
        final double n = vec3D.x - self.x;
        final double n2 = vec3D.y - self.y;
        final double n3;
        if ((n3 = vec3D.z - self.z) * n3 < 1.0E-7f) {
            return null;
        }
        if ((float2 = (float2 - self.z) / n3) < 0.0f || float2 > 1.0f) {
            return null;
        }
        return new Vector3d(self.x + n * float2, self.y + n2 * float2, self.z + n3 * float2);
    }

    public static int floor_double(double d) {
        final int n = (int)d;
        if (d < n) {
            return n - 1;
        }
        return n;
    }

    /**
     * get "t" from a,b,x
     */
    public static double reverse_interpolate(double a,double b,double x){
        return (x-a)/(b-a);
    }

    public static double reverse_interpolate_abs(double a,double b,double x) {
        return (x-a)/Math.abs(b-a);
    }


    public static Vector3d getVectorForRotation(float pitch, float yaw) {
        float f = (float) Math.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = (float) Math.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = (float) -Math.cos(-pitch * 0.017453292F);
        float f3 = (float) Math.sin(-pitch * 0.017453292F);
        return new Vector3d((f1 * f2), f3, (f * f2));
    }

    public static long toExactWorldPos(double pos) {
        return (long) (pos-(pos>=0?0:1));
    }

    public static double dist(Vector3d v0,Vector3d v1){
        return Math.sqrt(pow2(v0.x-v1.x)+pow2(v0.y-v1.y)+pow2(v0.z-v1.z));
    }

    public static double pow2(double a){
        return a*a;
    }

    public static double linear_interpolate2d(double _00,double _01,double _10,double _11,double xt,double yt){
        double _0z=linear_interpolate(_00,_01,xt);
        double _1z=linear_interpolate(_10,_11,xt);
        return linear_interpolate(_0z,_1z,yt);
    }
}

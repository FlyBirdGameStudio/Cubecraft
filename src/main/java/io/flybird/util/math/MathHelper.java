package io.flybird.util.math;

import org.joml.Vector3d;

import java.util.Arrays;

public class MathHelper {

    /**
     * linear_interpolate
     *
     * @param x a
     * @param y b
     * @param t t
     * @return interpolated value
     */
    public static double linearInterpolate(double x, double y, double t) {
        return x + (y - x) * t;
    }

    /**
     * 3d linear_interpolate
     *
     * @param x a
     * @param y b
     * @param t t
     * @return interpolated value
     */
    public static Vector3d linearInterpolate(Vector3d x, Vector3d y, double t) {
        return new Vector3d(
                linearInterpolate(x.x, y.x, t),
                linearInterpolate(x.y, y.y, t),
                linearInterpolate(x.z, y.z, t)
        );
    }

    /**
     * clamp value to range
     *
     * @param x   value
     * @param max max range
     * @param min min range
     * @return clamped value
     */
    public static double clamp(double x, double max, double min) {
        if (x > max) {
            x = max;
        }
        if (x < min) {
            x = min;
        }
        return x;
    }

    /**
     * get random from 3d vec
     *
     * @param n  x
     * @param n2 y
     * @param n3 z
     * @return rand
     * @author Minecraft
     */
    public static long rand3(long n, long n2, long n3) {
        long l = (n * 3129871) ^ n3 * 116129781L ^ n2;
        l = l * l * 42317861L + l * 11L;
        return l >> 16;
    }

    /**
     * get random from 2d vec
     *
     * @param n  x
     * @param n2 y
     * @return rand
     * @author Minecraft
     */
    public static long rand2(long n, long n2) {
        long what = 1145141919810L;
        long l = (n * 3129871) ^ what * 116129781L ^ n2;
        l = l * l * 42317861L + l * 11L;
        return l >> 16;
    }

    /**
     * scale number from a range to another range
     * @param x value
     * @param outputMin min range for out
     * @param outputMax max range for out
     * @param inputMin min range for in
     * @param inputMax max range for in
     * @return scaled value
     */
    public static double scale(double x, double outputMin, double outputMax, double inputMin, double inputMax) {
        return (x - inputMin) / (inputMax - inputMin) * (outputMax - outputMin) + outputMin;
    }

    /**
     * get a chunk position in world
     * @param world world coordinate
     * @param aspect aspect(chunk size)
     * @return chunk position
     */
    public static long getChunkPos(long world, long aspect) {
        int fix = world < 0 ? 1 : 0;
        return world / aspect - fix;
    }

    /**
     * get relative pos in chunk
     * @param world world coordinate
     * @param aspect aspect(chunk size)
     * @return relative pos
     */
    public static long getRelativePosInChunk(long world, long aspect) {
        long a = getChunkPos(world, aspect) * aspect;
        return world - a - (world < 0 ? 1 : 0);
    }


    /**
     * get "t" from a,b,x
     */
    public static double reverse_interpolate(double a, double b, double x) {
        return (x - a) / (b - a);
    }

    /**
     * get "t" from a,b,x
     */
    public static double reverse_interpolate_abs(double a, double b, double x) {
        return (x - a) / Math.abs(b - a);
    }

    /**
     * get vector for rotation
     *
     * @author Minecraft
     */
    public static Vector3d getVectorForRotation(float pitch, float yaw) {
        float f = (float) Math.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = (float) Math.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = (float) -Math.cos(-pitch * 0.017453292F);
        float f3 = (float) Math.sin(-pitch * 0.017453292F);
        return new Vector3d((f1 * f2), f3, (f * f2));
    }

    /**
     * distance from a-b
     * @param v0 a
     * @param v1 b
     * @return dist
     */
    public static double dist(Vector3d v0, Vector3d v1) {
        return Math.sqrt(pow2(v0.x - v1.x) + pow2(v0.y - v1.y) + pow2(v0.z - v1.z));
    }

    /**
     * power 2
     * @param a origin number
     * @return powered data
     */
    public static double pow2(double a) {
        return a * a;
    }

    /**
     * interpolate in 2d
     * @param _00 00
     * @param _01 01
     * @param _10 10
     * @param _11 11
     * @param xt xt
     * @param yt yt
     * @return interpolated data
     */
    public static double linear_interpolate2d(double _00, double _01, double _10, double _11, double xt, double yt) {
        double _0z = linearInterpolate(_00, _10, xt);
        double _1z = linearInterpolate(_01, _11, xt);
        return linearInterpolate(_0z, _1z, yt);
    }

    /**
     * reflect value against y
     * @param y reflect axis
     * @param v value
     * @return reflected value
     */
    public static double reflect(double y, double v) {
        return v - (y - v);
    }

    /**
     * hex char to int
     * @param ch char
     * @return value
     */
    public static int getHexValue(char ch) {
        if (ch >= '0' && ch <= '9') {
            return Integer.parseInt(String.valueOf(ch));
        }
        if ((ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F')) {
            switch (ch) {
                case 'a':
                case 'A':
                    //这里不用break是因为执行了return以后就不会再往下执行了
                    return 10;
                case 'b':
                case 'B':
                    return 11;
                case 'c':
                case 'C':
                    return 12;
                case 'd':
                case 'D':
                    return 13;
                case 'e':
                case 'E':
                    return 14;
                case 'f':
                case 'F':
                    return 15;
            }
        }
        return -1;
    }

    /**
     * hex string to int
     * @param str string
     * @return value
     */
    public static int hex2Int(String str) {
        int result = 0;
        char[] hex = str.toCharArray();
        for (int i = 0; i < hex.length; i++) {
            if (getHexValue(hex[i]) != -1) {
                result += getHexValue(hex[i]) * Math.pow(16, hex.length - i - 1);
            } else {
                return -1;
            }
        }
        return result;
    }

    /**
     * find min one in 3 value
     * @param d0 value
     * @param d1 value
     * @param d2 value
     * @return min one
     */
    public static double min3(double d0, double d1, double d2) {
        return Math.min(d0, Math.min(d1, d2));
    }

    /**
     * find max one in 3 value
     * @param d0 value
     * @param d1 value
     * @param d2 value
     * @return max one
     */
    public static double max3(double d0, double d1, double d2) {
        return Math.max(d0, Math.max(d1, d2));
    }

    /**
     * 3d linear interpolation
     * @param _000 vertex value
     * @param _001 vertex value
     * @param _010 vertex value
     * @param _011 vertex value
     * @param _100 vertex value
     * @param _101 vertex value
     * @param _110 vertex value
     * @param _111 vertex value
     * @param xt x pos
     * @param yt y pos
     * @param zt z pos
     *
     * @return interpolated value
     */
    public static double linear_interpolate3d(double _000, double _001, double _010, double _011,
                                              double _100, double _101, double _110, double _111,
                                              double xt, double yt, double zt
    ) {
        double[][][] c = new double[2][2][2];
        c[0][0][0] = _000;
        c[0][0][1] = _001;
        c[0][1][0] = _010;
        c[0][1][1] = _011;
        c[1][0][0] = _100;
        c[1][0][1] = _101;
        c[1][1][0] = _110;
        c[1][1][1] = _111;
        double accum = 0.0;
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                for (int k = 0; k < 2; k++)
                    accum += (i * xt + (1 - i) * (1 - xt)) *
                            (j * yt + (1 - j) * (1 - yt)) *
                            (k * zt + (1 - k) * (1 - zt)) *
                            c[i][j][k];
        return accum;
    }

    /**
     * find avg data
     * @param data data
     * @return avg
     */
    public static double avg(double... data) {
        double result = 0;
        for (double d : data) {
            result += d;
        }
        return result / data.length;
    }

    /**
     * alt method
     * @param v vertex data
     * @param v1 vertex data
     * @param v2 vertex data
     * @param v3 vertex data
     * @param v4 vertex data
     * @param v5 vertex data
     * @param v6 vertex data
     * @param v7 vertex data
     * @param t pos
     * @return interpolated data
     */
    public static double linear_interpolate3d(double v, double v1, double v2, double v3, double v4, double v5, double v6, double v7, Vector3d t) {
        return linear_interpolate3d(v, v1, v2, v3, v4, v5, v6, v7, t.x, t.y, t.z);
    }

    /**
     * find median from data
     * @param data data
     * @return median number
     */
    public static double median(double... data) {
        Arrays.sort(data);
        if (data.length % 2 == 0) {
            return MathHelper.avg(data[data.length / 2], data[data.length / 2 + 1]);
        } else {
            return data[(data.length) / 2];
        }
    }

    /**
     * find max from data
     * @param data dara
     * @return max number
     */
    public static double max(double... data) {
        Arrays.sort(data);
        return data[data.length - 1];
    }
}

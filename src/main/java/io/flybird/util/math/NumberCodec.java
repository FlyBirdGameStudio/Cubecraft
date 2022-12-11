package io.flybird.util.math;

import java.nio.ByteBuffer;

/**
 * code number to bytes,or decode number from bytes.
 *
 * @author GrassBlock2022
 */
public class NumberCodec {
    public static ByteBuffer buffer = ByteBuffer.allocate(8);

    public static byte[] split(short n) {
        return buffer.clear().putShort(n).array();
    }

    public static byte[] split(int n) {
        return buffer.clear().putInt(n).array();
    }

    public static byte[] split(long n) {
        return buffer.clear().putLong(n).array();
    }

    public static byte[] split(float n){
        return buffer.clear().putFloat(n).array();
    }

    public static byte[] split(double n){
        return buffer.clear().putDouble(n).array();
    }


    public static short asShort(byte[] arr){
        return buffer.clear().put(arr,0,2).flip().getShort();
    }

    public static int asInt(byte[] arr){
        return buffer.clear().put(arr,0,4).flip().getInt();
    }

    public static long asLong(byte[] arr){
        return buffer.clear().put(arr,0,8).flip().getInt();
    }

    public static float asFloat(byte[] arr){
        return buffer.clear().put(arr,0,4).flip().getFloat();
    }

    public static double asDouble(byte[] arr){
        return buffer.clear().put(arr,0,8).flip().getDouble();
    }
}

package io.flybird.util.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * simple util for array.
 *
 * @author GrassBlock2022
 */
public class ArrayUtil {

    /**
     * calc dispatched array position in 3d
     *
     * @param w width
     * @param h height
     * @param d depth
     * @param x x
     * @param y y
     * @param z z
     * @return position
     */
    public static int calcDispatchPos3d(int w, int h, int d, int x, int y, int z) {
        return (y * d + z) * w + x;
    }

    /**
     * box basic data type to object data type
     *
     * @param data raw array
     * @return boxed array
     */
    public static Byte[] box(byte[] data) {
        Byte[] data2 = new Byte[data.length];
        for (int i = 0; i < data.length; i++) {
            data2[i] = data[i];
        }
        return data2;
    }

    /**
     * unbox basic object data type to basic data type
     *
     * @param data boxed array
     * @return raw array
     */
    public static byte[] unBox(Byte[] data) {
        byte[] data2 = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            data2[i] = data[i];
        }
        return data2;
    }

    /**
     * covert data type to another data type.
     *
     * @param array array
     * @return another typed array
     */
    public static int[] short2int(short[] array) {
        int[] d = new int[array.length];
        int c = 0;
        for (int i : array) {
            d[c] = i;
            c++;
        }
        return d;
    }

    /**
     * covert data type to another data type.
     *
     * @param array array
     * @return another typed array
     */
    public static short[] int2short(int[] array) {
        short[] d = new short[array.length];
        int c = 0;
        for (int i : array) {
            d[c] = (short) i;
            c++;
        }
        return d;
    }

    /**
     * allocate data array from varargs.
     *
     * @param args args
     * @param <T>  Template class for atrray.
     * @return data
     */
    @SafeVarargs
    public static <T> T[] allocate(T... args) {
        return args;
    }

    /**
     * connect arrays to an big array.
     *
     * @param a arrays
     * @return connected array.
     */
    public static byte[] connect(byte[]... a) {
        ArrayList<Byte> data = new ArrayList<>();
        for (byte[] arr : a) {
            for (byte b : arr) {
                data.add(b);
            }
        }
        return unBox(data.toArray(new Byte[0]));
    }


    public static <T> boolean startWith(T[] t, T[] arr) {
        boolean b = true;
        for (int i = 0; i < t.length; i++) {
            if (t[i] != arr[i]) {
                b = false;
                break;
            }
        }
        return b;
    }

    public static boolean startWith(byte[] t, byte[] arr) {
        boolean b = true;
        for (int i = 0; i < t.length; i++) {
            if (t[i] != arr[i]) {
                b = false;
                break;
            }
        }
        return b;
    }

    public static <T> boolean endWith(T[] t, T[] arr) {
        boolean b = true;
        for (int i = arr.length - t.length; i < arr.length; i++) {
            if (t[i] != arr[arr.length - t.length + i]) {
                b = false;
                break;
            }
        }
        return b;
    }


    public static float[] copySub(int start, int end, float[] arr) {
        float[] result = new float[end - start];
        if (end - start >= 0) System.arraycopy(arr, start, result, 0, end - start);
        return result;
    }

    public static double[] copySub(int start, int end, double[] arr) {
        double[] result = new double[end - start];
        for (int i = start; i < end; i++) {
            result[i - start] = arr[i];
        }
        return result;
    }

    public static byte[] copySub(int start, byte end, byte[] arr) {
        byte[] result = new byte[end - start];
        for (int i = start; i < end; i++) {
            result[i - start] = arr[i];
        }
        return result;
    }


    public static <T> T getDispatched(int x, int y, int z, int size, T[] arr) {
        return arr[y * size * size + z * size + x];
    }

    public static <T> void setDispatched(int x, int y, int z, int size, T[] arr, T target) {
        arr[y * size * size + z * size + x] = target;
    }

    public static byte getDispatched(int x, int y, int z, int size, byte[] arr) {
        return arr[y * size * size + z * size + x];
    }

    public static void setDispatched(int x, int y, int z, int size, byte[] arr, byte target) {
        arr[y * size * size + z * size + x] = target;
    }

    public static double getDispatched(int x, int y, int z, int size, double[] arr) {
        return arr[y * size * size + z * size + x];
    }

    public static void setDispatched(int x, int y, int z, int size, double[] arr, double target) {
        arr[y * size * size + z * size + x] = target;
    }

    public static int getDispatched(int x, int y, int z, int size, int[] arr) {
        return arr[y * size * size + z * size + x];
    }

    public static void setDispatched(int x, int y, int z, int size, int[] arr, byte target) {
        arr[y * size * size + z * size + x] = target;
    }

}

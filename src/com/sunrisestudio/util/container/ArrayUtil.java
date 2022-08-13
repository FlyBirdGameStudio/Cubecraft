package com.sunrisestudio.util.container;

import java.util.Arrays;
import java.util.Objects;

public class ArrayUtil {
    public static <T>void iterateArray(T[] array, ArrayIterationAction<T> action){
        for(T item:array){
            action.action(item);
        }
    }

    public static <T>boolean arrayMatchAny(T t, T[] arr){
        return Arrays.asList(arr).contains(t);
    }

    public static <T>boolean arrayMatchNone(T t, T[] arr){
        return Arrays.stream(arr).noneMatch(t2 -> {
            return Objects.equals(t2, t);
        });
    }

    @SafeVarargs
    public static <T> T[] allocate(T... args) {
        return args;
    }

    public interface ArrayIterationAction<E>{
        void action(E item);
    }

    public static <T>boolean startWith(T[] t,T[] arr){
        boolean b=true;
        for (int i=0;i<t.length;i++){
            if (t[i] != arr[i]) {
                b = false;
                break;
            }
        }
        return b;
    }

    public static boolean startWith(byte[] t,byte[] arr){
        boolean b=true;
        for (int i=0;i<t.length;i++){
            if (t[i] != arr[i]) {
                b = false;
                break;
            }
        }
        return b;
    }


    public static <T>boolean endWith(T[] t,T[] arr){
        boolean b=true;
        for (int i=arr.length-t.length;i<arr.length;i++){
            if (t[i] != arr[arr.length-t.length+i]) {
                b = false;
                break;
            }
        }
        return b;
    }


    public static float[] copySub(int start,int end,float[] arr){
        float[] result=new float[end-start];
        if (end - start >= 0) System.arraycopy(arr, start, result, 0, end - start);
        return result;
    }

    public static double[] copySub(int start,int end,double[] arr){
        double[] result=new double[end-start];
        for (int i=start;i<end;i++){
            result[i-start]=arr[i];
        }
        return result;
    }
}

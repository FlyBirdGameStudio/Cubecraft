package io.flybird.util.container;

import java.util.ArrayList;
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

    public interface ArrayIterationAction<E>{
        void action(E item);
    }

    @SafeVarargs
    public static <T> T[] allocate(T... args) {
        return args;
    }

    public static byte[] connect(byte[] ... a){
        ArrayList<Byte> data=new ArrayList<>();
        for (byte[] arr:a){
            for (byte b:arr){
                data.add(b);
            }
        }
        return unBox(data.toArray(new Byte[0]));
    }

    public static byte[] unBox(Byte[] data){
        byte[] data2=new byte[data.length];
        for (int i=0;i<data.length;i++){
            data2[i]=data[i];
        }
        return data2;
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

    public static byte[] copySub(int start, byte end, byte[] arr) {
        byte[] result=new byte[end-start];
        for (int i=start;i<end;i++){
            result[i-start]=arr[i];
        }
        return result;
    }


    public static <T> T getDispatched(int x,int y,int z,int size,T[] arr){
        return arr[y*size*size+z*size+x];
    }

    public static <T> void setDispatched(int x,int y,int z,int size,T[] arr,T target){
        arr[y*size*size+z*size+x]=target;
    }

    public static byte getDispatched(int x,int y,int z,int size,byte[] arr){
        return arr[y*size*size+z*size+x];
    }

    public static void setDispatched(int x,int y,int z,int size,byte[] arr,byte target){
        arr[y*size*size+z*size+x]=target;
    }

    public static double getDispatched(int x,int y,int z,int size,double[] arr){
        return arr[y*size*size+z*size+x];
    }

    public static void setDispatched(int x,int y,int z,int size,double[] arr,double target){
        arr[y*size*size+z*size+x]=target;
    }

    public static int getDispatched(int x,int y,int z,int size,int[] arr){
        return arr[y*size*size+z*size+x];
    }

    public static void setDispatched(int x,int y,int z,int size,int[] arr,byte target){
        arr[y*size*size+z*size+x]=target;
    }

}

package com.sunrisestudio.util.container.buffer;

import org.lwjgl.BufferUtils;

import java.nio.*;

public class BufferBuilder {

    public static ByteBuffer getB(byte... values){
        ByteBuffer Bb= BufferUtils.createByteBuffer(values.length);
        Bb.clear();
        Bb.put(values);
        Bb.flip();
        return Bb;
    }

    public static ShortBuffer getS(short... values){
        ShortBuffer Sb= BufferUtils.createShortBuffer(values.length);
        Sb.clear();
        Sb.put(values);
        Sb.flip();
        return Sb;
    }

    public static IntBuffer getI(int... values){
        IntBuffer Ib= BufferUtils.createIntBuffer(values.length);
        Ib.clear();
        Ib.put(values);
        Ib.flip();
        return Ib;
    }

    public static LongBuffer getL(long... values){
        LongBuffer Lb= BufferUtils.createLongBuffer(values.length);
        Lb.clear();
        Lb.put(values);
        Lb.flip();
        return Lb;
    }

    public static FloatBuffer getF(float... values){
        FloatBuffer Fb= BufferUtils.createFloatBuffer(values.length);
        Fb.clear();
        Fb.put(values);
        Fb.flip();
        return Fb;
    }

    public static DoubleBuffer getD(double... values){
        DoubleBuffer Db= BufferUtils.createDoubleBuffer(values.length);
        Db.clear();
        Db.put(values);
        Db.flip();
        return Db;
    }

    public static CharBuffer getC(char... values){
        CharBuffer Cb= BufferUtils.createCharBuffer(values.length);
        Cb.clear();
        Cb.put(values);
        Cb.flip();
        return Cb;
    }

    public static FloatBuffer convertDoubleBufferToFloatBuffer(DoubleBuffer buffer){
        float[] data=new float[buffer.capacity()];
        for (int i=0;i<buffer.capacity();i++){
            data[i]= (float) buffer.get(i);
        }
        return getF(data);
    }

    public static DoubleBuffer convertFloatBufferToDoubleBuffer(FloatBuffer buffer){
        double[] data=new double[buffer.capacity()];
        for (int i=0;i<buffer.capacity();i++){
            data[i]= buffer.get(i);
        }
        return getD(data);
    }
}

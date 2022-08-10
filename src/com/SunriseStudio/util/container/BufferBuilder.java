package com.sunrisestudio.util.container;

import com.sunrisestudio.util.file.nbt.NBTBase;
import com.sunrisestudio.util.file.nbt.NBTBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.*;

public class BufferBuilder {

    /**
     * create a buffer from values
     * we do not recommend you to use this method due to buffer is un collectable and will cause memory leak.
     *
     * @param values values
     * @return buffer
     */
    public static ByteBuffer from(byte... values) {
        ByteBuffer Bb = BufferUtils.createByteBuffer(values.length);
        Bb.clear();
        Bb.put(values);
        Bb.flip();
        return Bb;
    }

    public static ShortBuffer from(short... values) {
        ShortBuffer Sb = BufferUtils.createShortBuffer(values.length);
        Sb.clear();
        Sb.put(values);
        Sb.flip();
        return Sb;
    }

    public static IntBuffer from(int... values) {
        IntBuffer Ib = BufferUtils.createIntBuffer(values.length);
        Ib.clear();
        Ib.put(values);
        Ib.flip();
        return Ib;
    }

    public static LongBuffer from(long... values) {
        LongBuffer Lb = BufferUtils.createLongBuffer(values.length);
        Lb.clear();
        Lb.put(values);
        Lb.flip();
        return Lb;
    }

    public static FloatBuffer from(float... values) {
        FloatBuffer Fb = BufferUtils.createFloatBuffer(values.length);
        Fb.clear();
        Fb.put(values);
        Fb.flip();
        return Fb;
    }

    public static DoubleBuffer from(double... values) {
        DoubleBuffer Db = BufferUtils.createDoubleBuffer(values.length);
        Db.clear();
        Db.put(values);
        Db.flip();
        return Db;
    }

    public static CharBuffer from(char... values) {
        CharBuffer Cb = BufferUtils.createCharBuffer(values.length);
        Cb.clear();
        Cb.put(values);
        Cb.flip();
        return Cb;
    }

    /**
     * clear,fill an existing buffer with a set of values,and flip it
     * @param buffer buffer to fill
     * @param values values
     */
    public static void fillBuffer(DoubleBuffer buffer, double... values) {
        buffer.clear();
        buffer.put(values);
        buffer.flip();
    }

    public static void fillBuffer(FloatBuffer buffer, float... values) {
        buffer.clear();
        buffer.put(values);
        buffer.flip();
    }

    public static void fillBuffer(ShortBuffer buffer, short... values) {
        buffer.clear();
        buffer.put(values);
        buffer.flip();
    }

    public static void fillBuffer(ByteBuffer buffer, byte... values) {
        buffer.clear();
        buffer.put(values);
        buffer.flip();
    }

    public static void fillBuffer(IntBuffer buffer, int... values) {
        buffer.clear();
        buffer.put(values);
        buffer.flip();
    }

    public static void fillBuffer(CharBuffer buffer, char... values) {
        buffer.clear();
        buffer.put(values);
        buffer.flip();
    }

    /**
     * create buffer from existing buffer,convert to lower value.
     * remember:buffer is not collectable and will cause memory leaking.
     *
     * @param buffer existing buffer
     * @return new buffer
     */
    public static FloatBuffer double2float(DoubleBuffer buffer) {
        float[] data = new float[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = (float) buffer.get(i);
        }
        return from(data);
    }

    public static IntBuffer long2Int(LongBuffer buffer) {
        int[] data = new int[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = (int) buffer.get(i);
        }
        return from(data);
    }

    public static ShortBuffer int2Short(IntBuffer buffer) {
        short[] data = new short[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = (short) buffer.get(i);
        }
        return from(data);
    }

    public static ByteBuffer short2Byte(ShortBuffer buffer) {
        byte[] data = new byte[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = (byte) buffer.get(i);
        }
        return from(data);
    }

    /**
     * create buffer from existing buffer,convert to higher value.
     * remember:buffer is not collectable and will cause memory leaking.
     *
     * @param buffer existing buffer
     * @return new buffer
     */
    public static DoubleBuffer float2Double(FloatBuffer buffer) {
        double[] data = new double[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = buffer.get(i);
        }
        return from(data);
    }

    public static LongBuffer int2Long(IntBuffer buffer) {
        long[] data = new long[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = buffer.get(i);
        }
        return from(data);
    }

    public static IntBuffer short2Int(ShortBuffer buffer) {
        int[] data = new int[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = buffer.get(i);
        }
        return from(data);
    }

    public static ShortBuffer byte2Short(ByteBuffer buffer) {
        short[] data = new short[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = buffer.get(i);
        }
        return from(data);
    }



    public static ByteBuf fromNBT(NBTBase tag, int size) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer(size);
        try {
            ByteBufOutputStream stream=new ByteBufOutputStream(byteBuf);
            NBTBuilder.write(tag,stream);
            stream.close();
        } catch (Exception e) {
            throw new RuntimeException("could not write NBT:" + e);
        }
        return byteBuf;
    }

    public static NBTBase toNBT(ByteBuf byteBuf) {
        NBTBase base;
        try {
            ByteBufInputStream stream=new ByteBufInputStream(byteBuf);
            base= NBTBuilder.read(stream);
            stream.close();
        } catch (Exception e) {
            throw new RuntimeException("could not read nbt:" + e);
        }
        return base;
    }



    public static ByteBuf wrap(byte[] arr) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer(arr.length);
        try {
            ByteBufOutputStream stream=new ByteBufOutputStream(byteBuf);
            stream.write(arr);
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException("could not wrap byte buf:" + e);
        }
        return byteBuf;
    }

}

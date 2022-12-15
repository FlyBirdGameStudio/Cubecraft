package io.flybird.util.container;

import io.flybird.util.file.nbt.NBTBase;
import io.flybird.util.file.NBTBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.*;
import java.nio.charset.StandardCharsets;


/**
 * simple util for buffer
 *
 * @author GrassBlock2022
 */
public class BufferUtil {

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

    /**
     * create a buffer from values
     * we do not recommend you to use this method due to buffer is un collectable and will cause memory leak.
     *
     * @param values values
     * @return buffer
     */
    public static ShortBuffer from(short... values) {
        ShortBuffer Sb = BufferUtils.createShortBuffer(values.length);
        Sb.clear();
        Sb.put(values);
        Sb.flip();
        return Sb;
    }

    /**
     * create a buffer from values
     * we do not recommend you to use this method due to buffer is un collectable and will cause memory leak.
     *
     * @param values values
     * @return buffer
     */
    public static IntBuffer from(int... values) {
        IntBuffer Ib = BufferUtils.createIntBuffer(values.length);
        Ib.clear();
        Ib.put(values);
        Ib.flip();
        return Ib;
    }

    /**
     * create a buffer from values
     * we do not recommend you to use this method due to buffer is un collectable and will cause memory leak.
     *
     * @param values values
     * @return buffer
     */
    public static LongBuffer from(long... values) {
        LongBuffer Lb = BufferUtils.createLongBuffer(values.length);
        Lb.clear();
        Lb.put(values);
        Lb.flip();
        return Lb;
    }

    /**
     * create a buffer from values
     * we do not recommend you to use this method due to buffer is un collectable and will cause memory leak.
     *
     * @param values values
     * @return buffer
     */
    public static FloatBuffer from(float... values) {
        FloatBuffer Fb = BufferUtils.createFloatBuffer(values.length);
        Fb.clear();
        Fb.put(values);
        Fb.flip();
        return Fb;
    }

    /**
     * create a buffer from values
     * we do not recommend you to use this method due to buffer is un collectable and will cause memory leak.
     *
     * @param values values
     * @return buffer
     */
    public static DoubleBuffer from(double... values) {
        DoubleBuffer Db = BufferUtils.createDoubleBuffer(values.length);
        Db.clear();
        Db.put(values);
        Db.flip();
        return Db;
    }

    /**
     * create a buffer from values
     * we do not recommend you to use this method due to buffer is un collectable and will cause memory leak.
     *
     * @param values values
     * @return buffer
     */
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

    /**
     * clear,fill an existing buffer with a set of values,and flip it
     * @param buffer buffer to fill
     * @param values values
     */
    public static void fillBuffer(FloatBuffer buffer, float... values) {
        buffer.clear();
        buffer.put(values);
        buffer.flip();
    }

    /**
     * clear,fill an existing buffer with a set of values,and flip it
     * @param buffer buffer to fill
     * @param values values
     */
    public static void fillBuffer(ShortBuffer buffer, short... values) {
        buffer.clear();
        buffer.put(values);
        buffer.flip();
    }

    /**
     * clear,fill an existing buffer with a set of values,and flip it
     * @param buffer buffer to fill
     * @param values values
     */
    public static void fillBuffer(ByteBuffer buffer, byte... values) {
        buffer.clear();
        buffer.put(values);
        buffer.flip();
    }

    /**
     * clear,fill an existing buffer with a set of values,and flip it
     * @param buffer buffer to fill
     * @param values values
     */
    public static void fillBuffer(IntBuffer buffer, int... values) {
        buffer.clear();
        buffer.put(values);
        buffer.flip();
    }

    /**
     * clear,fill an existing buffer with a set of values,and flip it
     * @param buffer buffer to fill
     * @param values values
     */
    public static void fillBuffer(CharBuffer buffer, char... values) {
        buffer.clear();
        buffer.put(values);
        buffer.flip();
    }

    /**
     * create buffer from existing buffer,convert to another datatype value.
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

    /**
     * create buffer from existing buffer,convert to another datatype value.
     * remember:buffer is not collectable and will cause memory leaking.
     *
     * @param buffer existing buffer
     * @return new buffer
     */
    public static IntBuffer long2Int(LongBuffer buffer) {
        int[] data = new int[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = (int) buffer.get(i);
        }
        return from(data);
    }

    /**
     * create buffer from existing buffer,convert to another datatype value.
     * remember:buffer is not collectable and will cause memory leaking.
     *
     * @param buffer existing buffer
     * @return new buffer
     */
    public static ShortBuffer int2Short(IntBuffer buffer) {
        short[] data = new short[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = (short) buffer.get(i);
        }
        return from(data);
    }

    /**
     * create buffer from existing buffer,convert to another datatype value.
     * remember:buffer is not collectable and will cause memory leaking.
     *
     * @param buffer existing buffer
     * @return new buffer
     */
    public static ByteBuffer short2Byte(ShortBuffer buffer) {
        byte[] data = new byte[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = (byte) buffer.get(i);
        }
        return from(data);
    }

    /**
     * create buffer from existing buffer,convert to another datatype value.
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

    /**
     * create buffer from existing buffer,convert to another datatype value.
     * remember:buffer is not collectable and will cause memory leaking.
     *
     * @param buffer existing buffer
     * @return new buffer
     */
    public static LongBuffer int2Long(IntBuffer buffer) {
        long[] data = new long[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = buffer.get(i);
        }
        return from(data);
    }

    /**
     * create buffer from existing buffer,convert to another datatype value.
     * remember:buffer is not collectable and will cause memory leaking.
     *
     * @param buffer existing buffer
     * @return new buffer
     */
    public static IntBuffer short2Int(ShortBuffer buffer) {
        int[] data = new int[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = buffer.get(i);
        }
        return from(data);
    }

    /**
     * create buffer from existing buffer,convert to another datatype value.
     * remember:buffer is not collectable and will cause memory leaking.
     *
     * @param buffer existing buffer
     * @return new buffer
     */
    public static ShortBuffer byte2Short(ByteBuffer buffer) {
        short[] data = new short[buffer.capacity()];
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = buffer.get(i);
        }
        return from(data);
    }


    /**
     * build buffer from nbt.
     * @param tag tag
     * @return build buffer
     */
    public static ByteBuf fromNBT(NBTBase tag) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        try {
            ByteBufOutputStream stream=new ByteBufOutputStream(byteBuf);
            NBTBuilder.write(tag,stream);
            stream.close();
        } catch (Exception e) {
            throw new RuntimeException("could not write NBT:" + e);
        }
        return byteBuf;
    }

    /**
     * build nbt tag from bytebuffer.
     * @param byteBuf source
     * @return nbt
     */
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


    /**
     * wrap some data to buffer.
     * @param arr array.
     * @return wrapped buffer.
     */
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

    /**
     * free a fucking java.nio.buffer
     * @param buffer buffer
     */
    public static void free(ByteBuffer buffer){
        MemoryUtil.memSet(MemoryUtil.memAddress(buffer),0,buffer.capacity());
    }

    /**
     * free a fucking java.nio.buffer
     * @param buffer buffer
     */
    public static void free(FloatBuffer buffer){
        MemoryUtil.memSet(MemoryUtil.memAddress(buffer),0,buffer.capacity()* 4L);
    }

    /**
     * free a fucking java.nio.buffer
     * @param buffer buffer
     */
    public static void free(IntBuffer buffer) {
        MemoryUtil.memSet(MemoryUtil.memAddress(buffer),0,buffer.capacity()* 4L);
    }

    /**
     * free a fucking java.nio.buffer
     * @param buffer buffer
     */
    public static byte[] unwrap(ByteBuf buffer){
        byte[] data=new byte[buffer.writerIndex()-buffer.readerIndex()];
        buffer.readBytes(data);
        return data;
    }

    /**
     * read a string from buffer with metadata length,friendly-reading.
     * @param buffer target
     * @return string
     */
    public static String readString(ByteBuf buffer) {
        int len=buffer.readByte();
        return (String) buffer.readCharSequence(len, StandardCharsets.UTF_8);
    }

    /**
     * write a string to buffer with metadata length,friendly-reading.
     * @param buffer target
     */
    public static void writeString(String s,ByteBuf buffer) {
        buffer.writeByte(s.getBytes(StandardCharsets.UTF_8).length);
        buffer.writeBytes(s.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * read an array to buffer with metadata length,friendly-reading.
     * @param buffer target
     */
    public static void writeArray(byte[] arr,ByteBuf buffer) {
        buffer.writeInt(arr.length);
        buffer.writeBytes(arr);
    }
}

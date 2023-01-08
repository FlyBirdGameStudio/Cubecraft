package io.flybird.quantum3d;

import org.lwjgl.system.MemoryUtil;

import java.nio.*;

/**
 * traceable buffer allocation.I HATE YOU NIO BUFFER!
 *
 * @author GrassBlock2022
 */
public class BufferAllocation {
    static int alloc;

    public static ByteBuffer allocByteBuffer(int size){
        alloc+=size;
        return MemoryUtil.memAlloc(size);
    }

    public static ShortBuffer allocShortBuffer(int size){
        alloc+=size*2;
        return MemoryUtil.memAlloc(size*2).asShortBuffer();
    }

    public static IntBuffer allocIntBuffer(int size){
        alloc+=size*4;
        return MemoryUtil.memAlloc(size*4).asIntBuffer();
    }

    public static FloatBuffer allocFloatBuffer(int size){
        alloc+=size*4;
        return MemoryUtil.memAlloc(size*4).asFloatBuffer();
    }

    public static LongBuffer allocLongBuffer(int size){
        alloc+=size*8;
        return MemoryUtil.memAlloc(size*8).asLongBuffer();
    }

    public static DoubleBuffer allocDoubleBuffer(int size){
        alloc+=size*8;
        return MemoryUtil.memAlloc(size*8).asDoubleBuffer();
    }

    public static void free(ByteBuffer buffer){
        alloc-=buffer.capacity();
        MemoryUtil.memFree(buffer);
    }

    public static void free(ShortBuffer buffer){
        alloc-=buffer.capacity()*2;
        MemoryUtil.memFree(buffer);
    }

    public static void free(IntBuffer buffer){
        alloc-=buffer.capacity()*4;
        MemoryUtil.memFree(buffer);
    }

    public static void free(FloatBuffer buffer){
        alloc-=buffer.capacity()*4;
        MemoryUtil.memFree(buffer);
    }

    public static void free(LongBuffer buffer){
        alloc-=buffer.capacity()*8;
        MemoryUtil.memFree(buffer);
    }

    public static void free(DoubleBuffer buffer){
        alloc-=buffer.capacity()*8;
        MemoryUtil.memFree(buffer);
    }

    public static int getAllocSize() {
        return alloc;
    }
}

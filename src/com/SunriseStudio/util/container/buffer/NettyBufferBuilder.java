package com.sunrisestudio.util.container.buffer;

import com.sunrisestudio.util.nbt.NBTBase;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class NettyBufferBuilder {
    public static ByteBuf fromNBT(NBTBase tag,int size){
        ByteBuf byteBuf= ByteBufAllocator.DEFAULT.ioBuffer(size);
        try {
            NBTBase.writeNamedTag(tag,new ByteBufOutputStream(byteBuf));
        } catch (Exception e) {
            throw new RuntimeException("could not write NBT:"+e);
        }
        return byteBuf;
    }

    public static NBTBase toNBT(ByteBuf byteBuf){
        try {
            return NBTBase.readNamedTag(new ByteBufInputStream(byteBuf));
        } catch (Exception e) {
            throw new RuntimeException("could not read nbt:"+e);
        }
    }

    public static ByteBuf wrap(byte[] arr){
        ByteBuf byteBuf= ByteBufAllocator.DEFAULT.ioBuffer(arr.length);
        try {
            new ByteBufOutputStream(byteBuf).write(arr);
        } catch (IOException e) {
            throw new RuntimeException("could not wrap bytebuf:"+e);
        }
        return byteBuf;
    }
}

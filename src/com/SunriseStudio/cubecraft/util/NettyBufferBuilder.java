package com.SunriseStudio.cubecraft.util;

import com.SunriseStudio.cubecraft.util.nbt.NBTBase;
import com.SunriseStudio.cubecraft.util.nbt.NBTTagByte;
import com.SunriseStudio.cubecraft.util.nbt.NBTTagCompound;
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
}

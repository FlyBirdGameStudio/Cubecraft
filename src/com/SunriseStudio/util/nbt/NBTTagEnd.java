package com.sunrisestudio.util.nbt;

import java.io.DataInput;
import java.io.DataOutput;

public class NBTTagEnd extends NBTBase
{
    public NBTTagEnd() {
        super();
    }
    
    @Override
    void readTagContents(final DataInput dataInput) {
    }
    
    @Override
    void writeTagContents(final DataOutput dataOutput) {
    }
    
    @Override
    public byte getType() {
        return 0;
    }
    
    @Override
    public String toString() {
        return "END";
    }
}

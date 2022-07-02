package com.SunriseStudio.cubecraft.util.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagFloat extends NBTBase
{
    public float floatValue;
    
    public NBTTagFloat() {
        super();
    }
    
    public NBTTagFloat(final float float1) {
        super();
        this.floatValue = float1;
    }
    
    @Override
    void writeTagContents(final DataOutput dataOutput) throws IOException {
        dataOutput.writeFloat(this.floatValue);
    }
    
    @Override
    void readTagContents(final DataInput dataInput) throws IOException {
        this.floatValue = dataInput.readFloat();
    }
    
    @Override
    public byte getType() {
        return 5;
    }
    
    @Override
    public String toString() {
        return "" + this.floatValue;
    }
}

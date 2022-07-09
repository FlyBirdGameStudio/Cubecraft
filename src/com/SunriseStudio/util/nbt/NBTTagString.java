package com.sunrisestudio.util.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString extends NBTBase
{
    public String stringValue;
    
    public NBTTagString() {
        super();
    }
    
    public NBTTagString(final String string) {
        super();
        this.stringValue = string;
        if (string == null) {
            throw new IllegalArgumentException("Empty string not allowed");
        }
    }
    
    @Override
    void writeTagContents(final DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.stringValue);
    }
    
    @Override
    void readTagContents(final DataInput dataInput) throws IOException {
        this.stringValue = dataInput.readUTF();
    }
    
    @Override
    public byte getType() {
        return 8;
    }
    
    @Override
    public String toString() {
        return "" + this.stringValue;
    }
}

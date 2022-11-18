package io.flybird.util.net;

import io.flybird.util.math.NumberCodec;


public class IOByteBuffer {
    public byte[] arr;
    private int pos;

    public IOByteBuffer(int allocSize) {
        arr=new byte[allocSize];
    }

    public void writeByte(byte b) {
        arr[pos]=b;
        pos++;
    }

    public void writeShort(short s) {
        System.arraycopy(NumberCodec.split(s),0,this.arr,pos,2);
        pos+=2;
    }

    public void writeInt(int i) {
        System.arraycopy(NumberCodec.split(i),0,this.arr,pos,4);
        pos+=4;
    }

    public void writeLong(long l) {
        System.arraycopy(NumberCodec.split(l),0,this.arr,pos,8);
        pos+=8;
    }

    public void writeFloat(float f){
        System.arraycopy(NumberCodec.split(f),0,this.arr,pos,4);
        pos+=4;
    }

    public void writeLong(double d) {
        System.arraycopy(NumberCodec.split(d),0,this.arr,pos,8);
        pos+=8;
    }

    public byte[] getArray() {
        return this.arr;
    }

    public void writeByteArray(byte[] bytes) {
        System.arraycopy(bytes,0,this.arr,pos,bytes.length);
        pos+= bytes.length;
    }
}



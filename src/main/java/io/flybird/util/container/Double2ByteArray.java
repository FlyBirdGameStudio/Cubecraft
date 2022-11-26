package io.flybird.util.container;

public class Double2ByteArray {
    public byte[] array;

    public Double2ByteArray(int size) {
        this.array = new byte[size];
    }

    public void set(int index, double d) {
        this.array[index] = (byte) (d * 256 - 128);
    }

    public double get(int index) {
        return (this.array[index] + 128) / 256f;
    }

    public double[] export() {
        double[] raw = new double[this.array.length];
        for (int i = 0; i < this.array.length; i++) {
            raw[i]=this.get(i);
        }
        return raw;
    }

    public void setArr(double[] raw) {
        for (int i = 0; i < this.array.length; i++) {
            this.set(i, raw[i]);
        }
    }

    public void setData(byte[] arr){
        this.array=arr;
    }

    public byte[] getData(){
        return this.array;
    }
}

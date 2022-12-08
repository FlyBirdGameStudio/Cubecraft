package io.flybird.util.container;

import io.flybird.util.file.nbt.tag.NBTTagCompound;

public class DynamicNameIdMap {
    public short[] array;
    public final MultiMap<String,Short> mapping=new MultiMap<>();
    public short counter;

    public DynamicNameIdMap(int size) {
        this.array = new short[size];
    }

    public void set(int index,String id){
        if(!this.mapping.containsKey(id)){
             this.alloc(id);
        }
        this.array[index]=this.mapping.get(id);
    }

    public void alloc(String id){
        this.mapping.put(id,this.counter);
        this.counter++;
    }

    public void manageFragment(){
        String[] raw=new String[this.array.length];
        for (int i=0;i<this.array.length;i++){
            raw[i]=this.mapping.of(this.array[i]);
        }
        this.mapping.clear();
        this.counter=0;
        for (int i=0;i<this.array.length;i++){
            this.set(i,raw[i]);
        }
    }

    public String get(int index){
        return this.mapping.of(this.array[index]);
    }

    public void fill(String id) {
        for (int i=0;i<this.array.length;i++){
            this.set(i,id);
        }
    }

    public NBTTagCompound export() {
        NBTTagCompound tag=new NBTTagCompound();
        NBTTagCompound map=new NBTTagCompound();
        CollectionUtil.iterateMap(this.mapping, (key, item) -> map.setInteger(key,item));
        tag.setCompoundTag("map",map);
        tag.setIntArray("data",ArrayUtil.short2int(this.array));
        return tag;
    }

    public void setData(NBTTagCompound tag){
        this.mapping.clear();
        NBTTagCompound map=tag.getCompoundTag("map");
        CollectionUtil.iterateMap(this.mapping, (key, item) -> map.setInteger(key,item));
        CollectionUtil.iterateMap(map.getTagMap(), (key, item) -> mapping.put(key, (short) map.getInteger(key)));
        this.array=ArrayUtil.int2short(tag.getIntArray("data"));
        this.manageFragment();
    }

    public void setArr(String[] raw) {
        for (int i=0;i<this.array.length;i++){
            this.set(i,raw[i]);
        }
    }
}

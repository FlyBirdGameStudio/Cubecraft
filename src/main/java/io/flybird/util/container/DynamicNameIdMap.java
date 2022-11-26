package io.flybird.util.container;


public class DynamicNameIdMap {
    public final int[] array;
    public final MultiMap<String,Integer> mapping=new MultiMap<>();
    public int counter;

    public DynamicNameIdMap(int size) {
        this.array = new int[size];
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

    public String[] export() {
        String[] raw=new String[this.array.length];
        for (int i=0;i<this.array.length;i++){
            raw[i]=this.mapping.of(this.array[i]);
        }
        return raw;
    }

    public void setArr(String[] raw) {
        for (int i=0;i<this.array.length;i++){
            this.set(i,raw[i]);
        }
    }
}

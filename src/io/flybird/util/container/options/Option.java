package io.flybird.util.container.options;

import java.util.HashMap;

public class Option {
    private final HashMap<String,Object> items;

    public void set(String path,Object value){
        this.items.put(path,value);
    }

    public Object get(String path){
        return this.items.get(path);
    }

    public Option(String namespace){
        this.items=Options.getValues(namespace);
    }

    public Object getOrDefault(String s, Object d) {
        if(items.get(s)==null){
            return d;
        }else {
            return items.get(s);
        }
    }

    public int getAsInt(String s,int d) {
        if(!this.items.containsKey(s)){
            return d;
        }
        return (Integer)this.items.get(s);
    }
}

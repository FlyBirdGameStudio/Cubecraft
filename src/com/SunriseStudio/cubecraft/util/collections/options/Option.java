package com.SunriseStudio.cubecraft.util.collections.options;

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
}

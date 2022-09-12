package com.flybirdstudio.starfish3d.render.textures;

import java.util.HashMap;

public class TextureContainer <T extends Texture>{
    HashMap<String,T> mapping=new HashMap<>();

    public HashMap<String, T> getMapping() {
        return mapping;
    }

    public T get(String name){
        return this.mapping.get(name);
    }

    public void set(String name,T t){
        this.mapping.put(name,t);
    }

    public void bind(String name){
        this.get(name).bind();
    }

    public void unbind(String name){
        this.get(name).unbind();
    }
}

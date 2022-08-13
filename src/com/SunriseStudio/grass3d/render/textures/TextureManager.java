package com.sunrisestudio.grass3d.render.textures;

import com.sunrisestudio.util.container.namespace.NameSpaceMap;

import java.util.HashMap;

public class TextureManager{
    private HashMap<String,Texture2D> texture2d=new HashMap<>();

    public Texture2D create2DTexture(String path,boolean multiSample,boolean mipmap){
        if(!this.texture2d.containsKey(path)) {
            Texture2D object = new Texture2D(multiSample, mipmap);
            object.generateTexture();
            object.load(path);
            this.texture2d.put(path, object);
            return object;
        }
        return this.texture2d.get(path);
    }

    public Texture2D get(String path){
        return this.texture2d.get(path);
    }

    public void bind2dTexture(String path) {
        texture2d.get(path).bind();
    }

    public void unBind2dTexture(String path) {
        this.texture2d.get(path).unbind();
    }
}

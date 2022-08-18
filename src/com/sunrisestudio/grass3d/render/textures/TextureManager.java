package com.sunrisestudio.grass3d.render.textures;

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

    public Texture2D get2DTexture(String path){
        return this.texture2d.get(path);
    }

    public void bind2dTexture(String path) {
        texture2d.get(path).bind();
    }

    public void unBind2dTexture(String path) {
        this.texture2d.get(path).unbind();
    }


    private HashMap<String,Texture2DArray> textureArray2d=new HashMap<>();

    public Texture2DArray create2DTextureArray(String id,boolean multiSample,boolean mipmap){
        if(!this.texture2d.containsKey(id)) {
            Texture2DArray object = new Texture2DArray(multiSample, mipmap,256,16,16);
            object.generateTexture();
            object.load(id);
            this.textureArray2d.put(id, object);
            return object;
        }
        return this.textureArray2d.get(id);
    }

    public Texture2DArray get2DArrayTexture(String path){
        return this.textureArray2d.get(path);
    }

    public void bind2DArrayTexture(String path) {
        textureArray2d.get(path).bind();
    }

    public void unBind2DArrayTexture(String path) {
        this.texture2d.get(path).unbind();
    }
}

package com.sunrisestudio.grass3d.render.textures;

import com.sunrisestudio.util.container.namespace.NameSpaceMap;

public class TextureManager{
    private final NameSpaceMap<Texture2DArray> textureArray2d=new NameSpaceMap<>(":");

    public void register2DArrayTexture(String fullname,Texture2DArray object){
        this.textureArray2d.set(fullname,object);
    }

    public void register2DArrayTexture(String id,String namespace,Texture2DArray object){
        this.textureArray2d.set(id,namespace,object);
    }

    public Texture2DArray create2DArrayTexture(String fullname,int width,int height,int layers,boolean multiSample,boolean mipmap){
        Texture2DArray object=new Texture2DArray(multiSample,mipmap,layers,width,height);
        object.generateTexture();
        this.textureArray2d.set(fullname,object);
        return object;
    }

    public Texture2DArray create2DArrayTexture(String id,String namespace,int width,int height,int layers,boolean multiSample,boolean mipmap){
        Texture2DArray object=new Texture2DArray(multiSample,mipmap,layers,width,height);
        object.generateTexture();
        this.textureArray2d.set(id,namespace,object);
        return object;
    }


    private final NameSpaceMap<Texture2D> texture2d=new NameSpaceMap<>(":");

    public void register2DTexture(String fullname,Texture2D object){
        this.texture2d.set(fullname,object);
    }

    public void register2D(String id,String namespace,Texture2D object){
        this.texture2d.set(id,namespace,object);
    }

    public Texture2D create2DTexture(String fullname,boolean multiSample,boolean mipmap){
        Texture2D object=new Texture2D(multiSample,mipmap);
        object.generateTexture();
        this.texture2d.set(fullname,object);
        return object;
    }

    public Texture2D create2DTexture(String id,String namespace,boolean multiSample,boolean mipmap){
        Texture2D object=new Texture2D(multiSample,mipmap);
        object.generateTexture();
        this.texture2d.set(id,namespace,object);
        return object;
    }
}

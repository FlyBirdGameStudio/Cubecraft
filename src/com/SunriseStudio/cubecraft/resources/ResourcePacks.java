package com.SunriseStudio.cubecraft.resources;

import com.SunriseStudio.cubecraft.util.LogHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ResourcePacks {
    public static ResourcePacks instance=new ResourcePacks();
    public ArrayList<ResourcePack> resourcePacks=new ArrayList<>();
    public LogHandler logHandler=LogHandler.create("resouceLoader","client");


    public InputStream getResource(String path,String fallback){
        InputStream inputStream=null;
        for (ResourcePack resourcePack:resourcePacks){
            try {
                inputStream=resourcePack.getInput(path);
            } catch (IOException e) {
                logHandler.warning("can not read file:"+path+",because of "+e);
            }
            if(inputStream!=null){
                break;
            }
        }
        if (inputStream==null){
            inputStream=this.getClass().getResourceAsStream(path);
        }
        if (inputStream==null){
            inputStream=this.getClass().getResourceAsStream(fallback);
        }
        return inputStream;
    }


    public BufferedImage getImage(String path){
        try {
            return ImageIO.read(this.getResource(path, "resources/resource/minecraft/fallback/texture.png"));
        } catch (Exception e) {
            logHandler.warning("can not read textures:"+path+",because of "+e);
        }
        return new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
    }
}

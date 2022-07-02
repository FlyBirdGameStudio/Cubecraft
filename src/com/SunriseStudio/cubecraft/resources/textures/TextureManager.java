package com.SunriseStudio.cubecraft.resources.textures;

import java.util.HashMap;

public class TextureManager {
    public HashMap<String, Texture> textures;

    public void create(String id,Texture t){this.textures.put(id,t);}



}

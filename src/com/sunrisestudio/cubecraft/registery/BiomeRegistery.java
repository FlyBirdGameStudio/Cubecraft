package com.sunrisestudio.cubecraft.registery;

import com.sunrisestudio.cubecraft.world.biome.Biome;
import com.sunrisestudio.util.container.namespace.NameSpaceItemGetter;


public class BiomeRegistery {
    @NameSpaceItemGetter(id = "plains", namespace = "cubecraft")
    public Biome plains(){
        return new Biome(
                0.3,
                0.5,
                0.4,
                0,
                0.03, "cubecraft:plains",
                "cubecraft:stone",
                new String[0]
                );
    }
}

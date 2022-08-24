package com.sunrisestudio.cubecraft.world.biome;

import com.sunrisestudio.util.container.namespace.NameSpacedRegisterMap;

import java.util.ArrayList;
import java.util.Comparator;

public class BiomeMap extends NameSpacedRegisterMap<Biome,Object> {
    public BiomeMap() {
        super(null);
    }

    public Biome match(double continental, double temperature, double humidity, double erosion, double altitude){
        ArrayList<Biome> biomes= new ArrayList<>();
        for (String biome:this.idList()){
            biomes.add(this.get(biome));
        }
        biomes.sort((o1, o2) -> -Double.compare(
                o1.match(continental, temperature, humidity, erosion, altitude),
                o2.match(continental, temperature, humidity, erosion, altitude)
        ));
        return biomes.get(0);
    }


}

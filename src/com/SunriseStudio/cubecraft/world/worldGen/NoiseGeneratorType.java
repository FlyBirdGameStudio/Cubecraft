package com.SunriseStudio.cubecraft.world.worldGen;

import com.SunriseStudio.cubecraft.world.worldGen.noiseGenerator.ImprovedNoise;
import com.SunriseStudio.cubecraft.world.worldGen.noiseGenerator.PerlinNoise;
import com.SunriseStudio.cubecraft.world.worldGen.noiseGenerator.SimplexNoise;
import com.SunriseStudio.cubecraft.world.worldGen.noiseGenerator.Synth;

import java.util.Random;

public enum NoiseGeneratorType {
    Perlin,
    Simplex,
    Improved;

    public static Synth getNoise(NoiseGeneratorType noiseGeneratorType,long seed){
        return switch (noiseGeneratorType){
            case Perlin -> new PerlinNoise(new Random(seed),4);
            case Simplex -> new SimplexNoise(new Random(seed));
            case Improved -> new ImprovedNoise(new Random(seed));
        };
    }
}

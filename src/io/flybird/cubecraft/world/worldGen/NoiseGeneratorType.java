package io.flybird.cubecraft.world.worldGen;

import io.flybird.cubecraft.world.worldGen.noiseGenerator.ImprovedNoise;
import io.flybird.cubecraft.world.worldGen.noiseGenerator.PerlinNoise;
import io.flybird.cubecraft.world.worldGen.noiseGenerator.SimplexNoise;
import io.flybird.cubecraft.world.worldGen.noiseGenerator.Synth;

import java.util.Random;

public enum NoiseGeneratorType {
    Perlin,
    Simplex,
    Improved;

    public static Synth getNoise(NoiseGeneratorType noiseGeneratorType, long seed){
        return switch (noiseGeneratorType){
            case Perlin -> new PerlinNoise(new Random(seed),4);
            case Simplex -> new SimplexNoise(new Random(seed));
            case Improved -> new ImprovedNoise(new Random(seed));
        };
    }
}

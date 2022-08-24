package com.sunrisestudio.cubecraft.world.worldGen.noise;

import java.util.Random;


public final class NoiseGeneratorOctaves extends NoiseGenerator {
    private NoiseGeneratorPerlin[] generatorCollection;
    private int octaves;
    
    public NoiseGeneratorOctaves(final Random random, final int octaves) {
        this.octaves = octaves;
        this.generatorCollection = new NoiseGeneratorPerlin[octaves];
        for (int i = 0; i < octaves; ++i) {
            this.generatorCollection[i] = new NoiseGeneratorPerlin(random);
        }
    }
    
    @Override
    public final double NoiseGenerator(double double1, double double2) {
        double n = 0.0;
        double n2 = 1.0;
        for (int i = 0; i < this.octaves; ++i) {
            n += this.generatorCollection[i].NoiseGenerator(double1 / n2, double2 / n2) * n2;
            n2 *= 2.0;
        }
        return n;
    }
}

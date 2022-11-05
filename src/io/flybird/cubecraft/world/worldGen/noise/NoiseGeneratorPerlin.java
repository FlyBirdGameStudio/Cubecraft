package io.flybird.cubecraft.world.worldGen.noise;


import io.flybird.util.math.MathHelper;

import java.util.Random;

public final class NoiseGeneratorPerlin extends NoiseGenerator {
    private int[] permutations;
    
    public NoiseGeneratorPerlin() {
        this(new Random());
    }
    
    public NoiseGeneratorPerlin(final Random random) {
        this.permutations = new int[512];
        for (int i = 0; i < 256; ++i) {
            this.permutations[i] = i;
        }
        for (int i = 0; i < 256; ++i) {
            final int n = random.nextInt(256 - i) + i;
            final int n2 = this.permutations[i];
            this.permutations[i] = this.permutations[n];
            this.permutations[n] = n2;
            this.permutations[i + 256] = this.permutations[i];
        }
    }
    
    private static double generateNoise(final double double1) {
        return double1 * double1 * double1 * (double1 * (double1 * 6.0 - 15.0) + 10.0);
    }
    
    private static double lerp(final double double1, final double double2, final double double3) {
        return double2 + double1 * (double3 - double2);
    }
    
    private static double grad(int integer, final double double2, final double double3, final double double4) {
        final double n = ((integer &= 0xF) < 8) ? double2 : double3;
        final double n2 = (integer < 4) ? double3 : ((integer == 12 || integer == 14) ? double2 : double4);
        return (((integer & 0x1) == 0x0) ? n : (-n)) + (((integer & 0x2) == 0x0) ? n2 : (-n2));
    }
    
    @Override
    public final double NoiseGenerator(final double double1, final double double2) {
        double double3 = 0.0;
        double double4 = double2;
        double double5 = double1;
        int n = MathHelper.floor_double(double5) & 0xFF;
        int n2 = MathHelper.floor_double(double4) & 0xFF;
        final int n3 = MathHelper.floor_double(0.0) & 0xFF;
        double5 -= MathHelper.floor_double(double5);
        double4 -= MathHelper.floor_double(double4);
        double3 = 0.0 - MathHelper.floor_double(0.0);
        final double generateNoise = generateNoise(double5);
        final double generateNoise2 = generateNoise(double4);
        final double generateNoise3 = generateNoise(double3);
        int n4 = this.permutations[n] + n2;
        final int n5 = this.permutations[n4] + n3;
        n4 = this.permutations[n4 + 1] + n3;
        n = this.permutations[n + 1] + n2;
        n2 = this.permutations[n] + n3;
        n = this.permutations[n + 1] + n3;
        return lerp(generateNoise3, lerp(generateNoise2, lerp(generateNoise, grad(this.permutations[n5], double5, double4, double3), grad(this.permutations[n2], double5 - 1.0, double4, double3)), lerp(generateNoise, grad(this.permutations[n4], double5, double4 - 1.0, double3), grad(this.permutations[n], double5 - 1.0, double4 - 1.0, double3))), lerp(generateNoise2, lerp(generateNoise, grad(this.permutations[n5 + 1], double5, double4, double3 - 1.0), grad(this.permutations[n2 + 1], double5 - 1.0, double4, double3 - 1.0)), lerp(generateNoise, grad(this.permutations[n4 + 1], double5, double4 - 1.0, double3 - 1.0), grad(this.permutations[n + 1], double5 - 1.0, double4 - 1.0, double3 - 1.0))));
    }
}

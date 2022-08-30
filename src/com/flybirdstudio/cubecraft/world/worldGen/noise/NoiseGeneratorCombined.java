package com.flybirdstudio.cubecraft.world.worldGen.noise;

public final class NoiseGeneratorCombined extends NoiseGenerator {
    private NoiseGenerator noiseGenerator;
    private NoiseGenerator noiseGenerator2;
    
    public NoiseGeneratorCombined(final NoiseGenerator noiseGenerator, final NoiseGenerator noiseGenerator2) {
        this.noiseGenerator = noiseGenerator;
        this.noiseGenerator2 = noiseGenerator2;
    }
    
    @Override
    public final double NoiseGenerator(final double double1, final double double2) {
        return this.noiseGenerator.NoiseGenerator(double1 + this.noiseGenerator2.NoiseGenerator(double1, double2), double2);
    }
}

package com.flybirdstudio.cubecraft.world.worldGen.noiseGenerator;

public class CombinedNoise extends Synth{
    private Synth noiseGenerator;
    private Synth noiseGenerator2;

    public CombinedNoise(final Synth noiseGenerator, final Synth noiseGenerator2) {
        this.noiseGenerator = noiseGenerator;
        this.noiseGenerator2 = noiseGenerator2;
    }

    @Override
    public final double getValue(double x, double y) {
        return this.noiseGenerator.getValue(x + this.noiseGenerator2.getValue(x, y), y);
    }
}

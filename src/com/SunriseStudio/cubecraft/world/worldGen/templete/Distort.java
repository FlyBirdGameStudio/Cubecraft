package com.sunrisestudio.cubecraft.world.worldGen.templete;

import com.sunrisestudio.cubecraft.world.worldGen.noiseGenerator.Synth;

public class Distort
extends Synth {
    private Synth source;
    private Synth distort;

    public Distort(Synth source, Synth distort) {
        this.source = source;
        this.distort = distort;
    }

    @Override
    public double getValue(double x, double y) {
        return this.source.getValue(x + this.distort.getValue(x, y), y);
    }
}

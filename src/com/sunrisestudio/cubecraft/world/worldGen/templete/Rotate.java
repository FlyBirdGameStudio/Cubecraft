package com.sunrisestudio.cubecraft.world.worldGen.templete;

import com.sunrisestudio.cubecraft.world.worldGen.noiseGenerator.Synth;

public class Rotate
extends Synth {
    private Synth synth;
    private double sin;
    private double cos;

    public Rotate(Synth synth, double angle) {
        this.synth = synth;
        this.sin = Math.sin(angle);
        this.cos = Math.cos(angle);
    }

    @Override
    public double getValue(double x, double y) {
        return this.synth.getValue(x * this.cos + y * this.sin, y * this.cos - x * this.sin);
    }
}

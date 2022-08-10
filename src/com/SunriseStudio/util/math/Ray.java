package com.sunrisestudio.util.math;

import org.joml.Vector3d;

public class Ray {
    private final double fromX,fromY,fromZ;
    private final double destX,destY,destZ;

    public Ray(double fromX, double fromY, double fromZ, double destX, double destY, double destZ) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.fromZ = fromZ;
        this.destX = destX;
        this.destY = destY;
        this.destZ = destZ;
    }

}

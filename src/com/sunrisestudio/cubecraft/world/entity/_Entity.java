package com.sunrisestudio.cubecraft.world.entity;

import com.sunrisestudio.util.math.HitResult;
import com.sunrisestudio.cubecraft.world._Level;
import com.sunrisestudio.util.math.AABB;

import java.util.ArrayList;

@Deprecated
public class _Entity {

    protected _Level world;

    //position
    public double xo;
    public double yo;
    public double zo;
    public double x;
    public double y;
    public double z;



    //rotation
    public float yRot;
    public float xRot;

    //physic
    public AABB collisionBox;

    public boolean onGround = false;

    public boolean removed = false;
    protected float heightOffset = 0.0f;
    protected float bbWidth = 0.6f;
    protected float bbHeight = 1.8f;

    public _Entity(_Level world) {
        this.world = world;
        this.resetPos();
    }

    protected void resetPos() {
        this.setPos(x, y, z);
    }

    public void remove() {
        this.removed = true;
    }

    protected void setSize(float w, float h) {
        this.bbWidth = w;
        this.bbHeight = h;
    }

    public void setPos(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        float w = this.bbWidth / 2.0f;
        this.collisionBox = new AABB(x - w, y , z - w, x + w, y +bbHeight, z + w);
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
    }

    public void move(double xa, double ya, double za) {
    }

    public boolean isLit() {
        return false;
    }

    public void render(float a) {
    }

}


package com.sunrisestudio.cubecraft.world.entity;

import com.sunrisestudio.util.math.HitResult;
import com.sunrisestudio.cubecraft.world._Level;
import com.sunrisestudio.util.math.AABB;

import java.util.ArrayList;

public strictfp class _Entity {
    public int selectSlot = 0;
    public HitResult hitResult;

    protected _Level world;

    //position
    public double xo;
    public double yo;
    public double zo;
    public double x;
    public double y;
    public double z;

    //movement
    public double xd;
    public double yd;
    public double zd;

    //rotation
    public float yRot;
    public float xRot;
    public float zRot;

    //physic
    public AABB collisionBox;
    public AABB selectionBox;
    public boolean onGround = false;
    public boolean horizontalCollision = false;
    public boolean removed = false;
    protected float heightOffset = 0.0f;
    protected float bbWidth = 0.6f;
    protected float bbHeight = 1.8f;

    public _Entity(_Level world) {
        this.world = world;

        this.resetPos();
        this.world.entities.add(this);
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
        float h = this.bbHeight / 2.0f;
        this.collisionBox = new AABB(x - w, y , z - w, x + w, y +bbHeight, z + w);
    }

    public void turn(float xo, float yo) {
        this.yRot = (this.yRot + xo * 0.15f);
        this.xRot = (this.xRot - yo * 0.15f);
        if (this.xRot < -90.0f) {
            this.xRot = -90.0f;
        }
        if (this.xRot > 90.0f) {
            this.xRot = 90.0f;
        }
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
    }

    public boolean isFree(double xa, double ya, double za) {
        AABB box = this.collisionBox.cloneMove(xa, ya, za);
        ArrayList<AABB> aABBs = this.world.getCollisionBox(box);
        if (aABBs.size() > 0) {
            return false;
        }
        return !this.world.containsAnyLiquid(box);
    }

    public void move(double xa, double ya, double za) {
        int i;
        double xaOrg = xa;
        double yaOrg = ya;
        double zaOrg = za;
        ArrayList<AABB> aABBs = this.world.getCollisionBox(this.collisionBox.expand(xa, ya, za));
        for (i = 0; i < aABBs.size(); ++i) {
            if (aABBs.get(i) != null)
                ya = ((AABB) aABBs.get(i)).clipYCollide(this.collisionBox, ya);
        }
        this.collisionBox.move(0.0f, ya, 0.0f);
        for (i = 0; i < aABBs.size(); ++i) {
            if (aABBs.get(i) != null)
                xa = ((AABB) aABBs.get(i)).clipXCollide(this.collisionBox, xa);
        }
        this.collisionBox.move(xa, 0.0f, 0.0f);
        for (i = 0; i < aABBs.size(); ++i) {
            if (aABBs.get(i) != null)
                za = ((AABB) aABBs.get(i)).clipZCollide(this.collisionBox, za);
        }
        this.collisionBox.move(0.0f, 0.0f, za);
        this.horizontalCollision = xaOrg != xa || zaOrg != za;
        boolean bl = this.onGround = yaOrg != ya && yaOrg < 0.0f;
        if (xaOrg != xa) {
            this.xd = 0.0f;
        }
        if (yaOrg != ya) {
            this.yd = 0.0f;
        }
        if (zaOrg != za) {
            this.zd = 0.0f;
        }
        this.x = (this.collisionBox.x0 + this.collisionBox.x1) / 2.0f;
        this.y = this.collisionBox.y0 + this.heightOffset;
        this.z = (this.collisionBox.z0 + this.collisionBox.z1) / 2.0f;
    }

    public boolean isInWater() {
        return this.world.containsLiquid(this.collisionBox.grow(0.0f, -0.4f, 0.0f), 1);
    }

    public boolean isInLava() {
        return this.world.containsLiquid(this.collisionBox, 2);
    }

    public void moveRelative(double xa, double za, float speed) {
        double dist = xa * xa + za * za;
        if (dist < 0.01f) {
            return;
        }
        dist = speed / (float) Math.sqrt(dist);
        float sin = (float) Math.sin((double) this.yRot * Math.PI / 180.0);
        float cos = (float) Math.cos((double) this.yRot * Math.PI / 180.0);
        this.xd += (xa *= dist) * cos - (za *= dist) * sin;
        this.zd += za * cos + xa * sin;
    }

    public boolean isLit() {
        int xTile = (int) this.x;
        int yTile = (int) this.y;
        int zTile = (int) this.z;
        return this.world.isLit(xTile, yTile, zTile);
    }

    public void render(float a) {
    }

    public double getReachDistance() {
        return 2.5;
    }
}


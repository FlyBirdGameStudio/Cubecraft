package com.sunrisestudio.cubecraft.world.entity;

import com.sunrisestudio.cubecraft.world._Level;
import org.lwjglx.input.Keyboard;

public class _Player
extends _Entity {
    public static final int KEY_UP = 0;
    public static final int KEY_DOWN = 1;
    public static final int KEY_LEFT = 2;
    public static final int KEY_RIGHT = 3;
    public static final int KEY_JUMP = 4;
    public static final int KEY_FLYUP = 5;
    public static final int KEY_FLYDOWN = 6;
    public static final int KEY_ADDFASTVALUE = 7;
    public static final int KEY_MINUSFASEVALUE = 8;
    public boolean Noclip = false;
    public float FastValue = 0.6f;
    public boolean[] keys = new boolean[100];
    public double xa=0.0;
    public double za=0.0;
    public boolean runningMode;
    public boolean flyingMode=true;


    public _Player(_Level world) {
        super(world);
        this.heightOffset = 1.62f;
    }

    public void releaseAllKeys() {
        for (int i = 0; i < 10; ++i) {
            this.keys[i] = false;
        }
    }

    public void setKey(final int key, final boolean state) {
        int id = -1;
        if (key == Keyboard.KEY_W ) {
            id = 0;
        }
        if (key == Keyboard.KEY_S) {
            id = 1;
        }
        if (key == Keyboard.KEY_A) {
            id = 2;
        }
        if (key == Keyboard.KEY_D) {
            id = 3;
        }
        if (key == Keyboard.KEY_SPACE) {
            id = 4;
        }
        if (key == Keyboard.KEY_LSHIFT) {
            id = 5;
        }
        if (keys[Keyboard.KEY_LCONTROL]) {
            this.runningMode=!this.runningMode;
        }
        if (keys[Keyboard.KEY_Z]) {
            this.flyingMode=!this.flyingMode;
        }
        if (id >= 0) {
            this.keys[id] = state;
        }


    }



    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        float xa = 0.0f;
        float za = 0.0f;
        float ya = 0.0f;
        float speed;
        final boolean inWater = this.isInWater();
        final boolean inLava = this.isInLava();
        {
            if (this.keys[0]) {
                ya -= 1;
            }
            if (this.keys[1]) {
                ya += 1;
            }
            if (this.keys[2]) {
                xa -= 1;
            }
            if (this.keys[3]) {
                xa += 1;
            }
            if (this.keys[4]) {
                if (!flyingMode) {
                    if (inWater) {
                        this.yd += 0.23f;
                    } else if (inLava) {
                        this.yd += 0.06f;
                    } else if (this.onGround) {
                        this.yd = 0.45f;
                    }
                } else {
                    yd = 0.45f;
                }
            }
            if (this.keys[5]) {
                if (flyingMode) {
                    yd = -0.45f;
                }
            }
        }//keys

        {
            if (yRot > 180) {
                yRot = -180;
            }
            if (yRot < -180) {
                yRot = 180;
            }
            if (xRot > 180) {
                xRot = -180;
            }
            if (yRot < -180) {
                xRot = 180;
            }
        }//facings

        {
            if (flyingMode) {
                if (runningMode) {
                    speed = 2.77f;
                } else {
                    speed = 2f;
                }
            } else {
                if (runningMode) {
                    speed = 1.58f;
                } else {
                    speed = 1.0f;
                }
            }
        }//setspeed

        {
                if (inWater) {
                    this.moveRelative(xa, ya, 0.027f * speed);
                    this.move(this.xd, this.yd, this.zd);
                    this.xd *= 0.8f;
                    this.yd *= 0.8f;
                    this.zd *= 0.8f;
                    if (!flyingMode) {
                        this.yd -= 0.08;
                    }
                    if (this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6f - this.y + yo, this.zd)) {
                        this.yd = 0.3f;
                    }

                } else if (inLava) {
                    this.moveRelative(xa, ya, 0.02f * speed);
                    this.move(this.xd, this.yd, this.zd);
                    this.xd *= 0.5f;
                    this.yd *= 0.5f;
                    this.zd *= 0.5f;
                    if (!flyingMode) {
                        this.yd -= 0.02;
                    }
                    if (this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6f - this.y + yo, this.zd)) {
                        this.yd = 0.3f;
                    }
                } else {
                    this.moveRelative(xa, ya, this.onGround ? 0.1f * speed : 0.02f * speed);
                    this.move(this.xd, this.yd, this.zd);
                    this.xd *= 0.91f;
                    this.yd *= 0.98f;
                    this.zd *= 0.91f;
                    if (!flyingMode) {
                        this.yd -= 0.08;
                    }else{
                        this.yd*=0.5f;
                    }
                    if (this.onGround) {
                        this.xd *= 0.6f;
                        this.zd *= 0.6f;
                    }
                }
        }//move
    }
}

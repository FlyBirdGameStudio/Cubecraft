package com.sunrisestudio.cubecraft;

import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.grass3d.platform.Keyboard;
import com.sunrisestudio.grass3d.platform.Mouse;
import com.sunrisestudio.grass3d.platform.input.InputHandler;

import java.util.Arrays;

public class PlayerController {
    private Entity entity;
    public boolean[] keys = new boolean[100];

    public PlayerController(Entity e){
        this.entity=e;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
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
            this.entity.runningMode=!this.entity.runningMode;
        }
        if (keys[Keyboard.KEY_Z]) {
            this.entity.flyingMode=!this.entity.flyingMode;
        }
        if (id >= 0) {
            this.keys[id] = state;
        }
    }

    public void tick(){
        float xa = 0.0f;
        float za = 0.0f;
        float ya = 0.0f;
        float speed = 1.5f;
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
                if (!this.entity.flyingMode) {
                    if (this.entity.inLiquid()) {
                        this.entity.yd += 0.23f;
                    } else if (this.entity.onGround) {
                        this.entity.yd = 0.45f;
                    }
                } else {
                    entity.yd = 0.45f;
                }
            }
            if (this.keys[5]) {
                if (entity.flyingMode) {
                    entity.yd = -0.45f;
                }else{
                    this.entity.sneak=!this.entity.sneak;
                    this.keys[5]=false;
                }
            }
            this.entity.moveRelative(xa, ya, this.entity.onGround ? 0.1f * speed : 0.02f * speed);
        }//keys

        if(InputHandler.isDoubleClicked(Keyboard.KEY_SPACE,250)){
            this.entity.flyingMode=!this.entity.flyingMode;
        }
    }

    public void tickFast(){
        this.entity.turn(Mouse.getDX(),Mouse.getDY(),0);
    }

    public void releaseAllKeys() {
        Arrays.fill(this.keys, false);
    }
}

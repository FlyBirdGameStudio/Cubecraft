package io.flybird.cubecraft.client;

import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.starfish3d.platform.input.Keyboard;
import io.flybird.starfish3d.platform.input.KeyboardCallback;
import io.flybird.starfish3d.platform.input.Mouse;
import io.flybird.starfish3d.platform.input.InputHandler;

import java.util.Arrays;

public class PlayerController {
    private Entity entity;
    public boolean[] keys = new boolean[100];

    public PlayerController(Entity e){
        this.entity=e;
        InputHandler.registerGlobalKeyboardCallback("cubecraft:player_controller",new KeyboardCallback(){
            @Override
            public void onKeyEventPressed() {
                if(Keyboard.getEventKey()==Keyboard.KEY_LCONTROL){
                    entity.runningMode=!entity.runningMode;
                }
            }
        });
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void tick(){
        float xa = 0.0f;
        float za = 0.0f;
        float ya = 0.0f;
        float speed = 1.5f;
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                ya -= 1;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                ya += 1;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                xa -= 1;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                xa += 1;
            }
            if (entity.flyingMode) {
                if (entity.runningMode) {
                    speed = 2.77f;
                } else {
                    speed = 2f;
                }
            } else {
                if (entity.runningMode) {
                    speed = 1.38f;
                } else {
                    speed = 0.9f;
                }
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
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
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                if (entity.flyingMode) {
                    entity.yd = -0.45f;
                }else{
                    this.entity.sneak=!this.entity.sneak;
                    this.keys[5]=false;
                }
            }
            this.entity.moveRelative(xa, ya, this.entity.onGround ? 0.1f * speed : 0.02f * speed);
        }//keys

        if(InputHandler.isKeyDoubleClicked(Keyboard.KEY_SPACE,250)){
            this.entity.flyingMode=!this.entity.flyingMode;
        }
    }

    public void tickFast(){
        this.entity.turn(Mouse.getDX(),Mouse.getDY(),0);
    }

    public void releaseAllKeys() {
        Arrays.fill(this.keys, false);
    }


    public static final String[] list=new String[]{
            "cubecraft:stone",
            "cubecraft:dirt",
            "cubecraft:grass_block",
            "cubecraft:oak_leaves",
            "cubecraft:oak_log",
            "cubecraft:dark_oak_leaves",
            "cubecraft:spruce_leaves",
            "cubecraft:birch_leaves",
            "cubecraft:acacia_leaves",

            "cubecraft:stone",
            "cubecraft:dirt",
            "cubecraft:grass_block",
            "cubecraft:oak_leaves",
            "cubecraft:dark_oak_leaves",
            "cubecraft:spruce_leaves",
            "cubecraft:birch_leaves",
    };
    public void setSelectedSlot(int slot){
        this.entity.selectedBlockID=list[slot];
    }
}
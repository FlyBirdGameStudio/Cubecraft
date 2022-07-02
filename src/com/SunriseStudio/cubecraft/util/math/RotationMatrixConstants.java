package com.SunriseStudio.cubecraft.util.math;

import com.SunriseStudio.cubecraft.world.block.BlockFacing;
import org.joml.Matrix4f;

public class RotationMatrixConstants {
    public static final Matrix4f FACE_LEFT=new Matrix4f().
            translate(-0.5f,-0.5f,-0.5f).
            rotateXYZ(0,0,-90).
            translate(0.5f,0.5f,0.5f);
    public static final Matrix4f FACE_RIGHT=new Matrix4f().
            translate(-0.5f,-0.5f,-0.5f).
            rotateXYZ(0,0,90).
            translate(0.5f,0.5f,0.5f);
    public static final Matrix4f FACE_FRONT=new Matrix4f().
            translate(-0.5f,-0.5f,-0.5f).
            rotateXYZ(-90,0,0).
            translate(0.5f,0.5f,0.5f);
    public static final Matrix4f FACE_BACK=new Matrix4f().
            translate(-0.5f,-0.5f,-0.5f).
            rotateXYZ(-90,0,-90).
            translate(0.5f,0.5f,0.5f);
    public static final Matrix4f FACE_TOP=new Matrix4f().
            translate(-0.5f,-0.5f,-0.5f).
            rotateXYZ(0,0,0).
            translate(0.5f,0.5f,0.5f);
    public static final Matrix4f FACE_BOTTOM=new Matrix4f().
            translate(-0.5f,-0.5f,-0.5f).
            rotateXYZ(0,0,180).
            translate(0.5f,0.5f,0.5f);

    public static Matrix4f get(BlockFacing facing) {
        return switch (facing){
            case Up -> FACE_TOP;
            case Down -> FACE_BOTTOM;
            case East -> FACE_RIGHT;
            case West -> FACE_LEFT;
            case North -> FACE_BACK;
            case South -> FACE_FRONT;
        };
    }
}

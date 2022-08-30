package com.flybirdstudio.starfish3d.audio;

import org.joml.Vector3d;
import org.lwjgl.openal.AL10;

public class Sound {
    public Vector3d position=new Vector3d();
    int alSource;
    public void generate(int buffer){
        this.alSource=AL10.alGenSources();
    }

    public void play(){
        AL10.alSourcePlay(this.alSource);
    }

    public void stop(){
        AL10.alSourceStop(this.alSource);
    }

    public void setPos(double x,double y,double z){
        this.position.set(x,y,z);
    }

    public Vector3d getPosition() {
        return position;
    }
}

package com.sunrisestudio.grass3d.render.shaders;

import org.lwjgl.opengl.GL20;

public class Program {
    private int glID;

    public void attachShader(Shader shader){
        GL20.glAttachShader(glID,shader.getGlShader());
    }

    public void link(){
        GL20.glLinkProgram(glID);
    }

    public int getGlID() {
        return glID;
    }
}

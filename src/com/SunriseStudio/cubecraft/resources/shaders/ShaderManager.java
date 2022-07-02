package com.SunriseStudio.cubecraft.resources.shaders;

import org.lwjgl.opengl.GL20;

import java.util.HashMap;

public class ShaderManager {
    private static final HashMap<String,Shader> shaders = new HashMap();
    private static final HashMap<String,Program> programs = new HashMap();

    public static void bindProgram(String programID) {
        GL20.glUseProgram(programs.get(programID).getGlID());
    }
}

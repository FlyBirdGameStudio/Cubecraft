package com.sunrisestudio.grass3d.render.shaders;

import org.lwjgl.opengl.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Shader{
    private final int glShader;
    private boolean openForAttach=true;

    public int getGlShader() {
        return glShader;
    }

    private Shader(int shader){
        this.glShader=GL20.glCreateShader(shader);
    }

    public void attachSource(InputStream in) throws IOException {
        if(this.openForAttach) {
            ArrayList<Integer> inputStreamFuckYou = new ArrayList<>();
            int byt;
            while ((byt = in.read()) != -1) {
                inputStreamFuckYou.add(byt);
            }
            Integer[] i = inputStreamFuckYou.toArray(new Integer[0]);
            byte[] b = new byte[inputStreamFuckYou.size()];
            for (int c = 0; c < i.length; c++) {
                b[c] = (byte) (i[c] - 128);
            }
            GL20.glShaderSource(glShader, new String(b, StandardCharsets.US_ASCII));
        }
    }

    public void compile(){
        GL20.glCompileShader(this.glShader);
        this.openForAttach=false;
    }

    public static Shader createNewVertexShader(){
        return new Shader(GL20.GL_VERTEX_SHADER);
    }

    public static Shader createNewFragmentShader(){
        return new Shader(GL20.GL_VERTEX_SHADER);
    }

    public static Shader createNewGeometryShader(){
        return new Shader(GL32.GL_GEOMETRY_SHADER);
    }

    public static Shader createNewTessControlShader(){
        return new Shader(GL40.GL_TESS_CONTROL_SHADER);
    }

    public static Shader createNewTessEvaluationShader(){
        return new Shader(GL40.GL_TESS_EVALUATION_SHADER);
    }

    public static Shader createNewConputeShader(){
        return new Shader(GL43.GL_COMPUTE_SHADER);
    }
}

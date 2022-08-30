package com.flybirdstudio.starfish3d.render;

import org.lwjgl.opengl.*;

public class FrameBuffer {
    int glID;
    public void alloc(){
        this.glID= GL30.glGenFramebuffers();

    }
}

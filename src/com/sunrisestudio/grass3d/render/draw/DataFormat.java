package com.sunrisestudio.grass3d.render.draw;

import org.lwjgl.opengl.GL11;

public enum DataFormat {
    Float1(GL11.GL_FLOAT,1,4),
    Float2(GL11.GL_FLOAT,2,8),
    Float3(GL11.GL_FLOAT,3,12),
    Float4(GL11.GL_FLOAT,4,16),

    Double1(GL11.GL_DOUBLE,1,8),
    Double2(GL11.GL_DOUBLE,2,16),
    Double3(GL11.GL_DOUBLE,3,24),
    Double4(GL11.GL_DOUBLE,4,32),

    Int1(GL11.GL_INT,1,4),
    Int2(GL11.GL_INT,2,8),
    Int3(GL11.GL_INT,3,12),
    Int4(GL11.GL_INT,4,16),

    Byte1(GL11.GL_BYTE,1,1),
    Byte2(GL11.GL_BYTE,2,2),
    Byte3(GL11.GL_BYTE,3,3),
    Byte4(GL11.GL_BYTE,4,4);

    final int glType;
    final int elements;
    final int bytes;

    DataFormat(int glType,int elements,int bytes){
        this.glType=glType;
        this.elements=elements;
        this.bytes=bytes;
    }
}

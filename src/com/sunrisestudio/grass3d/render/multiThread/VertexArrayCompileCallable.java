package com.sunrisestudio.grass3d.render.multiThread;

import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;

public interface VertexArrayCompileCallable {
    IVertexArrayBuilder compile();
    int getList();
}

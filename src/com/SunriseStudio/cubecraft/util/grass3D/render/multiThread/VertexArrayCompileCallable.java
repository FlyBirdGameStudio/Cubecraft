package com.SunriseStudio.cubecraft.util.grass3D.render.multiThread;

import com.SunriseStudio.cubecraft.util.grass3D.render.draw.IVertexArrayBuilder;

public interface VertexArrayCompileCallable {
    IVertexArrayBuilder compile();
    int getList();
}

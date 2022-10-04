package com.flybirdstudio.starfish3d.render.drawcall;

import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;

public interface IRenderCall {
    static IRenderCall create(boolean useVBO) {
        if(useVBO){
            return new VBORenderCall();
        }else{
            return new ListRenderCall();
        }
    }

    void call();
    void upload(VertexArrayBuilder builder);
    void allocate();
    void free();
}

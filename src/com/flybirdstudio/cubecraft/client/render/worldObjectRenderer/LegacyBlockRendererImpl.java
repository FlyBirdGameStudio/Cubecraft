package com.flybirdstudio.cubecraft.client.render.worldObjectRenderer;

import com.flybirdstudio.cubecraft.client.render.model.RenderType;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.BlockState;

public class LegacyBlockRendererImpl extends LegacyBlockRenderer{
    final String tex;

    public LegacyBlockRendererImpl(String tex, RenderType type) {
        super(type);
        this.tex = tex;
    }

    public LegacyBlockRendererImpl(String tex){
        this(tex,RenderType.ALPHA);
    }

    @Override
    public String getTexture(IWorld w, BlockState bs, long x, long y, long z, int face) {
        return this.tex;
    }


}

package io.flybird.cubecraft.client.render.worldObjectRenderer;

import io.flybird.cubecraft.client.render.model.RenderType;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;

public class LegacyBlockRendererImpl extends LegacyBlockRenderer{
    final String tex;

    public LegacyBlockRendererImpl(String tex, RenderType type, ResourceLocation... usedTextures) {
        super(type, usedTextures);
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

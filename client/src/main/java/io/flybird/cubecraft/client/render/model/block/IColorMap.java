package io.flybird.cubecraft.client.render.model.block;

import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;

import java.awt.image.BufferedImage;

public abstract class IColorMap {
    protected BufferedImage image;
    protected final ResourceLocation loc;
    public IColorMap(ResourceLocation loc) {
        this.loc = loc;
    }

    public void load(){
        this.image= ClientRegistries.RESOURCE_MANAGER.getResource(loc).getAsImage();
    }

    public abstract int sample(IWorld w, BlockState bs, long x, long y, long z);
}

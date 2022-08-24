package com.sunrisestudio.cubecraft.registery;

import com.sunrisestudio.cubecraft.client.render.worldObjectRenderer.BlockModelRenderer;
import com.sunrisestudio.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import com.sunrisestudio.util.container.namespace.NameSpaceItemGetter;

public class BlockRendererRegistery {
    @NameSpaceItemGetter(id = "grass_block",namespace="cubecraft")
    public IBlockRenderer grass_block(){
        return new BlockModelRenderer();
    }

    @NameSpaceItemGetter(id = "stone",namespace="cubecraft")
    public IBlockRenderer stone(){
        return new BlockModelRenderer();
    }

    @NameSpaceItemGetter(id = "dirt",namespace="cubecraft")
    public IBlockRenderer dirt(){
        return new BlockModelRenderer();
    }
}

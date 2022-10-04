package com.flybirdstudio.cubecraft.registery.block;

import com.flybirdstudio.cubecraft.client.render.model.RenderType;
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.LegacyBlockRenderer;
import com.flybirdstudio.cubecraft.registery.Registry;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.util.container.namespace.NameSpaceItemGetter;

public class BlockRendererRegistryTree {
    @NameSpaceItemGetter(id = "oak_leaves", namespace = "cubecraft")
    public IBlockRenderer oak_leaves() {
        return new LeaveRenderer("/resource/textures/block/oak_leaves.png");
    }

    @NameSpaceItemGetter(id = "birch_leaves", namespace = "cubecraft")
    public IBlockRenderer birch_leaves() {
        return new LeaveRenderer("/resource/textures/block/birch_leaves.png");
    }

    @NameSpaceItemGetter(id = "dark_oak_leaves", namespace = "cubecraft")
    public IBlockRenderer dark_oak_leaves() {
        return new LeaveRenderer("/resource/textures/block/dark_oak_leaves.png");
    }

    @NameSpaceItemGetter(id = "spruce_leaves", namespace = "cubecraft")
    public IBlockRenderer spruce_leaves() {
        return new LeaveRenderer("/resource/textures/block/spruce_leaves.png");
    }

    @NameSpaceItemGetter(id = "acacia_leaves", namespace = "cubecraft")
    public IBlockRenderer acacia_leaves() {
        return new LeaveRenderer("/resource/textures/block/acacia_leaves.png");
    }

    public class LeaveRenderer extends LegacyBlockRenderer{
        String tex;

        public LeaveRenderer(String tex){
            super(RenderType.ALPHA);
            this.tex=tex;
        }

        @Override
        public String getTexture(IWorld world, BlockState bs, long x, long y, long z, int face) {
            return tex;
        }

        @Override
        public int getFaceColor(IWorld world, BlockState bs, long x, long y, long z, int face) {
            return Registry.getBiomeMap().get(bs.getBiome()).getLeavesColor();
        }
    }





    public class LogRenderer extends LegacyBlockRenderer{
        String tex;
        String tex2;

        public LogRenderer(String tex,String tex2){
            super(RenderType.ALPHA);
            this.tex=tex;
            this.tex2=tex2;
        }

        @Override
        public String getTexture(IWorld world, BlockState bs, long x, long y, long z, int face) {
            return tex;
        }

        @Override
        public int getFaceColor(IWorld world, BlockState bs, long x, long y, long z, int face) {
            return Registry.getBiomeMap().get(bs.getBiome()).getLeavesColor();
        }
    }
}

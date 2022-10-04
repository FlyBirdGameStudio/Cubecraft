package com.flybirdstudio.cubecraft.registery.block;

import com.flybirdstudio.cubecraft.world.block.material.Block;
import com.flybirdstudio.cubecraft.world.block.material.OverwrittenBlock;
import com.flybirdstudio.util.container.namespace.GetterDepend;
import com.flybirdstudio.util.container.namespace.NameSpaceItemGetter;

public class BlockRegistryTree {
    @GetterDepend(id="block_leaves",namespace = "cubecraft")
    @NameSpaceItemGetter(id = "oak_leaves",namespace="cubecraft")
    public Block oak_leaves(Block behavior){
        return new OverwrittenBlock(behavior);
    }

    @GetterDepend(id="block_leaves",namespace = "cubecraft")
    @NameSpaceItemGetter(id = "birch_leaves",namespace="cubecraft")
    public Block birch_leaves(Block behavior){
        return new OverwrittenBlock(behavior);
    }

    @GetterDepend(id="block_leaves",namespace = "cubecraft")
    @NameSpaceItemGetter(id = "dark_oak_leaves",namespace="cubecraft")
    public Block dark_oak_leaves(Block behavior){
        return new OverwrittenBlock(behavior);
    }

    @GetterDepend(id="block_leaves",namespace = "cubecraft")
    @NameSpaceItemGetter(id = "spruce_leaves",namespace="cubecraft")
    public Block spruce_leaves(Block behavior){
        return new OverwrittenBlock(behavior);
    }

    @GetterDepend(id="block_leaves",namespace = "cubecraft")
    @NameSpaceItemGetter(id = "acacia_leaves",namespace="cubecraft")
    public Block acacia_leaves(Block behavior){
        return new OverwrittenBlock(behavior);
    }
}

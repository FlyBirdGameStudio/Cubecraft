package com.flybirdstudio.cubecraftcontent.block

import com.flybirdstudio.cubecraft.world.block.material.Block
import com.flybirdstudio.cubecraft.world.block.material.OverwrittenBlock
import com.flybirdstudio.util.container.namespace.GetterDepend
import com.flybirdstudio.util.container.namespace.NameSpaceItemGetter

class BlockRegistryTree {
    @GetterDepend(id = "block_leaves", namespace = "cubecraft")
    @NameSpaceItemGetter(id = "oak_leaves", namespace = "cubecraft")
    fun oak_leaves(behavior: Block?): Block {
        return OverwrittenBlock(behavior)
    }

    @GetterDepend(id = "block_leaves", namespace = "cubecraft")
    @NameSpaceItemGetter(id = "birch_leaves", namespace = "cubecraft")
    fun birch_leaves(behavior: Block?): Block {
        return OverwrittenBlock(behavior)
    }

    @GetterDepend(id = "block_leaves", namespace = "cubecraft")
    @NameSpaceItemGetter(id = "dark_oak_leaves", namespace = "cubecraft")
    fun dark_oak_leaves(behavior: Block?): Block {
        return OverwrittenBlock(behavior)
    }

    @GetterDepend(id = "block_leaves", namespace = "cubecraft")
    @NameSpaceItemGetter(id = "spruce_leaves", namespace = "cubecraft")
    fun spruce_leaves(behavior: Block?): Block {
        return OverwrittenBlock(behavior)
    }

    @GetterDepend(id = "block_leaves", namespace = "cubecraft")
    @NameSpaceItemGetter(id = "acacia_leaves", namespace = "cubecraft")
    fun acacia_leaves(behavior: Block?): Block {
        return OverwrittenBlock(behavior)
    }
}
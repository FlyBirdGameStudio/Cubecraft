package com.flybirdstudio.cubecraftcontent.block

import com.flybirdstudio.cubecraft.world.block.material.Block
import com.flybirdstudio.cubecraft.world.block.material.OverwrittenBlock
import com.flybirdstudio.util.container.namespace.GetterDepend
import com.flybirdstudio.util.container.namespace.NameSpaceItemGetter

class BlockRegistery {
    //environment
    @GetterDepend(id = "air", namespace = "cubecraft")
    @NameSpaceItemGetter(id = "air", namespace = "cubecraft")
    fun air(behavior: Block?): Block {
        return OverwrittenBlock(behavior)
    }

    //environment/stone
    @GetterDepend(id = "stone", namespace = "cubecraft")
    @NameSpaceItemGetter(id = "stone", namespace = "cubecraft")
    fun stone(behavior: Block?): Block {
        return OverwrittenBlock(behavior)
    }

    //environment/surface
    @GetterDepend(id = "dirt", namespace = "cubecraft")
    @NameSpaceItemGetter(id = "dirt", namespace = "cubecraft")
    fun dirt(behavior: Block?): Block {
        return OverwrittenBlock(behavior)
    }

    @GetterDepend(id = "block", namespace = "cubecraft")
    @NameSpaceItemGetter(id = "grass_block", namespace = "cubecraft")
    fun grassBlock(behavior: Block?): Block {
        return OverwrittenBlock(behavior)
    }
}
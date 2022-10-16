package com.flybirdstudio.cubecraftcontent.block

import com.flybirdstudio.cubecraft.Registry
import com.flybirdstudio.cubecraft.client.render.model.RenderType
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.IBlockRenderer
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.LegacyBlockRenderer
import com.flybirdstudio.cubecraft.world.IWorld
import com.flybirdstudio.cubecraft.world.block.BlockState
import com.flybirdstudio.util.container.namespace.NameSpaceItemGetter

class BlockRendererRegistryTree {
    @NameSpaceItemGetter(id = "oak_leaves", namespace = "cubecraft")
    fun oak_leaves(): IBlockRenderer {
        return LeaveRenderer("/resource/textures/block/oak_leaves.png")
    }

    @NameSpaceItemGetter(id = "birch_leaves", namespace = "cubecraft")
    fun birch_leaves(): IBlockRenderer {
        return LeaveRenderer("/resource/textures/block/birch_leaves.png")
    }

    @NameSpaceItemGetter(id = "dark_oak_leaves", namespace = "cubecraft")
    fun dark_oak_leaves(): IBlockRenderer {
        return LeaveRenderer("/resource/textures/block/dark_oak_leaves.png")
    }

    @NameSpaceItemGetter(id = "spruce_leaves", namespace = "cubecraft")
    fun spruce_leaves(): IBlockRenderer {
        return LeaveRenderer("/resource/textures/block/spruce_leaves.png")
    }

    @NameSpaceItemGetter(id = "acacia_leaves", namespace = "cubecraft")
    fun acacia_leaves(): IBlockRenderer {
        return LeaveRenderer("/resource/textures/block/acacia_leaves.png")
    }

    inner class LeaveRenderer(var tex: String) : LegacyBlockRenderer(RenderType.ALPHA) {
        override fun getTexture(world: IWorld, bs: BlockState, x: Long, y: Long, z: Long, face: Int): String {
            return tex
        }

        override fun getFaceColor(world: IWorld, bs: BlockState, x: Long, y: Long, z: Long, face: Int): Int {
            return Registry.getBiomeMap()[bs.biome].leavesColor
        }
    }

    inner class LogRenderer(var tex: String, var tex2: String) : LegacyBlockRenderer(RenderType.ALPHA) {
        override fun getTexture(world: IWorld, bs: BlockState, x: Long, y: Long, z: Long, face: Int): String {
            return tex
        }

        override fun getFaceColor(world: IWorld, bs: BlockState, x: Long, y: Long, z: Long, face: Int): Int {
            return Registry.getBiomeMap()[bs.biome].leavesColor
        }
    }
}
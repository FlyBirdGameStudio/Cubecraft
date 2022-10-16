package com.flybirdstudio.cubecraftcontent.renderer

import com.flybirdstudio.cubecraft.Registry
import com.flybirdstudio.cubecraft.client.render.model.RenderType
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.IBlockRenderer
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.LegacyBlockRenderer
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.LegacyBlockRendererImpl
import com.flybirdstudio.cubecraft.world.IWorld
import com.flybirdstudio.cubecraft.world.block.BlockState
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder
import com.flybirdstudio.util.container.namespace.NameSpaceItemGetter

class BlockRendererRegistry {
    @NameSpaceItemGetter(id = "grass_block", namespace = "cubecraft")
    fun grass_block(): IBlockRenderer {
        return object : LegacyBlockRenderer(RenderType.ALPHA) {
            override fun getTexture(world: IWorld, bs: BlockState, x: Long, y: Long, z: Long, face: Int): String {
                return if (face == 0) {
                    "/resource/textures/block/grass_block_top.png"
                } else if (face == 1) {
                    "/resource/textures/block/dirt.png"
                } else if (face > 1 && face < 6) {
                    "/resource/textures/block/grass_block_side.png"
                } else {
                    "/resource/textures/block/grass_block_side_overlay.png"
                }
            }

            override fun getFaceColor(world: IWorld, bs: BlockState, x: Long, y: Long, z: Long, face: Int): Int {
                return if (face > 1 && face < 6) {
                    0xFFFFFF
                } else if (face == 1) {
                    0xFFFFFF
                } else {
                    Registry.getBiomeMap()[bs.biome].grassColor
                }
            }

            override fun render(bs: BlockState, world: IWorld, renderType: RenderType, renderX: Double, renderY: Double, renderZ: Double, x: Long, y: Long, z: Long, builder: VertexArrayBuilder) {
                this.render(world, bs, x, y, z, renderX, renderY, renderZ, bs.facing, builder)
                val facing = bs.facing
                val c = getFaceColor(world, bs, x, y, z, 0)
                val tex = "/resource/textures/block/grass_block_side_overlay.png"
                if (shouldRender(2, facing, world, x, y, z)) {
                    renderFaceWithColor(tex, c, 2, builder, world, bs, x, y, z, renderX, renderY, renderZ - 0.001)
                }
                if (shouldRender(3, facing, world, x, y, z)) {
                    renderFaceWithColor(tex, c, 3, builder, world, bs, x, y, z, renderX, renderY, renderZ + 0.001)
                }
                if (shouldRender(4, facing, world, x, y, z)) {
                    renderFaceWithColor(tex, c, 4, builder, world, bs, x, y, z, renderX, renderY, renderZ)
                }
                if (shouldRender(5, facing, world, x, y, z)) {
                    renderFaceWithColor(tex, c, 5, builder, world, bs, x, y, z, renderX + 0.001, renderY, renderZ)
                }
            }
        }
    }

    @NameSpaceItemGetter(id = "stone", namespace = "cubecraft")
    fun stone(): IBlockRenderer {
        return LegacyBlockRendererImpl("/resource/textures/block/stone.png")
    }

    @NameSpaceItemGetter(id = "dirt", namespace = "cubecraft")
    fun dirt(): IBlockRenderer {
        return LegacyBlockRendererImpl("/resource/textures/block/dirt.png")
    }

    @NameSpaceItemGetter(id = "air", namespace = "cubecraft")
    fun air(): IBlockRenderer? {
        return null
    }
}
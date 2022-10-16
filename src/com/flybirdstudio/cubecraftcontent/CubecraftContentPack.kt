package com.flybirdstudio.cubecraftcontent

import com.flybirdstudio.cubecraft.Registry
import com.flybirdstudio.cubecraft.extansion.*
import com.flybirdstudio.cubecraftcontent.block.BlockBehaviorRegistery
import com.flybirdstudio.cubecraftcontent.block.BlockRegistery
import com.flybirdstudio.cubecraftcontent.block.BlockRegistryTree
import com.flybirdstudio.cubecraftcontent.block.BlockRendererRegistryTree
import com.flybirdstudio.cubecraftcontent.renderer.BlockRendererRegistry
import com.flybirdstudio.cubecraftcontent.renderer.EntityRendererRegistery

@CubecraftMod(name = "cubecraft content pack", version = "0.1.0")
class CubecraftContentPack(client: PlatformClient?, server: PlatformServer?, target: ExtansionRunningTarget?) : Mod(client, server, target) {
    override fun construct() {


        //block
        Registry.getBlockBehaviorMap().registerGetter(BlockBehaviorRegistery::class.java)
        Registry.getBlockMap().registerGetter(BlockRegistery::class.java)
        Registry.getBlockRendererMap().registerGetter(BlockRendererRegistry::class.java)

        Registry.getBlockMap().registerGetter(BlockRegistryTree::class.java)
        Registry.getBlockRendererMap().registerGetter(BlockRendererRegistryTree::class.java)

        //entity

        //entity
        Registry.getBiomeMap().registerGetter(BiomesRegistry::class.java)
        Registry.getEntityRendererMap().registerGetter(EntityRendererRegistery::class.java)
    }
}
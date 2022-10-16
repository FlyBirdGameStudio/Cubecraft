package com.flybirdstudio.cubecraftcontent

import com.flybirdstudio.cubecraft.world.biome.Biome
import com.flybirdstudio.cubecraft.world.chunk.Chunk
import com.flybirdstudio.cubecraft.world.worldGen.noiseGenerator.PerlinNoise
import com.flybirdstudio.util.container.namespace.NameSpaceItemGetter
import com.flybirdstudio.util.math.MathHelper
import java.util.*

class BiomesRegistry {
    @NameSpaceItemGetter(id = "plains", namespace = "cubecraft")
    fun plains(): Biome {
        return object : Biome(0.3, 0.5, 0.4, 0.0, 0.03, "cubecraft:plains", "cubecraft:stone", 0x91bd59, 0x77ab2f) {
            override fun buildSurface(primer: Chunk, x: Int, z: Int, height: Double, seed: Long) {
                val p = primer.key
                val noise = PerlinNoise(Random(seed), 3)
                val relativeHeight = MathHelper.getRelativePosInChunk(height.toLong(), 16).toInt()
                val levels = noise.getValue(p.toWorldPosX(x).toDouble(), p.toWorldPosZ(z).toDouble()).toInt() / 2 + 5
                for (y in 0..15) {
                    val h = p.toWorldPosY(y)
                    if (h == height.toLong()) {
                        primer.getBlockState(x, relativeHeight, z).id = "cubecraft:grass_block"
                    }
                    if (h < height.toLong() && h > height.toLong() - levels) {
                        primer.getBlockState(x, y, z).id = "cubecraft:dirt"
                    }
                }
            }
        }
    }
}
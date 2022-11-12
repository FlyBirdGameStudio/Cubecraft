package io.flybird.cubecraft.internal.worldGen;

import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.worldGen.pipeline.ChunkGenerator;

public class WorldGeneratorFlat extends ChunkGenerator {
    @Override
    protected void init() {

    }

    @Override
    public void generate(Chunk chunk) {
        for (int x=0;x<16;x++){
            for (int z=0;z<16;z++){
                for (int y=0;y<16;y++){
                    if(chunk.getKey().toWorldPosY(y)<=1024) {
                        BlockState bs = chunk.getBlockState(x, y, z);
                    }
                }
                Registry.getBiomeMap().get("cubecraft:plains").buildSurface(chunk,x,z,1024,setting.seed());
            }
        }
    }
}

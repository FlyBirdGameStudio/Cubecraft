package com.sunrisestudio.cubecraft.world.worldGen.pipeline;

import com.sunrisestudio.cubecraft.world.chunk.Chunk;
import com.sunrisestudio.cubecraft.world.worldGen.WorldGeneratorSetting;
import com.sunrisestudio.util.LogHandler;
import com.sunrisestudio.util.container.SettableFinal;

import java.util.ArrayList;

public class ChunkGeneratorPipeline {
    private SettableFinal<String> id;
    public void setBindingWorldID(String bindingWorldID) {
        this.id.setValue(bindingWorldID);
    }

    private LogHandler handler=LogHandler.create("worldGenerator-"+this.id.getValue(),"server");
    private ArrayList<IChunkGenerator> pipeline;

    public void addLast(IChunkGenerator generator){
        this.pipeline.add(generator);
    }

    public void generate(Chunk primer, WorldGeneratorSetting worldGeneratorSetting){
        for (IChunkGenerator chunkGenerator:this.pipeline){
            try {
                chunkGenerator.generate(primer, worldGeneratorSetting);
            }catch (Exception e){
                this.handler.exception(e);
            }
        }
    }
}

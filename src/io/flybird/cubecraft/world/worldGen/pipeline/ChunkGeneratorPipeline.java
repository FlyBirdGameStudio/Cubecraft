package io.flybird.cubecraft.world.worldGen.pipeline;

import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.worldGen.GenerateStage;
import io.flybird.cubecraft.world.worldGen.WorldGenListener;
import io.flybird.cubecraft.world.worldGen.WorldGeneratorSetting;
import io.flybird.util.LogHandler;
import io.flybird.util.container.SettableFinal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ChunkGeneratorPipeline {
    private final LogHandler logger;
    private final ArrayList<IChunkGenerator> handlers =new ArrayList<>();

    public final String id;

    public ChunkGeneratorPipeline(String id) {
        this.id=id;
        this.logger = LogHandler.create("world-generator-"+this.id,"game");
    }

    public ChunkGeneratorPipeline add(IChunkGenerator generator){
        this.handlers.add(generator);
        return this;
    }

    public void generate(Chunk primer, WorldGeneratorSetting worldGeneratorSetting){
        for (GenerateStage stage:GenerateStage.byOrder()){
            this.generate(stage,primer,worldGeneratorSetting);
        }
    }

    public void generate(GenerateStage stage,Chunk chunk,WorldGeneratorSetting setting){
        for (IChunkGenerator handler:this.handlers){
            Method[] ms = handler.getClass().getMethods();
            for (Method m : ms) {
                if (Arrays.stream(m.getAnnotations()).anyMatch(annotation -> annotation instanceof WorldGenListener)) {
                    WorldGenListener a=m.getAnnotation(WorldGenListener.class);
                    if (a.stage() == stage&& Objects.equals(a.world(), chunk.getWorld().getID())) {
                        try {
                            m.invoke(handler,chunk,setting);
                        } catch (IllegalAccessException | InvocationTargetException e2) {
                            logger.exception(e2);
                        }
                    }
                }
            }
        }
    }
}

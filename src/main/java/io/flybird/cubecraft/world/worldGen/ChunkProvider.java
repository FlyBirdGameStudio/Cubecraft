package io.flybird.cubecraft.world.worldGen;

import io.flybird.cubecraft.client.ClientMain;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.chunk.ChunkPos;
import io.flybird.cubecraft.world.worldGen.noiseGenerator.PerlinNoise;
import io.flybird.cubecraft.world.worldGen.noiseGenerator.Synth;
import io.flybird.cubecraft.world.worldGen.pipeline.ChunkGeneratorPipeline;
import io.flybird.cubecraft.world.worldGen.templete.Scale;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

public class ChunkProvider implements IChunkProvider{
    public ChunkGeneratorPipeline pipeline;

    public static final int REGION_GRID_SIZE = 64;
    public static HashMap<String, ChunkProvider> providers = new HashMap<>();

    public IWorld world;

    Synth test;

    public ChunkProvider(IWorld world) {
        this.world = world;
        this.test= new Scale(new PerlinNoise(new Random(world.getSeed()), 3),1,1);
        this.pipeline= Registries.WORLD_GENERATOR.get(this.world.getID());
    }

    public static ChunkProvider getProvider(IWorld world) {
        return providers.getOrDefault(world.getID(), new ChunkProvider(world));
    }

    /**
     * this method will try to load chunk on disk first. If result null,it will try to generate a new chunk.
     *
     * @param pos position
     * @return a brand-new chunk
     */
    @Override
    public Chunk loadChunk(ChunkPos pos) {
        long regionX = pos.x() / REGION_GRID_SIZE;
        long regionY = pos.y() / REGION_GRID_SIZE;
        long regionZ = pos.z() / REGION_GRID_SIZE;
        Chunk c=new Chunk(ChunkProvider.this.world, pos);

        String region = ClientMain.getGamePath() + "/saves/" + "/" + world.getID() +
                "/" + regionX + "_" + regionY + "_" + regionZ + ".region";
        File regionFile = new File(region);

        this.pipeline.generate(c,new WorldGeneratorSetting(114514,new HashMap<>()));
        return c;
    }

}

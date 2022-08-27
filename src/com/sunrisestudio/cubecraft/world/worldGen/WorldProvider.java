package com.sunrisestudio.cubecraft.world.worldGen;

import com.sunrisestudio.cubecraft.Start;
import com.sunrisestudio.cubecraft.registery.Registry;
import com.sunrisestudio.cubecraft.world.World;
import com.sunrisestudio.cubecraft.world.block.BlockFacing;
import com.sunrisestudio.cubecraft.world.chunk.Chunk;
import com.sunrisestudio.cubecraft.world.chunk.ChunkPos;
import com.sunrisestudio.cubecraft.world.worldGen.noiseGenerator.PerlinNoise;
import com.sunrisestudio.cubecraft.world.worldGen.noiseGenerator.Synth;
import com.sunrisestudio.cubecraft.world.worldGen.pipeline.ChunkGeneratorPipeline;
import com.sunrisestudio.cubecraft.world.worldGen.pipeline.IChunkGenerator;
import com.sunrisestudio.cubecraft.world.worldGen.pipeline.object.ChunkGeneratorOverWorld;
import com.sunrisestudio.cubecraft.world.worldGen.templete.Scale;
import org.joml.SimplexNoise;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

public class WorldProvider {
    public ChunkGeneratorPipeline pipeline;

    public static final int REGION_GRID_SIZE = 64;
    public static HashMap<String, WorldProvider> providers = new HashMap<>();

    public World world;

    Synth test;

    public WorldProvider(World world) {
        this.world = world;
        this.test= new Scale(new PerlinNoise(new Random(world.getSeed()), 3),1,1);
        this.pipeline= Registry.getWorldGeneratorMap().get(this.world.getID());
    }

    public static WorldProvider getProvider(World world) {
        return providers.getOrDefault(world.getID(), new WorldProvider(world));
    }

    /**
     * this method will try to load chunk on disk first. If result null,it will try to generate a new chunk.
     *
     * @param pos position
     * @return a brand-new chunk
     */
    public Chunk loadChunk(ChunkPos pos) {
        long regionX = pos.x() / REGION_GRID_SIZE;
        long regionY = pos.y() / REGION_GRID_SIZE;
        long regionZ = pos.z() / REGION_GRID_SIZE;
        Chunk c=new Chunk(WorldProvider.this.world, pos);

        //for example:region(3,3,4)is"gamepath/saves/world/dim-0/3_3_4.region"
        String region = Start.getGamePath() + "/saves/" + "/" + world.getID() +
                "/" + regionX + "_" + regionY + "_" + regionZ + ".region";
        File regionFile = new File(region);
        this.pipeline.generate(c,new WorldGeneratorSetting(114514,new HashMap<>()));
        return c;
    }

}

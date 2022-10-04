package com.flybirdstudio.cubecraft.world.worldGen;

import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.EnumFacing;
import com.flybirdstudio.cubecraft.world.chunk.Chunk;
import com.flybirdstudio.cubecraft.world.worldGen.noise.NoiseGeneratorCombined;
import com.flybirdstudio.cubecraft.world.worldGen.noise.NoiseGeneratorOctaves;
import com.flybirdstudio.util.container.options.Option;
import com.flybirdstudio.util.math.MathHelper;

import java.util.Random;

public class ChunkProvider0223 extends ChunkProvider {

    private long width;
    private long depth;
    private long height;
    //private byte[] blocksByteArray;
    private long waterLevel;
    private long groundLevel;
    public boolean islandGen;
    public boolean floatingGen;
    public boolean flatGen;
    public long levelType;
    //private long[] blocksIntArray;
    private Random rand = new Random("notch sb!".charAt(0));


    private IWorld world;
    public ChunkProvider0223(IWorld world) {
        this.world=world;
    }


    public final void generate(Chunk primer) {

        try {
            int i = 1;
            this.width = 16;
            this.depth = 16;
            this.height = 16;
            for (long j = 0; j < i; ++j) {
                this.waterLevel = height - 32 - j * 48;
                this.groundLevel = this.waterLevel - 2;
                double[][] heightMap = new double[16][16];
                if (this.flatGen) {

                } else {
                    final NoiseGeneratorCombined noiseGeneratorCombined = new NoiseGeneratorCombined(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                    final NoiseGeneratorCombined noiseGeneratorCombined2 = new NoiseGeneratorCombined(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                    final NoiseGeneratorOctaves noiseGeneratorOctaves = new NoiseGeneratorOctaves(this.rand, 6);
                    final NoiseGeneratorOctaves noiseGeneratorOctaves2 = new NoiseGeneratorOctaves(this.rand, 2);
                    heightMap = new double[16][16];
                    for (int x = 0; x < 16; ++x) {
                        final double abs = Math.abs((x / (this.width - 1.0) - 0.5) * 2.0);
                        for (int z = 0; z < 16; ++z) {
                            final double abs2 = Math.abs((z / (this.depth - 1.0) - 0.5) * 2.0);
                            final double n2 = noiseGeneratorCombined.NoiseGenerator(x * 1.3f+primer.getKey().x()*16, z * 1.3f+primer.getKey().z()*16) / 6.0 - 4.0;
                            double n3 = noiseGeneratorCombined2.NoiseGenerator(x * 1.3f+primer.getKey().x()*16, z * 1.3f+primer.getKey().z()*16) / 5.0 + 10.0 - 4.0;
                            final double n4;
                            if ((n4 = noiseGeneratorOctaves.NoiseGenerator(x+primer.getKey().x()*16, z+primer.getKey().z()*16) / 8.0) > 0.0) {
                                n3 = n2;
                            }
                            double n5 = Math.max(n2, n3) / 2.0;
                            if (this.islandGen) {
                                double n6 = Math.sqrt(abs * abs + abs2 * abs2) * 1.2000000476837158;
                                if ((n6 = Math.max(n6 = Math.min(n6,
                                                noiseGeneratorOctaves2.NoiseGenerator(x * 0.05f+primer.getKey().x()*16, z * 0.05f+primer.getKey().z()*16) / 4.0 + 1.0),
                                        Math.max(abs, abs2))) > 1.0) {
                                    n6 = 1.0;
                                }
                                if (n6 < 0.0) {
                                    n6 = 0.0;
                                }
                                n6 *= n6;
                                if ((n5 = n5 * (1.0 - n6) - n6 * 10.0 + 5.0) < 0.0) {
                                    n5 -= n5 * n5 * 0.20000000298023224;
                                }
                            } else if (n5 < 0.0) {
                                n5 *= 0.8;
                            }
                            heightMap[x][z] = n5;
                        }
                    }
                    double[][] map2 = heightMap;
                    final NoiseGeneratorCombined noiseGeneratorCombined3 = new NoiseGeneratorCombined(
                            new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                    final NoiseGeneratorCombined noiseGeneratorCombined4 = new NoiseGeneratorCombined(
                            new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                    for (int x = 0; x < 16; ++x) {
                        for (int z = 0; z < 16; ++z) {
                            final double n8 = noiseGeneratorCombined3.NoiseGenerator(x << 1+primer.getKey().x()*16, z << 1+primer.getKey().z()*16) / 8.0;
                            final long n9 = (noiseGeneratorCombined4.NoiseGenerator(x << 1+primer.getKey().x()*16, z << 1+primer.getKey().z()*16) > 0.0) ? 1
                                    : 0;
                            if (n8 > 2.0) {
                                map2[x][z] = ((long) (map2[x][z]) - n9) / 2 << 1 + n9;
                            }
                        }
                    }
                }

                double[][] map3 = heightMap;
                final NoiseGeneratorOctaves noiseGeneratorOctaves3 = new NoiseGeneratorOctaves(this.rand, 8);
                final NoiseGeneratorOctaves noiseGeneratorOctaves4 = new NoiseGeneratorOctaves(this.rand, 8);
                for (int x = 0; x < 16; ++x) {
                    final double abs3 = Math.abs((x / (16 - 1.0) - 0.5) * 2.0);
                    for (int z = 0; z < 16; ++z) {
                        double max = (max = Math.max(abs3, Math.abs((z / (16 - 1.0) - 0.5) * 2.0))) * max * max;
                        final long map3_sample2;
                        final long map3_sample = (map3_sample2 = (long) map3[x][z])
                                + ((long) (noiseGeneratorOctaves3.NoiseGenerator(x, z) / 24.0) - 4);
                        map3[x][z] = Math.max(map3[x][z], map3[x][z] + ((long) (noiseGeneratorOctaves3.NoiseGenerator(x+primer.getKey().x()*16, z+primer.getKey().z()*16) / 24.0) - 4));
                        map3[x][z] = MathHelper.clamp(map3[x][z], 14, -100000);
                        if (map3[x][z] <= 0) {
                            map3[x][z] = 1;
                        }
                        final double n4;
                        long height3;
                        if ((height3 = (long) ((height3 = (long) (Math
                                .sqrt(Math.abs(n4 = noiseGeneratorOctaves4.NoiseGenerator(x * 2.3+primer.getKey().x()*16, z * 2.3+primer.getKey().z()*16) / 24.0))
                                * Math.signum(n4) * 20.0) + this.waterLevel) * (1.0 - max)
                                + max * this.height)) > this.waterLevel) {
                            height3 = this.height;
                        }
                        for (long y = 0; y < 16; ++y) {
                            String blockID = "cubecraft:air";
                            if (y+primer.getKey().y()*16 <= map3_sample2) {
                                blockID = "cubecraft:dirt";
                            }
                            if (y+primer.getKey().y()*16 <= map3_sample) {
                                blockID = "cubecraft:stone";
                            }
                            if (this.floatingGen && y+primer.getKey().y()*16 < height3) {
                                blockID = "cubecraft:air";
                            }
                            primer.setBlock(x, (int) y, z, blockID, EnumFacing.Up);
                        }
                    }
                }


                final NoiseGeneratorOctaves noiseGeneratorOctaves5 = new NoiseGeneratorOctaves(this.rand, 8);
                final NoiseGeneratorOctaves noiseGeneratorOctaves6 = new NoiseGeneratorOctaves(this.rand, 8);
                long n20 = this.waterLevel - 1;
                if (this.levelType == 2) {
                    n20 += 2;
                }
                for (int x = 0; x < 16; ++x) {
                    for (int z = 0; z < 16; ++z) {
                        boolean b = noiseGeneratorOctaves5.NoiseGenerator(x+primer.getKey().x()*16, z+primer.getKey().z()*16) > 8.0;
                        if (this.islandGen) {
                            b = (noiseGeneratorOctaves5.NoiseGenerator(x+primer.getKey().x()*16, z+primer.getKey().z()*16) > -8.0);
                        }
                        if (this.levelType == 2) {
                            b = (noiseGeneratorOctaves5.NoiseGenerator(x+primer.getKey().x()*16, z+primer.getKey().z()*16) > -32.0);
                        }
                        final boolean b2 = noiseGeneratorOctaves6.NoiseGenerator(x+primer.getKey().x()*16, z+primer.getKey().z()*16) > 12.0;
                        if (this.levelType == 1 || this.levelType == 3) {
                            b = (noiseGeneratorOctaves5.NoiseGenerator(x+primer.getKey().x()*16, z+primer.getKey().z()*16) > -8.0);
                        }
                        final long n;
                        final long n22 = ((n = (long) map3[x][z]) * this.depth + z) * this.width + x;
                        final long n23;

                        /*
                        if (((n23 = (this.blocksByteArray[((n + 1) * this.depth + z) * this.width + x]
                                & 0xFF)) == Block.waterMoving.blockID || n23 == Block.waterStill.blockID || n23 == 0)
                                && n <= this.waterLevel - 1 && b2) {
                            primer.setBlock(x,n11,z,"cubecraft:stone", BlockFacing.Up);
                        }
                         */


                        if (primer.getBlockState(x, ((int) heightMap[x][z]), z).getId() == "cubecraft:air") {
                            String id = "cubecraft:air";
                            if (n <= n20 && b) {
                                id = "cubecraft:sand";
                                if (this.levelType == 1) {
                                    id = "cubecraft:grass_block";
                                }
                            }
                            if (primer.getBlockState(x, (int) heightMap[x][z], z).getId() != "cubecraft:air") {
                                primer.setBlock(x, (int) heightMap[x][z], z, id, EnumFacing.Up);
                            }
                        }
                    }
                }
            }
            long n12 = 16;
            long n7 = 16;
            long height2 = 16;
            long n20 = n12 * n7 * height2 / 256 / 64 << 1;
            for (long n13 = 0; n13 < n20; ++n13) {
                float n25 = this.rand.nextFloat() * n12;
                float n26 = this.rand.nextFloat() * height2;
                float n27 = this.rand.nextFloat() * n7;
                final long n = (long) ((this.rand.nextFloat() + this.rand.nextFloat()) * 200.0f);
                float n28 = this.rand.nextFloat() * 3.1415927f * 2.0f;
                float n29 = 0.0f;
                float float1 = this.rand.nextFloat() * 3.1415927f * 2.0f;
                float n30 = 0.0f;
                final float n31 = this.rand.nextFloat() * this.rand.nextFloat();
                for (long n15 = 0; n15 < n; ++n15) {
                    n25 += Math.sin(n28) * Math.cos(float1);
                    n27 += Math.cos(n28) * Math.cos(float1);
                    n26 += Math.sin(float1);
                    n28 += n29 * 0.2f;
                    n29 = (n29 *= 0.9f) + (this.rand.nextFloat() - this.rand.nextFloat());
                    float1 = (float1 += n30 * 0.5f) * 0.5f;
                    n30 = (n30 *= 0.75f) + (this.rand.nextFloat() - this.rand.nextFloat());
                    if (this.rand.nextFloat() >= 0.25f) {
                        final float n32 = n25 + (this.rand.nextFloat() * 4.0f - 2.0f) * 0.2f;
                        final float n33 = n26 + (this.rand.nextFloat() * 4.0f - 2.0f) * 0.2f;
                        final float n34 = n27 + (this.rand.nextFloat() * 4.0f - 2.0f) * 0.2f;
                        long j;
                        float n35;
                        long x;
                        long z;
                        float n38;
                        float n39;
                        float n40;
                        for (n35 = (float) (Math.sin(n15 * 3.1415927f / n)
                                * (1.2f + ((this.height - n33) / this.height * 3.5f + 1.0f) * n31)), i = (int) (n32
                                                                - n35); i <= (long) (n32 + n35); ++i) {
                            for (x = (long) (n33 - n35); x <= (long) (n33 + n35); ++x) {
                                for (z = (long) (n34 - n35); z <= (long) (n34 + n35); ++z) {
                                    n38 = i - n32;
                                    n39 = x - n33;
                                    n40 = z - n34;
                                    if ((n38 = n38 * n38 + n39 * n39 * 2.0f + n40 * n40) < n35 * n35 && i > 0 && x > 0
                                            && z > 0 && i < this.width - 1 && x < this.height - 1
                                            && z < this.depth - 1) {
                                        j = (x * this.depth + z) * this.width + i;
                                        /*
                                        if (this.blocksByteArray[j] == Block.stone.blockID) {
                                            this.blocksByteArray[j] = 0;
                                        }

                                         */
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // long j = this.populateOre(Block.oreCoal.blockID, 1000, 10, (height << 2) / 5);
            // final long populateOre = this.populateOre(Block.oreIron.blockID, 800, 8, height * 3 / 5);
            // long k = this.populateOre(Block.oreGold.blockID, 500, 6, (height << 1) / 5);
            //  i = this.populateOre(Block.oreDiamond.blockID, 800, 2, height / 5);
            //  System.out.prlongln(new StringBuilder().append("Coal: ").append(j).append(", Iron: ").append(populateOre)
            //          .append(", Gold: ").append(k).append(", Diamond: ").append(i).toString());
            this.lavaGen();

                /*
                world.cloudHeight = height + 2;
                if (this.floatingGen) {
                    this.groundLevel = -128;
                    this.waterLevel = this.groundLevel + 1;
                    world.cloudHeight = -16;
                } else if (this.islandGen || this.originalGen) {
                    this.groundLevel = this.waterLevel - 9;
                } else {
                    this.groundLevel = this.waterLevel + 1;
                    this.waterLevel = this.groundLevel - 16;
                }

                 */
            this.liquidThemeSpawner(primer);
            if (!this.floatingGen) {
                String id = "cubecraft:water";
                if (this.levelType == 1) {
                    id = "cubecraft:lava";
                }
                for (long j1 = 0; j1 < width; ++j1) {
                    this.flood(j1, this.waterLevel - 1, 0, 0, id);
                    this.flood(j1, this.waterLevel - 1, depth - 1, 0, id);
                }
                for (long j2 = 0; j2 < depth; ++j2) {
                    this.flood(width - 1, this.waterLevel - 1, j2, 0, id);
                    this.flood(0, this.waterLevel - 1, j2, 0, id);
                }
            }

            //set world themes
                /*
                if (this.levelType == 0) {
                    world.skyColor = 10079487;
                    world.fogColor = 16777215;
                    world.cloudColor = 16777215;
                }
                if (this.levelType == 1) {
                    world.cloudColor = 2164736;
                    world.fogColor = 1049600;
                    world.skyColor = 1049600;
                    final World world2 = world;
                    final World world3 = world;
                    final long n41 = 7;
                    world3.skyBrightness = n41;
                    world2.skylightSubtracted = n41;
                    world.defaultFluid = Block.lavaMoving.blockID;
                    if (this.floatingGen) {
                        world.cloudHeight = height + 2;
                        this.waterLevel = -16;
                    }
                }
                if (this.levelType == 2) {
                    world.skyColor = 13033215;
                    world.fogColor = 13033215;
                    world.cloudColor = 15658751;
                    final World world4 = world;
                    final World world5 = world;
                    final long n42 = 15;
                    world5.skyBrightness = n42;
                    world4.skylightSubtracted = n42;
                    world.skyBrightness = 16;
                    world.cloudHeight = height + 64;
                }
                if (this.levelType == 3) {
                    world.skyColor = 7699847;
                    world.fogColor = 5069403;
                    world.cloudColor = 5069403;
                    final World world6 = world;
                    final World world7 = world;
                    final long n43 = 12;
                    world7.skyBrightness = n43;
                    world6.skylightSubtracted = n43;
                }

                 */
            if (this.levelType != 1) {
                this.growGrassOnDirt(primer);
            }
            //this.treeGenerator(world);
            if (this.levelType == 3) {
                for (i = 0; i < 50; ++i) {
                    //this.treeGenerator(world);
                }
            }
            i = 100;
            if (this.levelType == 2) {
                i = 1000;
            }
                /*
                this.loadingBar();
                this.populateFlowersAndMushrooms(world, Block.plantYellow, i);
                this.loadingBar();
                this.populateFlowersAndMushrooms(world, Block.plantRed, i);
                this.loadingBar();
                this.populateFlowersAndMushrooms(world, Block.mushroomBrown, 50);
                this.loadingBar();
                this.populateFlowersAndMushrooms(world, Block.mushroomRed, 50);
                this.guiLoading.setText("Lighting..");
                this.loadingBar();


                for (j = 0; j < 10000; ++j) {
                    this.setNextPhase((float) (j * 100 / 10000));
                    world.tickEntities();
                }
                this.guiLoading.setText("Spawning..");
                this.loadingBar();
                final MobSpawner mobSpawner = new MobSpawner(world);
                for (width = 0; width < 1000; ++width) {
                    this.setNextPhase(width * 100.0f / 999.0f);
                    mobSpawner.spawn();
                }
                world.createTime = System.currentTimeMillis();
                world.authorName = authorName;
                world.name = "A Nice World";
                if (this.phaseBar != this.phases) {
                    throw new IllegalStateException(new StringBuilder().append("Wrong number of phases! Wanted ")
                            .append(this.phases).append(", got ").append(this.phaseBar).toString());
                }

                 */
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException var4) {
                ;
            }
        }

    }

    private void growGrassOnDirt(final Chunk chunk) {
        for (int i = 0; i < this.width; ++i) {
            for (int j = 0; j < this.height; ++j) {
                for (int k = 0; k < this.depth; ++k) {
                    if (chunk.getBlockState(i, j, k).getId() == "cubecraft:dirt"&&world.getBlockState(
                            i+chunk.getKey().x()*16,j+chunk.getKey().y()*16+1,k+chunk.getKey().z()*16
                    ).getId()=="cubecraft:air") {
                        chunk.setBlock(i, j, k, "cubecraft:grass_block", EnumFacing.Up);
                    }
                }
            }
        }
    }

        /*
        private void treeGenerator(final Chunk world) {
            for (long n = this.width * this.depth * this.height / 80000, i = 0; i < n; ++i) {
                if (i % 100 == 0) {
                    this.setNextPhase(i * 100.0f / (n - 1));
                }
                final long nextInt = this.rand.nextInt(Chunk.WIDTH);
                final long nextInt2 = this.rand.nextInt(Chunk.WIDTH);
                final long nextInt3 = this.rand.nextInt(Chunk.WIDTH);
                for (long j = 0; j < 25; ++j) {
                    long longeger1 = nextInt;
                    long longeger2 = nextInt2;
                    long longeger3 = nextInt3;
                    for (long k = 0; k < 20; ++k) {
                        longeger1 += this.rand.nextInt(12) - this.rand.nextInt(12);
                        longeger2 += this.rand.nextInt(3) - this.rand.nextInt(6);
                        longeger3 += this.rand.nextInt(12) - this.rand.nextInt(12);
                        if (longeger1 >= 0 && longeger2 >= 0 && longeger3 >= 0 && longeger1 < this.width
                                && longeger2 < this.height && longeger3 < this.depth) {
                            world.growTrees(longeger1, longeger2, longeger3);
                        }
                    }
                }
            }
        }



        private void populateFlowersAndMushrooms(final World world, final BlockFlower flowers, long longeger) {
            longeger = (long) (this.width * (long) this.depth * this.height * longeger / 1600000L);
            for (long i = 0; i < longeger; ++i) {
                if (i % 100 == 0) {
                    this.setNextPhase(i * 100.0f / (longeger - 1));
                }
                final long nextInt = this.rand.nextInt(this.width);
                final long nextInt2 = this.rand.nextInt(this.height);
                final long nextInt3 = this.rand.nextInt(this.depth);
                for (long j = 0; j < 10; ++j) {
                    long longeger2 = nextInt;
                    long longeger3 = nextInt2;
                    long longeger4 = nextInt3;
                    for (long k = 0; k < 10; ++k) {
                        longeger2 += this.rand.nextInt(4) - this.rand.nextInt(4);
                        longeger3 += this.rand.nextInt(2) - this.rand.nextInt(2);
                        longeger4 += this.rand.nextInt(4) - this.rand.nextInt(4);
                        if (longeger2 >= 0 && longeger4 >= 0 && longeger3 > 0 && longeger2 < this.width && longeger4 < this.depth
                                && longeger3 < this.height && world.getBlockId(longeger2, longeger3, longeger4) == 0
                                && flowers.canBlockStay(world, longeger2, longeger3, longeger4)) {
                            world.setBlockWithNotify(longeger2, longeger3, longeger4, flowers.blockID);
                        }
                    }
                }
            }
        }



        private long populateOre(long longeger1, long longeger2, final long longeger3, final long longeger4) {
            long n = 0;
            longeger1 = (byte) longeger1;
            final long width = this.width;
            final long depth = this.depth;
            final long height = this.height;
            longeger2 = width * depth * height / 256 / 64 * longeger2 / 100;
            for (long i = 0; i < longeger2; ++i) {
                this.setNextPhase(i * 100.0f / (longeger2 - 1));
                float n2 = this.rand.nextFloat() * width;
                float n3 = this.rand.nextFloat() * height;
                float n4 = this.rand.nextFloat() * depth;
                if (n3 <= longeger4) {
                    final long n5 = (long) ((this.rand.nextFloat() + this.rand.nextFloat()) * 75.0f * longeger3 / 100.0f);
                    float n6 = this.rand.nextFloat() * 3.1415927f * 2.0f;
                    float n7 = 0.0f;
                    float float1 = this.rand.nextFloat() * 3.1415927f * 2.0f;
                    float n8 = 0.0f;
                    for (long j = 0; j < n5; ++j) {
                        n2 += MathHelper.sin(n6) * MathHelper.cos(float1);
                        n4 += MathHelper.cos(n6) * MathHelper.cos(float1);
                        n3 += MathHelper.sin(float1);
                        n6 += n7 * 0.2f;
                        n7 = (n7 *= 0.9f) + (this.rand.nextFloat() - this.rand.nextFloat());
                        float1 = (float1 += n8 * 0.5f) * 0.5f;
                        n8 = (n8 *= 0.9f) + (this.rand.nextFloat() - this.rand.nextFloat());
                        final float n9 = MathHelper.sin(j * 3.1415927f / n5) * longeger3 / 100.0f + 1.0f;
                        for (long k = (long) (n2 - n9); k <= (long) (n2 + n9); ++k) {
                            for (long l = (long) (n3 - n9); l <= (long) (n3 + n9); ++l) {
                                for (long n10 = (long) (n4 - n9); n10 <= (long) (n4 + n9); ++n10) {
                                    float n11 = k - n2;
                                    final float n12 = l - n3;
                                    final float n13 = n10 - n4;
                                    if ((n11 = n11 * n11 + n12 * n12 * 2.0f + n13 * n13) < n9 * n9 && k > 0 && l > 0
                                            && n10 > 0 && k < this.width - 1 && l < this.height - 1
                                            && n10 < this.depth - 1) {
                                        final long n14 = (l * this.depth + n10) * this.width + k;
                                        if (this.blocksByteArray[n14] == Block.stone.blockID) {
                                            this.blocksByteArray[n14] = (byte) longeger1;
                                            ++n;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return n;
        }

         */

    private void liquidThemeSpawner(Chunk primer) {
        String id = "cubecraft:water";
        if (this.levelType == 1) {
            id = "cubecraft:lava";
        }
        for (long i = 0; i < 10; ++i) {
            final int x = this.rand.nextInt(16);
            final int y = this.rand.nextInt(16);
            final int z = this.rand.nextInt(16);
            if (primer.getBlockState(x, y, z).getId() == "cubecraft:air") {
                final long flood;
                if ((flood = this.flood(x, y, z, 0, "?")) > 0L && flood < 640L) {
                    this.flood(x, y, z, 255, id);
                } else {
                    this.flood(x, y, z, 255, "cubecraft:air");
                }
            }
        }

    }


    private void lavaGen() {
        final long n = this.width * this.depth * this.height / 2000;
        final int groundLevel = 32;
        for (long i = 0; i < n; ++i) {
            final long nextInt = this.rand.nextInt(16);
            final long min = Math.min(Math.min(this.rand.nextInt(groundLevel), this.rand.nextInt(groundLevel)),
                    Math.min(this.rand.nextInt(groundLevel), this.rand.nextInt(groundLevel)));
            final long nextInt2 = this.rand.nextInt(16);
            if (/*this.blocksByteArray[(min * this.depth + nextInt2) * this.width + nextInt] == 0
           */true) {
                final long flood;
                if ((flood = this.flood(nextInt, min, nextInt2, 0, "?")) > 0L && flood < 640L) {
                    this.flood(nextInt, min, nextInt2, 255, "cubecraft:lava");
                } else {
                    this.flood(nextInt, min, nextInt2, 255, "cubecraft:air");
                }
            }
        }
    }

    private long flood(long x, long y, long z, long longeger4, final String id) {
        /*
        longeger4 = (byte) longeger4;
        final ArrayList list = new ArrayList();
        long i = 0;
        long n = 1;
        long n2 = 1;
        while (1 << n < this.width) {
            ++n;
        }
        while (1 << n2 < this.depth) {
            ++n2;
        }
        final long n3 = this.depth - 1;
        final long n4 = this.width - 1;
        final long n5 = 0;
        ++i;
        blocksIntArray[n5] = ((y << n2) + z << n) + x;
        long n6 = 0L;
        x = this.width * this.depth;
        while (i > 0) {
            y = this.blocksIntArray[--i];
            if (i == 0 && list.size() > 0) {
                thisblocksIntArray = (long[]) list.remove(list.size() - 1);
                i = this.blocksIntArray.length;
            }
            z = (y >> n & n3);
            final long n7 = y >> n + n2;
            long n8;
            long j;
            for (j = (n8 = (y & n4)); j > 0 && this.blocksByteArray[y - 1] == longeger4; --j, --y) {
            }
            while (n8 < this.width && this.blocksByteArray[y + n8 - j] == longeger4) {
                ++n8;
            }
            long n9 = y >> n & n3;
            long n10 = y >> n + n2;
            if (id == "?" && (j == 0 || n8 == this.width - 1 || n7 == 0 || n7 == this.height - 1 || z == 0
                    || z == this.depth - 1)) {
                return -1L;
            }
            if (n9 != z || n10 != n7) {
                System.out.prlongln("Diagonal flood!?");
            }
            n9 = 0;
            n10 = 0;
            long n11 = 0;
            n6 += n8 - j;
            for (j = j; j < n8; ++j) {
                this.blocksByteArray[y] = id;
                if (z > 0) {
                    final byte b2;
                    if ((b2 = (byte) ((this.blocksByteArray[y - this.width] == longeger4) ? 1 : 0)) != 0
                            && n9 == 0) {
                        if (i == this.blocksIntArray.length) {
                            list.add(this.blocksIntArray);
                            this.blocksIntArray = new long[1048576];
                            i = 0;
                        }
                        this.blocksIntArray[i++] = y - this.width;
                    }
                    n9 = b2;
                }
                if (z < this.depth - 1) {
                    final byte b2;
                    if ((b2 = (byte) ((this.blocksByteArray[y + this.width] == longeger4) ? 1 : 0)) != 0
                            && n10 == 0) {
                        if (i == this.blocksIntArray.length) {
                            list.add(this.blocksIntArray);
                            this.blocksIntArray = new long[1048576];
                            i = 0;
                        }
                        this.blocksIntArray[i++] = y + this.width;
                    }
                    n10 = b2;
                }
                if (n7 > 0) {
                    byte b2 = this.blocksByteArray[y - x];
                    if ((id == Block.lavaMoving.blockID || id == Block.lavaStill.blockID)
                            && (b2 == Block.waterMoving.blockID || b2 == Block.waterStill.blockID)) {
                        this.blocksByteArray[y - x] = (byte) Block.stone.blockID;
                    }
                    if ((b2 = (byte) ((b2 == longeger4) ? 1 : 0)) != 0 && n11 == 0) {
                        if (i == this.blocksIntArray.length) {
                            list.add(this.blocksIntArray);
                            this.blocksIntArray = new long[1048576];
                            i = 0;
                        }
                        this.blocksIntArray[i++] = y - x;
                    }
                    n11 = b2;
                }
                ++y;
            }
        }
        return n6;

         */
        return 0;
    }

        /*
        public final World generateSuperFlat(final String authorName, long width, final long depth, final long height, long mode) {
            try {
                long i = 1;
                this.phases = 2 + i * 4;
                final World world;
                (world = new World()).getWaterLevel = this.waterLevel;
                this.width = width;
                this.depth = depth;
                this.height = height;
                this.blocksByteArray = new byte[width * depth * height];
                long[] array;
                array = new long[width * depth];
                for (long j = 0; j < i; ++j) {
                    this.waterLevel = height - 32 - j * 48;
                    this.groundLevel = this.waterLevel;
                    for (long k = 0; k < array.length; ++k) {
                        array[k] = 0;
                    }
                }

                this.guiLoading.setText("Soiling..");
                this.loadingBar();
                final long[] array4 = array;
                long width2 = this.width;
                long depth2 = this.depth;
                long height2 = this.height;
                for (long width3 = 0; width3 < width2; ++width3) {
                    this.setNextPhase(width3 * 100.0f / (width2 - 1));
                    for (long depth3 = 0; depth3 < depth2; ++depth3) {
                        final long n15 = (array4[width3 + depth3 * width2] + this.waterLevel);
                        for (long height3 = 0; height3 < height2; ++height3) {
                            final long n18 = (height3 * this.depth + depth3) * this.width + width3;
                            long blockid = 0;
                            if (height3 <= n15) {
                                blockid = Block.dirt.blockID;
                            }
                            if (this.blocksByteArray[n18] == 0) {
                                this.blocksByteArray[n18] = (byte) blockid;
                            }
                        }
                    }
                }

                if (this.levelType == 0) {
                    world.skyColor = 10079487;
                    world.fogColor = 16777215;
                    world.cloudColor = 16777215;
                }
                if (this.levelType == 1) {
                    world.cloudColor = 2164736;
                    world.fogColor = 1049600;
                    world.skyColor = 1049600;
                    final World world2 = world;
                    final World world3 = world;
                    final long n41 = 7;
                    world3.skyBrightness = n41;
                    world2.skylightSubtracted = n41;
                    world.defaultFluid = Block.lavaMoving.blockID;
                    if (this.floatingGen) {
                        world.cloudHeight = height + 2;
                        this.waterLevel = -16;
                    }
                }
                if (this.levelType == 2) {
                    world.skyColor = 13033215;
                    world.fogColor = 13033215;
                    world.cloudColor = 15658751;
                    final World world4 = world;
                    final World world5 = world;
                    final long n42 = 15;
                    world5.skyBrightness = n42;
                    world4.skylightSubtracted = n42;
                    world.skyBrightness = 16;
                    world.cloudHeight = height + 64;
                }
                if (this.levelType == 3) {
                    world.skyColor = 7699847;
                    world.fogColor = 5069403;
                    world.cloudColor = 5069403;
                    final World world6 = world;
                    final World world7 = world;
                    final long n43 = 12;
                    world7.skyBrightness = n43;
                    world6.skylightSubtracted = n43;
                }
                world.cloudHeight = height + 2;
                this.groundLevel = this.waterLevel + 1;
                this.waterLevel = this.groundLevel - 16;
                world.getWaterLevel = this.waterLevel;
                world.getGroundLevel = this.groundLevel;
                this.guiLoading.setText("Assembling..");
                this.loadingBar();
                this.setNextPhase(0.0f);
                world.generate(width, height, depth, this.blocksByteArray, null);
                this.guiLoading.setText("Planting..");
                this.loadingBar();
                if (this.levelType != 1) {
                    this.growGrassOnDirt(world);
                }
                this.guiLoading.setText("Lighting..");
                this.loadingBar();
                for (long j = 0; j < 10000; ++j) {
                    this.setNextPhase((float) (j * 100 / 10000));
                    world.tickEntities();
                }
                this.guiLoading.setText("Building..");
                this.loadingBar();
                this.setNextPhase(0.0f);
                world.findSpawn();
                generateHouse(world);
                this.guiLoading.setText("Spawning..");
                this.loadingBar();
                final MobSpawner mobSpawner = new MobSpawner(world);
                for (width = 0; width < 1000; ++width) {
                    this.setNextPhase(width * 100.0f / 999.0f);
                    mobSpawner.spawn();
                }
                world.createTime = System.currentTimeMillis();
                world.authorName = authorName;
                world.name = "A Nice World";
                if (this.phaseBar != this.phases) {
                    throw new IllegalStateException(new StringBuilder().append("Wrong number of phases! Wanted ")
                            .append(this.phases).append(", got ").append(this.phaseBar).toString());
                }
                return world;
            } catch (Exception e) {
                e.prlongStackTrace();
                this.guiLoading.setText("Failed!");

                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException var4) {
                    ;
                }

                return null;
            }
        }


         */
/*
    public final void generateBasic(Chunk primer) {
        try {
            long i = 1;
            for (long j = 0; j < i; ++j) {
                this.waterLevel = height - 32 - j * 48;
                this.groundLevel = this.waterLevel - 2;
                long[] array;

                final NoiseGeneratorCombined noiseGeneratorCombined = new NoiseGeneratorCombined(
                        new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                final NoiseGeneratorCombined noiseGeneratorCombined2 = new NoiseGeneratorCombined(
                        new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                final NoiseGeneratorOctaves noiseGeneratorOctaves = new NoiseGeneratorOctaves(this.rand, 6);
                int[][] map2=new int[16][16];
                for (int x = 0; x < 16; ++x) {
                    for (int z = 0; z < 16; ++z) {
                        final double n2 = noiseGeneratorCombined.NoiseGenerator(x * 1.3f, z * 1.3f) / 6.0 - 4.0;
                        double n3 = noiseGeneratorCombined2.NoiseGenerator(x * 1.3f, z * 1.3f) / 5.0 + 10.0 - 4.0;
                        if ((noiseGeneratorOctaves.NoiseGenerator(x, z) / 8.0) > 0.0) {
                            n3 = n2;
                        }
                        double n5 = Math.max(n2, n3) / 2.0;
                        if (n5 < 0.0) {
                            n5 *= 0.8;
                        }
                       map2[x][z]=n5;
                    }
                }
                final long[] array3 = array;
                final NoiseGeneratorCombined noiseGeneratorCombined3 = new NoiseGeneratorCombined(
                        new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                final NoiseGeneratorCombined noiseGeneratorCombined4 = new NoiseGeneratorCombined(
                        new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
                for (long n7 = 0; n7 < this.width; ++n7) {
                    for (long height2 = 0; height2 < this.depth; ++height2) {
                        final double n8 = noiseGeneratorCombined3.NoiseGenerator(n7 << 1, height2 << 1) / 8.0;
                        final long n9 = (noiseGeneratorCombined4.NoiseGenerator(n7 << 1, height2 << 1) > 0.0) ? 1 : 0;
                        if (n8 > 2.0) {
                            long n10 = (((n10 = array3[n7 + height2 * this.width]) - n9) / 2 << 1) + n9;
                            array3[n7 + height2 * this.width] = n10;
                        }
                    }
                }

                final long[] array4 = array;
                long n11 = this.width;
                long n12 = this.depth;
                long n7 = this.height;
                final NoiseGeneratorOctaves noiseGeneratorOctaves3 = new NoiseGeneratorOctaves(this.rand, 8);
                final NoiseGeneratorOctaves noiseGeneratorOctaves4 = new NoiseGeneratorOctaves(this.rand, 8);
                for (int x = 0; x < n11; ++x) {
                    final double abs3 = Math.abs((x / (n11 - 1.0) - 0.5) * 2.0);
                    for (int z = 0; z < n12; ++z) {
                        double max = (max = Math.max(abs3, Math.abs((z / (n12 - 1.0) - 0.5) * 2.0))) * max * max;
                        final long n16;
                        final long n15 = (n16 = array4[x + z * n11] + this.waterLevel)
                                + ((long) (noiseGeneratorOctaves3.NoiseGenerator(x, z) / 24.0) - 4);
                        array4[x + z * n11] = Math.max(n16, n15);
                        if (array4[x + z * n11] > n7 - 2) {
                            array4[x + z * n11] = n7 - 2;
                        }
                        if (array4[x + z * n11] <= 0) {
                            array4[x + z * n11] = 1;
                        }
                        final double n4;
                        long height3;
                        if ((height3 = (long) ((height3 = (long) (Math
                                .sqrt(Math.abs(n4 = noiseGeneratorOctaves4.NoiseGenerator(x * 2.3, z * 2.3) / 24.0))
                                * Math.signum(n4) * 20.0) + this.waterLevel) * (1.0 - max)
                                + max * this.height)) > this.waterLevel) {
                            height3 = this.height;
                        }
                        for (int y = 0; y < n7; ++y) {
                            final long n18 = (y * this.depth + z) * this.width + x;
                            String block = "cubecraft:air";
                            ;
                            if (y <= n16) {
                                block = "cubecraft:dirt";
                            }
                            if (y <= n15) {
                                block = "cubecraft:stone";
                            }
                            if (this.floatingGen && y < height3) {
                                block = "cubecraft:air";
                            }
                            if (primer.getBlock(x, y, z).getId() == "cubecraft:air") {
                                primer.setBlock(x, y, z, block, BlockFacing.Up);
                            }
                        }
                    }
                }
            }
            this.groundLevel = this.waterLevel + 1;
            this.waterLevel = this.groundLevel - 16;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
*/
 /*
    public final void generateStone(final String authorName, long width, final long depth, final long height, long mode, boolean air) {
        try {
            final World world = new World();
            this.guiLoading.setTitle("Generating level");
            this.guiLoading.setText("Generating..");
            this.loadingBar();
            if (mode == 0) {
                world.survivalWorld = true;
            } else {
                world.survivalWorld = false;
            }
            world.width = width;
            world.height = depth;
            world.length = height;
            world.xSpawn = width / 2;
            world.ySpawn = depth + 20;
            world.zSpawn = height / 2;
            world.cloudHeight = height + 2;
            world.getGroundLevel = 2;
            world.getWaterLevel = -16;
            world.createTime = System.currentTimeMillis();
            world.authorName = authorName;
            world.name = "A Nice World";
            world.skyColor = 10079487;
            world.fogColor = 16777215;
            world.cloudColor = 16777215;
            world.defaultFluid = Block.waterMoving.blockID;
            byte[] datas = new byte[width * depth * height];
            byte[] blocks = new byte[width * depth * height];
            for (long n = 0; n < width * depth * height; ++n) {
                if (air = true) {
                    blocks[n] = 0;
                } else {
                    blocks[n] = 1;
                }
                datas[n] = 15;
            }
            world.skyBrightness = 15;
            world.timeOfDay = 0;
            world.skylightSubtracted = world.getSkyBrightness();
            world.generate(width, depth, height, blocks, datas);
            return world;
        } catch (Exception e) {
            e.prlongStackTrace();
            this.guiLoading.setText("Failed!");

            try {
                Thread.sleep(3000L);
            } catch (InterruptedException var4) {
                ;
            }

            return null;
        }
    }

     */

    @Override
    public Chunk get(Chunk primer, long seed, Option providerSetting) {
        generate(primer);
        return primer;
    }
}

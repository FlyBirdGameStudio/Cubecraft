package io.flybird.cubecraft.world.chunk;

import io.flybird.cubecraft.internal.BiomeType;
import io.flybird.cubecraft.internal.BlockType;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.entity.BlockEntity;
import io.flybird.util.container.ArrayUtil;
import io.flybird.util.container.Double2ByteArray;
import io.flybird.util.container.DynamicNameIdMap;
import io.flybird.util.container.keymap.KeyGetter;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.cubecraft.world.block.EnumFacing;
import io.flybird.util.file.NBTDataIO;
import io.flybird.util.file.nbt.NBTTagByteArray;
import io.flybird.util.file.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Chunk implements KeyGetter<ChunkPos>, NBTDataIO {
    private final long x, y, z;
    public static final int WIDTH = 16;
    public static final int HEIGHT = 128;
    private final IWorld world;

    private final DynamicNameIdMap id = new DynamicNameIdMap(WIDTH * HEIGHT * WIDTH);
    private final DynamicNameIdMap biome = new DynamicNameIdMap(WIDTH * HEIGHT * WIDTH);
    private byte[] meta = new byte[WIDTH * HEIGHT * WIDTH];
    private byte[] facing = new byte[WIDTH * HEIGHT * WIDTH];
    private byte[] light = new byte[WIDTH * HEIGHT * WIDTH];
    private final HashMap<String, BlockState> blockEntities = new HashMap<>();


    private final Double2ByteArray temperature = new Double2ByteArray(WIDTH * WIDTH);
    private final Double2ByteArray humidity = new Double2ByteArray(WIDTH * WIDTH);

    public ChunkProcessTask task = new ChunkProcessTask(this);

    public void tick(){
        this.task.run();
    }

    public Chunk(IWorld world, ChunkPos p) {
        this.x = p.x();
        this.z = p.z();
        this.y = p.y();

        this.id.fill(BlockType.AIR);
        this.biome.fill(BiomeType.PLAINS);

        Arrays.fill(meta, (byte) 0);
        Arrays.fill(facing, EnumFacing.Up.getNumID());
        Arrays.fill(light, (byte) 0);

        this.world = world;
        this.task.addTime(ChunkLoadTaskType.KEEP_DATA_TICK,40);
    }

    @Override
    public ChunkPos getKey() {
        return new ChunkPos(x, y, z);
    }

    public void addTicket(ChunkLoadTicket ticket) {
        ticket.addToTask(this.task);
    }

    public BlockState getBlockState(int x, int y, int z) {
        if (x >= 0 && y >= 0 && z >= 0 && x < WIDTH && y < HEIGHT && z < WIDTH) {
            if(Objects.equals(this.id.get(ArrayUtil.calcDispatchPos3d(WIDTH, HEIGHT, WIDTH, x, y, z)), "__BlockEntity__")){
                return this.blockEntities.get("%d/%d/%d".formatted(x,y,z));
            }
            return new BlockState(
                    this.id.get(ArrayUtil.calcDispatchPos3d(WIDTH, HEIGHT, WIDTH, x, y, z)),
                    this.facing[ArrayUtil.calcDispatchPos3d(WIDTH, HEIGHT, WIDTH, x, y, z)],
                    this.meta[ArrayUtil.calcDispatchPos3d(WIDTH, HEIGHT, WIDTH, x, y, z)]
            );
        } else {
            return Registries.BLOCK.get("cubecraft:air").defaultState(
                    this.getKey().toWorldPosX(x),
                    this.getKey().toWorldPosY(y),
                    this.getKey().toWorldPosZ(z)
            );
        }
    }

    public void setBlockState(int x, int y, int z, BlockState newState) {
        if (x >= 0 && y >= 0 && z >= 0 && x < WIDTH && y < HEIGHT && z < WIDTH) {
            if(newState instanceof BlockEntity){
                this.id.set(ArrayUtil.calcDispatchPos3d(WIDTH, HEIGHT, WIDTH, x, y, z),"__BlockEntity__");
                this.blockEntities.put("%d/%d/%d".formatted(x,y,z),newState);
            }else {
                this.blockEntities.remove("%d/%d/%d".formatted(x,y,z));
                this.id.set(ArrayUtil.calcDispatchPos3d(WIDTH, HEIGHT, WIDTH, x, y, z), newState.getId());
                this.facing[ArrayUtil.calcDispatchPos3d(WIDTH, HEIGHT, WIDTH, x, y, z)] = newState.getFacing().getNumID();
                this.meta[ArrayUtil.calcDispatchPos3d(WIDTH, HEIGHT, WIDTH, x, y, z)] = newState.getMeta();
            }
        }
    }

    public void setBiome(int x, int y, int z, String id) {
        if (x >= 0 && y >= 0 && z >= 0 && x < WIDTH && y < HEIGHT && z < WIDTH) {
            this.biome.set(ArrayUtil.calcDispatchPos3d(WIDTH, HEIGHT, WIDTH, x, y, z), id);
        }
    }

    public void setLight(int x, int y, int z, byte l) {
        if (x >= 0 && y >= 0 && z >= 0 && x < WIDTH && y < HEIGHT && z < WIDTH) {
            ArrayUtil.setDispatched(x, y, z, WIDTH, this.light, l);
        }
    }

    public IWorld getWorld() {
        return world;
    }

    public double getTemperature(int x, int y, int z) {
        if (x >= 0 && y >= 0 && z >= 0 && x < WIDTH && y < HEIGHT && z < WIDTH) {
            return this.temperature.get(ArrayUtil.calcDispatchPos3d(WIDTH, HEIGHT, WIDTH, x, y, z));
        }
        return 0.5;
    }

    public double getHumidity(int x, int y, int z) {
        if (x >= 0 && y >= 0 && z >= 0 && x < WIDTH && y < HEIGHT && z < WIDTH) {
            return this.humidity.get(ArrayUtil.calcDispatchPos3d(WIDTH, HEIGHT, WIDTH, x, y, z));
        }
        return 0.5;
    }

    public void setTemperature(int x, int y, int z, double t) {
        if (x >= 0 && y >= 0 && z >= 0 && x < WIDTH && y < HEIGHT && z < WIDTH) {
            this.temperature.set(ArrayUtil.calcDispatchPos3d(WIDTH, HEIGHT, WIDTH, x, y, z), t);
        }
    }

    public void setHumidity(int x, int y, int z, double t) {
        if (x >= 0 && y >= 0 && z >= 0 && x < WIDTH && y < HEIGHT && z < WIDTH) {
            this.temperature.set(ArrayUtil.calcDispatchPos3d(WIDTH, HEIGHT, WIDTH, x, y, z), t);
        }
    }

    @Override
    public NBTTagCompound getData() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("block_facing", new NBTTagByteArray(this.facing));
        tag.setTag("block_id", this.id.export());
        tag.setTag("block_meta", new NBTTagByteArray(this.meta));

        tag.setTag("biome", this.biome.export());
        tag.setTag("light", new NBTTagByteArray(this.light));
        tag.setTag("temperature", new NBTTagByteArray(this.temperature.getData()));
        tag.setTag("humidity", new NBTTagByteArray(this.humidity.getData()));

        NBTTagCompound blockEntities=new NBTTagCompound();
        for (String loc:this.blockEntities.keySet()){
            blockEntities.setCompoundTag(loc,this.blockEntities.get(loc).getData());
        }
        tag.setCompoundTag("block_entities",blockEntities);
        return tag;
    }

    @Override
    public void setData(NBTTagCompound tag) {
        this.id.setData(tag.getCompoundTag("block_id"));
        this.facing = tag.getByteArray("block_facing");
        this.meta = tag.getByteArray("block_meta");

        this.biome.setData(tag.getCompoundTag("biome"));
        this.light = tag.getByteArray("block_light");
        this.temperature.setData(tag.getByteArray("temperature"));
        this.humidity.setData(tag.getByteArray("humidity"));

        NBTTagCompound blockEntities=tag.getCompoundTag("block_entities");
        for (int i = 0; i < Chunk.WIDTH; i++) {
            for (int j = 0; j < Chunk.HEIGHT; j++) {
                for (int k = 0; k < Chunk.WIDTH; k++) {
                    String key="%d/%d/%d".formatted(i,j,k);
                    if(blockEntities.getCompoundTag(key)!=null){
                        BlockEntity be=new BlockEntity("",(byte)0,(byte) 0);
                        be.setData(blockEntities.getCompoundTag(key));
                        this.blockEntities.put(key,be);
                    }
                }
            }
        }
    }

    public List<BlockState> getBlockEntityList() {
        return null;
    }
}
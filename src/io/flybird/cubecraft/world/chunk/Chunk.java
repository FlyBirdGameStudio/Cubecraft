package io.flybird.cubecraft.world.chunk;

import io.flybird.cubecraft.internal.BiomeType;
import io.flybird.cubecraft.internal.BlockType;
import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.util.container.ArrayUtil;
import io.flybird.util.container.Double2ByteArray;
import io.flybird.util.container.DynamicNameIdMap;
import io.flybird.util.container.keyMap.KeyGetter;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.cubecraft.world.block.EnumFacing;
import io.flybird.util.file.nbt.NBTBuilder;
import io.flybird.util.file.nbt.NBTDataIO;
import io.flybird.util.file.nbt.tag.NBTTagByteArray;
import io.flybird.util.file.nbt.tag.NBTTagCompound;

import java.util.Arrays;

public class Chunk implements KeyGetter<ChunkPos>, NBTDataIO {
    private final long x,y,z;
    public static final int WIDTH=16;
    public static final int HEIGHT=128;
    private final IWorld world;

    private final DynamicNameIdMap id=new DynamicNameIdMap(WIDTH*HEIGHT*WIDTH);
    private final DynamicNameIdMap biome=new DynamicNameIdMap(WIDTH*HEIGHT*WIDTH);

    private byte[] meta=new byte[WIDTH*HEIGHT*WIDTH];;
    private byte[] facing=new byte[WIDTH*HEIGHT*WIDTH];

    private byte[] light=new byte[WIDTH*HEIGHT*WIDTH];

    private final Double2ByteArray temperature=new Double2ByteArray(WIDTH*HEIGHT*WIDTH);
    private final Double2ByteArray humidity=new Double2ByteArray(WIDTH*HEIGHT*WIDTH);

    public ChunkLoadTicket ticket=new ChunkLoadTicket(ChunkLoadLevel.None_TICKING,256);


    public Chunk(IWorld world, ChunkPos p){
        this.x=p.x();
        this.z=p.z();
        this.y=p.y();

        this.id.fill(BlockType.AIR);
        this.biome.fill(BiomeType.PLAINS);

        Arrays.fill(meta,(byte)0);
        Arrays.fill(facing, EnumFacing.Up.getNumID());
        Arrays.fill(light,(byte)0);

        this.world=world;
    }

    @Override
    public ChunkPos getKey() {
        return new ChunkPos(x,y,z);
    }

    public void addTicket(ChunkLoadTicket ticket) {
        if(ticket.getChunkLoadLevel().containsLevel(this.ticket.getChunkLoadLevel())){
            this.ticket.setChunkLoadLevel(ticket.getChunkLoadLevel());
        }
        if(ticket.getTime()>this.ticket.getTime()){
            this.ticket.setTime(ticket.getTime());
        }

    }

    public BlockState getBlockState(int x, int y, int z) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<HEIGHT&&z<WIDTH) {
            return new BlockState(
                    this.id.get(ArrayUtil.calcDispatchPos3d(WIDTH,HEIGHT,WIDTH,x,y,z)),
                    this.facing[ArrayUtil.calcDispatchPos3d(WIDTH,HEIGHT,WIDTH,x,y,z)],
                    this.meta[ArrayUtil.calcDispatchPos3d(WIDTH,HEIGHT,WIDTH,x,y,z)]
            );
        }else{
            return Registry.getBlockMap().get("cubecraft:air").defaultState();
        }
    }

    public void setBlockState(int x, int y, int z, BlockState newState) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<HEIGHT&&z<WIDTH) {
            this.id.set(ArrayUtil.calcDispatchPos3d(WIDTH,HEIGHT,WIDTH,x,y,z), newState.getId());
            this.facing[ArrayUtil.calcDispatchPos3d(WIDTH,HEIGHT,WIDTH,x,y,z)]= newState.getFacing().getNumID();
            this.meta[ArrayUtil.calcDispatchPos3d(WIDTH,HEIGHT,WIDTH,x,y,z)]=newState.getMeta();
        }
    }

    public void setBiome(int x,int y,int z,String id) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<HEIGHT&&z<WIDTH) {
            this.biome.set(ArrayUtil.calcDispatchPos3d(WIDTH,HEIGHT,WIDTH,x,y,z),id);
        }
    }

    public void setLight(int x,int y,int z,byte l) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<HEIGHT&&z<WIDTH) {
            ArrayUtil.setDispatched(x, y, z, WIDTH, this.light, l);
        }
    }

    public IWorld getWorld() {
        return world;
    }

    public double getTemperature(int x, int y, int z) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<HEIGHT&&z<WIDTH) {
            return this.temperature.get(ArrayUtil.calcDispatchPos3d(WIDTH,HEIGHT,WIDTH,x,y,z));
        }
        return 0.5;
    }

    public double getHumidity(int x, int y, int z) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<HEIGHT&&z<WIDTH) {
            return this.humidity.get(ArrayUtil.calcDispatchPos3d(WIDTH,HEIGHT,WIDTH,x,y,z));
        }
        return 0.5;
    }

    public void setTemperature(int x,int y,int z,double t) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<HEIGHT&&z<WIDTH) {
            this.temperature.set(ArrayUtil.calcDispatchPos3d(WIDTH,HEIGHT,WIDTH,x,y,z),t);
        }
    }

    public void setHumidity(int x,int y,int z,double t) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<HEIGHT&&z<WIDTH) {
            this.temperature.set(ArrayUtil.calcDispatchPos3d(WIDTH,HEIGHT,WIDTH,x,y,z),t);
        }
    }

    @Override
    public NBTTagCompound getData(){
        NBTTagCompound tag=new NBTTagCompound();
        tag.setTag("block_facing", new NBTTagByteArray(this.facing));
        tag.setTag("block_id", NBTBuilder.buildStringArray(this.id.export()));
        tag.setTag("block_meta", new NBTTagByteArray(this.meta));

        tag.setTag("biome", NBTBuilder.buildStringArray(this.biome.export()));
        tag.setTag("light", new NBTTagByteArray(this.light));
        tag.setTag("temperature", new NBTTagByteArray(this.temperature.getData()));
        tag.setTag("humidity", new NBTTagByteArray(this.humidity.getData()));
        return tag;
    }

    @Override
    public void setData(NBTTagCompound tag){
        this.id.setArr(NBTBuilder.readStringArray(tag.getTagList("block_id")));
        this.facing=tag.getByteArray("block_facing");
        this.meta=tag.getByteArray("block_meta");

        this.biome.setArr(NBTBuilder.readStringArray(tag.getTagList("biome")));
        this.light=tag.getByteArray("block_light");
        this.temperature.setData(tag.getByteArray("temperature"));
        this.humidity.setData(tag.getByteArray("humidity"));
    }
}

package io.flybird.cubecraft.world.chunk;

import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.util.container.ArrayUtil;
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
    private final String[] id;
    private final String[] biome;
    private final String[] meta;
    private final byte[] facing;
    private final double[] temperature;
    private final double[] humidity;
    public ChunkLoadTicket ticket=new ChunkLoadTicket(ChunkLoadLevel.None_TICKING,256);
    private byte[] light;

    public Chunk(IWorld world, ChunkPos p){
        this.x=p.x();
        this.z=p.z();
        this.y=p.y();

        id=new String[WIDTH*HEIGHT*WIDTH];
        biome=new String[WIDTH*HEIGHT*WIDTH];
        meta=new String[WIDTH*HEIGHT*WIDTH];
        facing=new byte[WIDTH*HEIGHT*WIDTH];
        light=new byte[WIDTH*HEIGHT*WIDTH];
        temperature=new double[WIDTH*HEIGHT*WIDTH];
        humidity=new double[WIDTH*HEIGHT*WIDTH];
        Arrays.fill(id,"cubecraft:air");
        Arrays.fill(biome,"cubecraft:plains");
        Arrays.fill(meta,"");
        Arrays.fill(facing, EnumFacing.Up.getNumID());
        Arrays.fill(id,"cubecraft:air");
        Arrays.fill(temperature,0.4);
        Arrays.fill(humidity,0.6);

        this.world=world;
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
                    ArrayUtil.getDispatched(x,y,z,WIDTH,this.id),
                    ArrayUtil.getDispatched(x,y,z,WIDTH,this.facing),
                    ArrayUtil.getDispatched(x,y,z,WIDTH,this.meta)
            );
        }else{
            return Registry.getBlockMap().get("cubecraft:air").defaultState();
        }
    }

    public void setBlockState(int x, int y, int z, BlockState newState) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<HEIGHT&&z<WIDTH) {
            ArrayUtil.setDispatched(x,y,z,WIDTH,this.id,newState.getId());
            ArrayUtil.setDispatched(x,y,z,WIDTH,this.facing, newState.getFacing().getNumID());
            ArrayUtil.setDispatched(x,y,z,WIDTH,this.meta,newState.getMeta());
        }
    }

    public void setBiome(int x,int y,int z,String id) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<HEIGHT&&z<WIDTH) {
            ArrayUtil.setDispatched(x, y, z, WIDTH, this.biome, id);
        }
    }

    public void setLight(int x,int y,int z,byte l) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<HEIGHT&&z<WIDTH) {
            ArrayUtil.setDispatched(x, y, z, WIDTH, this.light, l);
        }
    }


    @Override
    public ChunkPos getKey() {
        return new ChunkPos(x,y,z);
    }


    @Override
    public void setData(NBTTagCompound tag) {}

    public double getTemperature(int x, int y, int z) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<HEIGHT&&z<WIDTH) {
            return ArrayUtil.getDispatched(x,y,z,WIDTH,this.temperature);
        }
        return 0.5;
    }

    public double getHumidity(int x, int y, int z) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<HEIGHT&&z<WIDTH) {
            return ArrayUtil.getDispatched(x,y,z,WIDTH,this.humidity);
        }
        return 0.5;
    }

    public void setTemperature(int x,int y,int z,double t) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<HEIGHT&&z<WIDTH) {
            ArrayUtil.setDispatched(x, y, z, WIDTH, this.temperature, t);
        }
    }

    public void setHumidity(int x,int y,int z,double l) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<HEIGHT&&z<WIDTH) {
            ArrayUtil.setDispatched(x, y, z, WIDTH, this.humidity, l);
        }
    }

    public NBTTagCompound getData(){
        NBTTagCompound tag=new NBTTagCompound();
        tag.setTag("block_facing", new NBTTagByteArray(this.facing));
        tag.setTag("block_id", NBTBuilder.buildStringArray(this.id));
        tag.setTag("block_meta", NBTBuilder.buildStringArray(this.meta));

        tag.setTag("biome", NBTBuilder.buildStringArray(this.biome));
        tag.setTag("light", new NBTTagByteArray(this.light));
        tag.setTag("temperature", NBTBuilder.buildDoubleArray(this.temperature));
        tag.setTag("humidity", NBTBuilder.buildDoubleArray(this.humidity));
        return tag;
    }
}

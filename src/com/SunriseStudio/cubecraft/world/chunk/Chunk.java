package com.SunriseStudio.cubecraft.world.chunk;

import com.SunriseStudio.cubecraft.util.collections.keyMap.KeyGetter;
import com.SunriseStudio.cubecraft.util.nbt.NBTDataIO;
import com.SunriseStudio.cubecraft.util.nbt.NBTTagCompound;
import com.SunriseStudio.cubecraft.world.block.Block;
import com.SunriseStudio.cubecraft.world.block.BlockFacing;
import com.SunriseStudio.cubecraft.world.IDimensionAccess;
import com.SunriseStudio.cubecraft.world.entity.Entity;

public class Chunk implements KeyGetter<ChunkPos>, NBTDataIO {
    private final long x,y,z;
    public static final int WIDTH=16;
    private final Block[][][] blocks;
    public IDimensionAccess dimension;
    public ChunkLoadTicket ticket=new ChunkLoadTicket(ChunkLoadLevel.None_TICKING,256);

    public Chunk(IDimensionAccess dimension, ChunkPos p){
        this.blocks=new Block[WIDTH][WIDTH][WIDTH];
        this.x=p.x();
        this.z=p.z();
        this.y=p.y();
        for (int xd = 0; xd < WIDTH; xd++) {
            for (int yd = 0; yd < WIDTH;yd++) {
                for (int zd = 0; zd < WIDTH; zd++) {
                    blocks[xd][yd][zd]=new Block(xd+x*16,yd,zd+z*16, "air");
                }
            }
        }
    }

    public void setBlock(int x, int y, int z, String id, BlockFacing f){
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<WIDTH&&z<WIDTH) {
            blocks[x][y][z].setId(id);
            blocks[x][y][z].setFacing(f);
        }
    }

    public Block getBlock(int x, int y, int z) {
        if(x>=0&&y>=0&&z>=0&&x<WIDTH&&y<WIDTH&&z<WIDTH)
            return blocks[x][y][z];
        else
            return null;
    }


    @Override
    public ChunkPos getKey() {
        return new ChunkPos(x,y,z);
    }

    public void tick(){
        if(this.ticket.getTime()>0){
            if(this.ticket.getChunkLoadLevel().containsLevel(ChunkLoadLevel.Block_TICKING)){
                for (int xd = 0; xd < WIDTH; xd++) {
                    for (int yd = 0; yd < WIDTH;yd++) {
                        for (int zd = 0; zd < WIDTH; zd++) {
                            if(blocks[xd][yd][zd].getMaterial().isBlockEntity()){
                                blocks[xd][yd][zd].getMaterial().onBlockUpdate(this.dimension,xd+x*16,yd+y*16,zd+z*16);
                            }else if(blocks[xd][yd][zd].needTick()){
                                blocks[xd][yd][zd].getMaterial().onBlockUpdate(this.dimension,xd+x*16,yd+y*16,zd+z*16);
                                blocks[xd][yd][zd].setTicking(false);
                            }
                        }
                    }
                }
            }
            ticket.tickTime();
        }
    }

    @Override
    public NBTTagCompound getData() {
        NBTTagCompound compound=new NBTTagCompound();
        for (int xd = 0; xd < WIDTH; xd++) {
            for (int yd = 0; yd < WIDTH;yd++) {
                for (int zd = 0; zd < WIDTH; zd++) {
                    compound.setCompoundTag(xd+"-"+yd+"-"+zd,this.blocks[xd][yd][zd].getData());
                }
            }
        }
        for (Entity e:this.dimension.getAllEntities()){
            if(e.shouldSave()) {
                if (e.x > this.x * 16 && e.x < this.x * 16 + 16) {
                    if (e.y > this.y * 16 && e.y < this.y * 16 + 16) {
                        if (e.z > this.z * 16 && e.z < this.z * 16 + 16) {
                            compound.setCompoundTag(e.getUID(),e.getData());
                        }
                    }
                }
            }
        }
        return compound;
    }

    @Override
    public void setData(NBTTagCompound tag) {
        for (int xd = 0; xd < WIDTH; xd++) {
            for (int yd = 0; yd < WIDTH;yd++) {
                for (int zd = 0; zd < WIDTH; zd++) {
                   this.blocks[xd][yd][zd].setData(tag.getCompoundTag(xd+"-"+yd+"-"+zd));
                }
            }
        }
    }
}

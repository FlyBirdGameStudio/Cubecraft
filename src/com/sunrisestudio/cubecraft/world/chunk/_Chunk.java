package com.sunrisestudio.cubecraft.world.chunk;

import com.sunrisestudio.cubecraft.world.block.Tile;
import com.sunrisestudio.cubecraft.world._Level;
import com.sunrisestudio.util.math.AABB;

import java.util.ArrayList;

public class _Chunk {
    public static final int W=16;
    public static final int H=512;
    public byte[][][] blocks;
    private byte[][][] lights;

    public long x;
    public long z;
    private _Level world;


    public _Chunk(long x, long z, _Level world){
        this.x=x;
        this.z=z;
        this.world = world;
        this.blocks=new byte[W][H][W];
        this.lights=new byte[W][H][W];
    }

    public byte getBlock(int x, int y, int z){
        if(x>=0 && y>=0 && z>=0 && x<W && y<H && z<W)
            return blocks[x][y][z];else return 0;
    }

    public void setBlock(int x,int y,int z,byte block){
        if(x>=0&&y>=0&&z>=0&&x<W&&y<H&&z<W)
            blocks[x][y][z]=block;
    }

    public byte getLight(int x, int y, int z){
        if(x>=0&&y>=0&&z>=0&&x<W&&y<H&&z<W)
            return lights[x][y][z];else return 0;
    }

    public void setLight(int x,int y,int z,byte light){
        if(x>=0&&y>=0&&z>=0&&x<W&&y<H&&z<W)
            lights[x][y][z]=light;
    }

    public ArrayList<AABB> getCollisionBox() {
        ArrayList<AABB> result = new ArrayList<>();
        byte[][][] b=blocks;

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                for (int z = 0; z < W; z++) {
                    if(b[x][y][z]!=0)
                        result.add(Tile.tiles[b[x][y][z]].getAABB(x+this.x*16,y,z+this.z*16));
                }
            }
        }
        result.add(new AABB(this.x,0,this.z,this.x+16,0,this.z+16));
        return result;
    }

    public ArrayList<AABB> getSelectionBox() {
        ArrayList<AABB> result = new ArrayList<>();
        byte[][][] b=blocks;

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                for (int z = 0; z < W; z++) {
                    result.add(Tile.tiles[b[x][y][z]].getAABB(x+this.x*16,y,z+this.z*16));
                }
            }
        }

        return result;
    }

    public double distanceTo(double x,double z){
        return Math.abs(this.x*16-x)*Math.abs(this.z*16-z);
    }

}

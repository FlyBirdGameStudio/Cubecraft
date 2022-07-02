package com.SunriseStudio.cubecraft.world;

import com.SunriseStudio.cubecraft.world.block.Tile;
import com.SunriseStudio.cubecraft.world.chunk._Chunk;
import com.SunriseStudio.cubecraft.world.entity._Entity;
import com.SunriseStudio.cubecraft.world.worldGen.WorldGen;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.SunriseStudio.cubecraft.util.math.AABB;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Level {
    private static final int LOADING_DISTANCE=9;

    private ArrayList<LevelListener> levelListeners = new ArrayList<>();
    public ArrayList<_Chunk> chunks =new ArrayList<>();
    public ArrayList<_Entity> entities =new ArrayList<>();

    private Random worldRandom;
    public String name;
    public long time;
    public long seed;
    private WorldGen worldGen;
    private int skyColor=0x81BDE9;
    private int voidColor=0x0A1772;

    public File toJson(){
        File jsonFile=new File("/world.json");
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        FileWriter fw = null;
        try {
            fw=new FileWriter(jsonFile);
            gson.toJson(this, fw);
            fw.close();
        } catch(Exception e) {

            if(fw!=null) {
                try {
                    fw.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return jsonFile;
    }


    public Level(long seed){
        this.worldGen=new WorldGen(this,seed);
    }

    public Level(File f){

    }

    public void tick(){
        for(_Entity e:entities){
            loadWorld(e);
            e.tick();
        }
        time++;
    }
    //chunk operation

    public void loadWorld(_Entity p){
        long playerCX = (long) (p.x / 16);
        long playerCZ = (long) (p.z / 16);

        for (long cx = playerCX - LOADING_DISTANCE; cx <= playerCX + LOADING_DISTANCE; cx++) {
            for (long cz = playerCZ - LOADING_DISTANCE; cz <= playerCZ + LOADING_DISTANCE; cz++) {
                if (getChunk(cx, cz) == null) {
                    chunks.add(this.worldGen.generateChunk(cx,cz));
                }
            }
        }
        Iterator<_Chunk> remover = chunks.iterator();
        while (remover.hasNext()) {
            _Chunk c = remover.next();

            if (c.distanceTo(p.x, p.z) > (LOADING_DISTANCE)*LOADING_DISTANCE * 256) {
                remover.remove();
            }
        }
    }



    public _Chunk getChunk(long x, long z){
        _Chunk result = null;
        ArrayList<_Chunk> chunks = new ArrayList<>(this.chunks);
        for (_Chunk c : chunks) {
            if(c!=null) {
                if (c.x == x && c.z == z) {
                    result = c;
                }
            }
        }

        return result;

    }

    public void setChunk(long x, long z, _Chunk chunk){
        for (_Chunk c : chunks) {
            if (c.x == x && c.z == z) {
                c=chunk;
            }
        }
    }


    //physic operation

    public static final int PHYSICAL_LOADING_DISTANCE=4;

    public ArrayList<AABB> getCollisionBox(AABB box){
        ArrayList<AABB> result=new ArrayList<>();
        for (long i = (long)box.x0-PHYSICAL_LOADING_DISTANCE; i < (long)box.x1+PHYSICAL_LOADING_DISTANCE; i++) {
            for (int j = (int)box.y0-PHYSICAL_LOADING_DISTANCE; j < (int)box.y1+PHYSICAL_LOADING_DISTANCE; j++) {
                for (long k = (long)box.z0-PHYSICAL_LOADING_DISTANCE; k < (long)box.z1+PHYSICAL_LOADING_DISTANCE; k++) {
                    if (this.getTile(i,j,k)!=0) {
                        result.add(Tile.tiles[this.getTile(i, j, k)].getAABB(i, j, k));
                    }
                }
            }
        }
        result.add(new AABB(box.x0,-100,box.z0,box.x1,0,box.z1));
        return result;
    }

    public ArrayList<AABB> getSelectionBox(AABB box){
        ArrayList<AABB> result=new ArrayList<>();
        for (_Chunk c:chunks){
            if(c.distanceTo(box.x0,box.z0)<=2048)
                result.addAll(c.getSelectionBox());
        }

        return result;
    }

    public boolean containsAnyLiquid(AABB box) {
        int x0 = (int)Math.floor(box.x0);
        int x1 = (int)Math.floor(box.x1 + 1.0f);
        int y0 = (int)Math.floor(box.y0);
        int y1 = (int)Math.floor(box.y1 + 1.0f);
        int z0 = (int)Math.floor(box.z0);
        int z1 = (int)Math.floor(box.z1 + 1.0f);
        for (int x = x0; x < x1; ++x) {
            for (int y = y0; y < y1; ++y) {
                for (int z = z0; z < z1; ++z) {
                    Tile tile = Tile.tiles[this.getTile(x, y, z)];
                    if (tile == null || tile.getLiquidType() <= 0) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsLiquid(AABB box, int liquidId) {
        long x0 = (long)Math.floor(box.x0);
        long x1 = (long)Math.floor(box.x1 + 1.0f);
        long y0 = (long)Math.floor(box.y0);
        long y1 = (long)Math.floor(box.y1 + 1.0f);
        long z0 = (long)Math.floor(box.z0);
        long z1 = (long)Math.floor(box.z1 + 1.0f);
        for (long x = x0; x < x1; ++x) {
            for (long y = y0; y < y1; ++y) {
                for (long z = z0; z < z1; ++z) {
                    Tile tile = Tile.tiles[this.getTile(x, y, z)];
                    if (tile == null || tile.getLiquidType() != liquidId) continue;
                    return true;
                }
            }
        }
        return false;
    }


    //listener operation

    public void addListener(LevelListener levelListener) {
        this.levelListeners.add(levelListener);
    }

    public void removeListener(LevelListener levelListener) {
        this.levelListeners.remove(levelListener);
    }

    //block operation

    public boolean setTile(long x, int y, long z, int type) {

        this.getChunk(x/16,z/16).setBlock(Math.toIntExact(x % _Chunk.W), y, Math.toIntExact(z % _Chunk.W), (byte) type);
        for (int i = 0; i < this.levelListeners.size(); ++i) {
            this.levelListeners.get(i).tileChanged(x, y, z);
        }
        return true;
    }

    public boolean setTileNoUpdate(long x, int y, long z, int type) {
        setTile(x,y,z,type);
        return true;
    }

    public boolean isLit(long x, long y, long z) {
        return true;
    }

    public int getTile(long x, long y, long z){
        long cx=x/16;
        long cz=z/16;
        long absX=x;
        long absZ=z;
        if(x<0){
            cx-=1;
        }
        if(z<0){
            cz-=1;
        }

        if(getChunk(cx,cz)!=null) {
            return getChunk(cx,cz).getBlock((int)(absX -cx*16), Math.toIntExact(y), (int)(absZ -cz*16));
        }else{
            return 0;
        }

    }

    public boolean isSolidTile(long x, long y, long z) {
        if(getTile(x,y,z)==0)return false;
        return Tile.tiles[getTile(x, y, z)].isSolid();
    }



    public void init(){
        toJson();
    }

    public float[] fogColor=new float[]{223f/255f,245f/255f,1,0};
    public float[] getFogColor() {
        return fogColor;
    }

    public int getFogColorI(){
        return 0x9acEE9;
    }

    public int getSkyColor() {
        return skyColor;
    }

    public int getVoidColor() {
        return voidColor;
    }
}

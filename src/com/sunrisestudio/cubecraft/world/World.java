package com.sunrisestudio.cubecraft.world;

import com.sunrisestudio.cubecraft.Registry;
import com.sunrisestudio.cubecraft.world.block.BlockState;
import com.sunrisestudio.cubecraft.world.block.BlockFacing;
import com.sunrisestudio.cubecraft.world.chunk.Chunk;
import com.sunrisestudio.cubecraft.world.chunk.ChunkLoadLevel;
import com.sunrisestudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.sunrisestudio.cubecraft.world.chunk.ChunkPos;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;
import com.sunrisestudio.cubecraft.world.worldGen.WorldProvider;
import com.sunrisestudio.util.container.CollectionUtil;
import com.sunrisestudio.util.container.HashMapSet;
import com.sunrisestudio.util.math.*;

import org.joml.Vector3d;

import java.util.*;

public class World implements IWorldAccess {
    public HashMapSet<ChunkPos, Chunk> chunks = new HashMapSet<>();
    public HashMap<String, Entity> entities = new HashMap<>();
    private  LevelInfo levelInfo;
    private long time;


    public World(LevelInfo levelInfo) {
        this.levelInfo = levelInfo;
    }

    //physics
    @Override
    public ArrayList<AABB> getCollisionBox(AABB box) {
        ArrayList<AABB> result = new ArrayList<>();
        for (long i = (long) box.x0 - 4; i < (long) box.x1 + 4; i++) {
            for (long j = (long) box.y0 - 4; j < (long) box.y1 + 4; j++) {
                for (long k = (long) box.z0 - 4; k < (long) box.z1 + 4; k++) {
                    AABB[] collisionBoxes = this.getBlock(i, j, k).getCollisionBox(i, j, k);
                    if (collisionBoxes != null) {
                        result.addAll(CollectionUtil.pack(collisionBoxes));
                    }
                }
            }
        }
        return result;
    }

    @Override
    public ArrayList<AABB> getSelectionBox(Entity from) {
        ArrayList<AABB> result = new ArrayList<>();
        for (long i = (long) ((long) from.x - from.getReachDistance()); i < (long) from.x + from.getReachDistance(); i++) {
            for (long j = (long) ((long) from.y - from.getReachDistance()); j < (long) from.y + from.getReachDistance(); j++) {
                for (long k = (long) ((long) from.z - from.getReachDistance()); k < (long) from.z + from.getReachDistance(); k++) {
                    HitBox[] collisionBoxes = this.getBlock(i, j, k).getSelectionBox(this, i, j, k);
                    if (collisionBoxes != null) {
                        result.addAll(CollectionUtil.pack(collisionBoxes));
                    }
                }
            }
        }
        /*
        for (Entity e : this.entities.values()) {
            if (
                    Math.abs(e.x - from.x) < e.getReachDistance() + 1 &&
                            Math.abs(e.y - from.y) < e.getReachDistance() + 1 &&
                            Math.abs(e.z - from.z) < e.getReachDistance() + 1
            ) {
                result.addAll(List.of(e.getSelectionBoxes()));
            }
        }

         */
        return result;
    }

    @Override
    public String getID() {
        return "cubecraft:over_world";
    }

    @Override
    public Level getLevel() {
        return this.levelInfo.level();
    }

    @Override
    public long getSeed() {
        return 0;
    }

    @Override
    public void tick() {
        time++;
        Iterator<Chunk> it = this.chunks.map.values().iterator();
        try {
            while (it.hasNext()) {
                Chunk chunk = it.next();
                chunk.ticket.tickTime();
                if (chunk.ticket.getTime() <= 0) {
                    it.remove();
                }
            }
        }catch (ConcurrentModificationException fuckyouasync){
            //remove has the lowest priority,so ignore this exception.
        }
        Iterator<Entity> it2 = this.entities.values().iterator();
        while (it2.hasNext()) {
            Entity e = it2.next();
            if (e instanceof Player) {
                e.tick();
                this.loadChunkRange((long) (e.x) / 16, (long) (e.y) / 16, (long) (e.z) / 16, 3, 50);
            } else {
                Chunk c = this.getChunk(new ChunkPos((long) (e.x) / 16, (long) (e.y) / 16, (long) (e.z) / 16));
                if (c.ticket.getTime() > 0 && c.ticket.getChunkLoadLevel().containsLevel(ChunkLoadLevel.Entity_TICKING)) {
                    e.tick();
                } else {
                    it2.remove();
                }
            }
        }
    }

    @Override
    public Chunk getChunk(ChunkPos p) {
        return this.chunks.get(p);
    }

    @Override
    public Chunk getChunk(long cx, long cy, long cz) {
        return this.getChunk(new ChunkPos(cx, cy, cz));
    }

    @Override
    public void loadChunk(ChunkPos p, ChunkLoadTicket ticket) {
        if (getChunk(p) == null) {
            chunks.add(WorldProvider.getProvider(this).loadChunk(p));
        }
        getChunk(p).addTicket(ticket);
    }

    @Override
    public void loadChunk(long cx, long cy, long cz, ChunkLoadTicket chunkLoadTicket) {
        this.loadChunk(new ChunkPos(cx, cy, cz), chunkLoadTicket);
    }

    @Override
    public void loadChunkRange(long centerCX, long centerCY, long centerCZ, int range, int ticks) {
        for (long x = centerCX - range; x <= centerCX + range; x++) {
            for (long y = centerCY - range; y <= centerCY + range; y++) {
                for (long z = centerCZ - range; z <= centerCZ + range; z++) {
                    loadChunk(x, y, z, ChunkLoadTicket.fromDistance(range, ticks));
                }
            }
        }
    }

    @Override
    public long getTime() {
        return this.time;
    }

    public Vector3d getSpawnPos() {
        return new Vector3d(0, 35, 0);
    }

    @Override
    public void spawnEntity(String id, double x, double y, double z) {
        Entity e = Registry.getEntityMap().get(id);
        assert e != null;
        e.setPos(x, y, z);
        this.addEntity(e);
    }

    @Override
    public void addEntity(Entity e) {
        if (!this.entities.containsKey(e.getUID())) {
            this.entities.put(e.getUID(), e);
            e.setWorld(World.this);
            this.loadChunk((long) e.x / 16, (long) (e.y / 16), (long) (e.z / 16), new ChunkLoadTicket(ChunkLoadLevel.Entity_TICKING, 256));
        } else {
            throw new RuntimeException("conflict entity uuid:" + e.getUID());
        }
    }

    @Override
    public Collection<Entity> getAllEntities() {
        return this.entities.values();
    }

    @Override
    public Entity getEntity(String uid) {
        return this.entities.get(uid);
    }

    @Override
    public void removeEntity(String uid) {
        this.entities.remove(uid);
    }

    @Override
    public void removeEntity(Entity e) {
        this.entities.remove(e.getUID());
    }

    @Override
    public BlockState getBlock(long x, long y, long z) {
        ChunkPos chunkPos = ChunkPos.fromWorldPos(x, y, z);
        if (getChunk(chunkPos) == null) {
            return new BlockState("cubecraft:air");
        }

        return getChunk(chunkPos).getBlock(
                (int)MathHelper.getRelativePosInChunk(x,16),
                (int)MathHelper.getRelativePosInChunk(y,16),
                (int)MathHelper.getRelativePosInChunk(z,16)
        );
    }

    @Override
    public void setBlock(long x, long y, long z, String id, BlockFacing facing) {
        setBlockNoUpdate(x, y, z, id, facing);
        getBlock(x, y, z).setTicking(true);
        getBlock(x + 1, y, z).setTicking(true);
        getBlock(x - 1, y, z).setTicking(true);
        getBlock(x, y + 1, z).setTicking(true);
        getBlock(x, y - 1, z).setTicking(true);
        getBlock(x, y, z + 1).setTicking(true);
        getBlock(x, y, z - 1).setTicking(true);
    }

    @Override
    public void setBlockNoUpdate(long x, long y, long z, String id, BlockFacing facing) {
        ChunkPos chunkPos = ChunkPos.fromWorldPos(x, y, z);
        if (getChunk(chunkPos) == null) {
            return;
        }
        getChunk(chunkPos).setBlock(
                (int)MathHelper.getRelativePosInChunk(x,16),
                (int)MathHelper.getRelativePosInChunk(y,16),
                (int)MathHelper.getRelativePosInChunk(z,16),
                id,
                facing
        );
    }

    @Override
    public void setUpdate(long x, long y, long z) {
        getBlock(x, y, z).setTicking(true);
    }

    @Override
    public WorldInfo getWorldInfo() {
        return new WorldInfo(0x81BDE9, 0x0A1772, 0xDCE9F5, 0xFFFFFF);
    }

    @Override
    public HashMapSet<ChunkPos, Chunk> getChunkCache() {
        return this.chunks;
    }
/*

    public RayTraceResult rayTraceBlocks(Vector3d vec31, Vector3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z)) {
            if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z)) {
                long i = (long) Math.floor(vec32.x);
                long j = (long) Math.floor(vec32.y);
                long k = (long) Math.floor(vec32.z);
                long l = (long) Math.floor(vec31.x);
                long i1 = (long) Math.floor(vec31.y);
                long j1 = (long) Math.floor(vec31.z);

                if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(this, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid)) {
                    RayTraceResult raytraceresult = iblockstate.collisionRayTrace(this, blockpos, vec31, vec32);

                    if (raytraceresult != null) {
                        return raytraceresult;
                    }
                }

                RayTraceResult raytraceresult2 = null;
                int k1 = 200;

                while (k1-- >= 0) {
                    if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z)) {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k) {
                        return returnLastUncollidableBlock ? raytraceresult2 : null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l) {
                        d0 = (double) l + 1.0D;
                    } else if (i < l) {
                        d0 = (double) l + 0.0D;
                    } else {
                        flag2 = false;
                    }

                    if (j > i1) {
                        d1 = (double) i1 + 1.0D;
                    } else if (j < i1) {
                        d1 = (double) i1 + 0.0D;
                    } else {
                        flag = false;
                    }

                    if (k > j1) {
                        d2 = (double) j1 + 1.0D;
                    } else if (k < j1) {
                        d2 = (double) j1 + 0.0D;
                    } else {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.x - vec31.x;
                    double d7 = vec32.y - vec31.y;
                    double d8 = vec32.z - vec31.z;

                    if (flag2) {
                        d3 = (d0 - vec31.x) / d6;
                    }

                    if (flag) {
                        d4 = (d1 - vec31.y) / d7;
                    }

                    if (flag1) {
                        d5 = (d2 - vec31.z) / d8;
                    }

                    if (d3 == -0.0D) {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D) {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D) {
                        d5 = -1.0E-4D;
                    }

                    BlockFacing enumfacing;

                    if (d3 < d4 && d3 < d5) {
                        enumfacing = i > l ? BlockFacing.West: BlockFacing.East;
                        vec31 = new Vector3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
                    } else if (d4 < d5) {
                        enumfacing = j > i1 ? BlockFacing.Down : BlockFacing.Up;
                        vec31 = new Vector3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
                    } else {
                        enumfacing = k > j1 ? BlockFacing.North : BlockFacing.South;
                        vec31 = new Vector3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
                    }

                    l = (long) (Math.floor(vec31.x) - (enumfacing == BlockFacing.East ? 1 : 0));
                    i1 = (long) (Math.floor(vec31.y) - (enumfacing ==BlockFacing.Up ? 1 : 0));
                    j1 = (long) (Math.floor(vec31.z) - (enumfacing == BlockFacing.South ? 1 : 0));


                    if (!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(this, blockpos) != Block.NULL_AABB) {
                        if (block1.canCollideCheck(iblockstate1, stopOnLiquid)) {
                            RayTraceResult raytraceresult1 = iblockstate1.collisionRayTrace(this, blockpos, vec31, vec32);

                            if (raytraceresult1 != null) {
                                return raytraceresult1;
                            }
                        } else {
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
                        }
                    }
                }

                return returnLastUncollidableBlock ? raytraceresult2 : null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

     */

    public HitResult rayTraceBlocks( Vector3d from,  Vector3d dest) {
        RayInterSection interSection=new RayInterSection(from,dest);



        /*
        //check if nan
        if (Double.isNaN(from.x) || Double.isNaN(from.y) || Double.isNaN(from.z)) {
            return null;
        }
        if (Double.isNaN(dest.x) || Double.isNaN(dest.y) || Double.isNaN(dest.z)) {
            return null;
        }

        //floor position
        long destFloorX = MathHelper.floor(dest.x);
        long destFloorY = MathHelper.floor(dest.y);
        long destFloorZ = MathHelper.floor(dest.z);

        long blockX = MathHelper.floor(from.x);
        long blockY = MathHelper.floor(from.y);
        long blockZ = MathHelper.floor(from.z);

        //try
        for (int n=0;n<20;n++) {
            if (Double.isNaN(from.x) ||Double.isNaN(from.y) || Double.isNaN(from.z)) {
                return null;
            }
            if (blockX == destFloorX && blockY == destFloorY && blockZ == destFloorZ) {
                return null;
            }
            double xCoord = 999.0f;
            double yCoord = 999.0f;
            double zCoord = 999.0f;
            if (destFloorX > blockX) {
                xCoord = blockX + 1.0f;
            }
            if (destFloorX < blockX) {
                xCoord = (float)blockX;
            }
            if (destFloorY > blockY) {
                yCoord = blockY + 1.0f;
            }
            if (destFloorY < blockY) {
                yCoord = (float)blockY;
            }
            if (destFloorZ > blockZ) {
                zCoord = blockZ + 1.0f;
            }
            if (destFloorZ < blockZ) {
                zCoord = (float)blockZ;
            }
            double x = 999.0f;
            double y = 999.0f;
            double z = 999.0f;
            double xd = dest.x - from.x;
            double yd = dest.y - from.y;
            double zd = dest.z - from.z;
            if (xCoord != 999.0f) {
                x = (xCoord - from.x) / xd;
            }
            if (yCoord != 999.0f) {
                y = (yCoord - from.y) / yd;
            }
            if (zCoord != 999.0f) {
                z = (zCoord - from.z) / zd;
            }
            int n8;
            
            
            //clip
            if (x < y && x < z) {
                if (destFloorX > blockX) {
                    n8 = 4;
                }
                else {
                    n8 = 5;
                }
                from.x = xCoord;
                from.y += yd * x;
                from.z += zd * x;
            } else if (y < z) {
                if (destFloorY > blockY) {
                    n8 = 0;
                }
                else {
                    n8 = 1;
                }
                from.x += xd * y;
                from.y = yCoord;
                from.z += zd * y;
            } else {
                if (destFloorZ > blockZ) {
                    n8 = 2;
                }
                else {
                    n8 = 3;
                }
                from.x += xd * z;
                from.y += yd * z;
                from.z = zCoord;
            }

            Vector3d vec3D4 = new Vector3d(from.x, from.y, from.z);

            //x axis
            double xCoord2 = MathHelper.floor(from.x);
            vec3D4.x = xCoord2;
            blockX = (long) xCoord2;
            if (n8 == 5) {
                --blockX;
                ++vec3D4.x;
            }

            //y axis
            float yCoord2 = (float)MathHelper.floor(from.y);
            vec3D4.y = yCoord2;
            blockY = (int)yCoord2;
            if (n8 == 1) {
                --blockY;
                ++vec3D4.y;
            }

            //z axis
            float zCoord2 = (float)MathHelper.floor(from.z);
            vec3D4.z = zCoord2;
            blockZ = (int)zCoord2;
            if (n8 == 3) {
                --blockZ;
                ++vec3D4.z;
            }

            AABB[] aabb =getBlock(blockX,blockY,blockZ).getSelectionBox(this,blockX,blockY,blockZ);

            Arrays.sort(aabb, (o1, o2) -> o1.getCenter().distance(from)>o2.getCenter().distance(from)?1:0);

            if(aabb.length>0) {
                return aabb[0].onRayTrace(from, dest, blockX, blockY, blockZ);
            }
        }
        return null;

         */
        return null;
    }

    @Override
    public void loadChunkAndNear(long x, long y, long z, ChunkLoadTicket ticket) {
        loadChunkIfNull(x,y,z,ticket);
        loadChunkIfNull(x-1,y,z,ticket);
        loadChunkIfNull(x+1,y,z,ticket);
        loadChunkIfNull(x,y-1,z,ticket);
        loadChunkIfNull(x,y+1,z,ticket);
        loadChunkIfNull(x,y,z-1,ticket);
        loadChunkIfNull(x,y,z+1,ticket);

    }

    public void loadChunkIfNull(long x,long y,long z,ChunkLoadTicket ticket){
        if (getChunk(x, y, z) == null) {
            loadChunk(new ChunkPos(x, y, z), ticket);
        }
    }
}

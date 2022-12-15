package io.flybird.cubecraft.world;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.entity.EntityLocation;
import io.flybird.util.file.FileUtil;

import java.util.Date;
import java.util.HashMap;

public class Level {
    private final String name;
    public final HashMap<String,IWorld> dims = new HashMap<>();
    private final LevelInfo levelInfo;
    private EntityLocation getEntityLocation;

    public Level(String name, GameSetting setting) {
        this.name = name;
        for (IWorldProvider provider: Registries.WORLD_PROVIDER.itemList()){
            IWorld world=provider.createServerWorld(this,setting);
            this.dims.put(world.getID(),world);
        }
        this.levelInfo=new LevelInfo(this.name,"null",114514L,new Date(System.currentTimeMillis()),false);
    }

    public String getName() {
        return this.name;
    }

    public long getSeed() {
        return 0;
    }

    public void createLevelFolder() {
        FileUtil.createFileRelative("/data/saves/%s/level.nbt".formatted(this.name));
        for (IWorld world : this.dims.values()) {
            FileUtil.createFileRelative("/data/saves/%s/%s/chunk.ldb".formatted(this.name, world.getID().replace(":","_")));
            FileUtil.createFileRelative("/data/saves/%s/%s/entity.ldb".formatted(this.name, world.getID().replace(":","_")));
        }
    }

    public EntityLocation getSpawnPoint(String uid){
        return new EntityLocation(0,1024,0,0,0,0,"cubecraft:overworld");
    }

    public LevelInfo getInfo() {
        return this.levelInfo;
    }

    public HashMap<String, IWorld> getDims() {
        return dims;
    }
}

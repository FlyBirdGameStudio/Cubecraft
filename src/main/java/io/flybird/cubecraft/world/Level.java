package io.flybird.cubecraft.world;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.register.ContentRegistry;
import io.flybird.util.container.CollectionUtil;
import io.flybird.util.file.FileUtil;

import java.util.HashSet;

public class Level {
    private final String name;
    public HashSet<IWorld> dims = new HashSet<>();
    private LevelInfo levelInfo;

    public Level(String name, GameSetting setting) {
        this.name = name;
        for (IWorldProvider provider:ContentRegistry.getWorldProviderMap().itemList()){
            this.dims.add(provider.createServerWorld(this,setting));
        }
        this.levelInfo=new LevelInfo(this.name,"null",114514L,null,false,this);
    }

    public String getName() {
        return this.name;
    }

    public long getSeed() {
        return 0;
    }

    public void createLevelFolder() {
        FileUtil.createFileRelative("/data/saves/%s/level.nbt".formatted(this.name));
        for (IWorld world : this.dims) {
            FileUtil.createFileRelative("/data/saves/%s/%s/chunk.ldb".formatted(this.name, world.getID().replace(":","_")));
            FileUtil.createFileRelative("/data/saves/%s/%s/entity.ldb".formatted(this.name, world.getID().replace(":","_")));
        }
    }

    public LevelInfo getInfo() {
        return this.levelInfo;
    }
}

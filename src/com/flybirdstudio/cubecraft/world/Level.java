package com.flybirdstudio.cubecraft.world;

import java.util.HashSet;

public class Level {
    public HashSet<IWorld> dims=new HashSet<>();
    private String name;
    private long createTime;

    public Level(String name) {
        this.name=name;
        this.createTime=System.currentTimeMillis();
    }

    public String getName() {
        return this.name;
    }

    public long getSeed() {
        return 0;
    }
}

package com.sunrisestudio.cubecraft.world;

import com.sunrisestudio.cubecraft.world.access.IWorldAccess;

import java.util.HashSet;

public class Level {
    public HashSet<IWorldAccess> dims=new HashSet<>();
    private String name;
    private long createTime;

    public Level(String name) {
        this.name=name;
        this.createTime=System.currentTimeMillis();
    }

    public String getName() {
        return this.name;
    }
}

package io.flybird.cubecraft.world;

import io.flybird.util.file.nbt.NBTDataIO;
import io.flybird.util.file.nbt.tag.NBTTagCompound;

import java.util.Date;

public class LevelInfo implements NBTDataIO {
    private String name;
    private String creator;
    private long seed;
    private Date createDate;
    private boolean cheat;

    public LevelInfo(String name, String creator, long seed, Date createDate, boolean cheat) {
        this.name = name;
        this.creator = creator;
        this.seed = seed;
        this.createDate = createDate;
        this.cheat = cheat;
    }

    @Override
    public NBTTagCompound getData() {
        NBTTagCompound tag=new NBTTagCompound();
        tag.setString("name",name);
        tag.setString("creator",creator);
        tag.setLong("seed",seed);
        tag.setLong("date",createDate.getTime());
        tag.setBoolean("cheat",cheat);
        return tag;
    }

    @Override
    public void setData(NBTTagCompound tag) {
        name= tag.getString("name");
        creator=tag.getString("creator");
        seed=tag.getLong("seed");
        createDate=new Date(tag.getLong("date"));
        cheat=tag.getBoolean("cheat");
    }
}
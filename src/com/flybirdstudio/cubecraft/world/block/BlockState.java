package com.flybirdstudio.cubecraft.world.block;

import com.flybirdstudio.cubecraft.Registry;
import com.flybirdstudio.cubecraft.world.HittableObject;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.material.Block;
import com.flybirdstudio.cubecraft.world.entity.Entity;
import com.flybirdstudio.util.file.nbt.NBTDataIO;
import com.flybirdstudio.util.file.nbt.tag.NBTTagCompound;
import com.flybirdstudio.util.math.AABB;
import com.flybirdstudio.util.math.HitBox;
import com.flybirdstudio.util.math.HitResult;

public class BlockState implements NBTDataIO, HittableObject {
    private byte facing;
    private String id;
    private String biome = "cubecraft:plains";
    private NBTTagCompound blockMeta;
    public BlockState(String id) {
        this.id = id;
    }


    //data
    public String getId() {
        return id;
    }

    public BlockState setId(String id) {
        this.id = id;
        return this;
    }

    public BlockState setFacing(EnumFacing f) {
        this.facing = f.numID;
        return this;
    }

    public BlockState setFacing(byte f) {
        this.facing = f;
        return this;
    }

    public EnumFacing getFacing() {
        return EnumFacing.fromId(this.facing);
    }

    public Block getBlock() {
        return Registry.getBlockMap().get(this.id);
    }

    public NBTTagCompound getBlockMeta() {
        return blockMeta;
    }

    public void setBlockMeta(NBTTagCompound blockMeta) {
        this.blockMeta = blockMeta;
    }

    public void setBiome(String id) {
        this.biome = id;
    }

    public String getBiome() {
        return biome;
    }


    //physic
    public AABB[] getCollisionBox(long x, long y, long z) {
        return this.getBlock().getCollisionBox(x, y, z);
    }

    public HitBox[] getSelectionBox(long x, long y, long z) {
        return this.getBlock().getSelectionBox(x, y, z, this);
    }


    //io
    @Override
    public NBTTagCompound getData() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setByte("facing", this.facing);
        compound.setString("id", this.id);
        compound.setCompoundTag("meta", this.blockMeta);
        return compound;
    }

    @Override
    public void setData(NBTTagCompound compound) {
        this.facing = compound.getByte("facing");
        this.id = compound.getString("id");
        this.blockMeta = compound.getCompoundTag("meta");
    }


    //hit
    @Override
    public void onHit(Entity from, IWorld world, HitResult hr) {
        this.getBlock().onHit(
                from, world,
                (long) hr.aabb().getPosition().x,
                (long) hr.aabb().getPosition().y,
                (long) hr.aabb().getPosition().z,
                hr.facing()
        );
    }

    @Override
    public void onInteract(Entity from, IWorld world, HitResult hr) {
        this.getBlock().onInteract(
                from, world,
                (long) hr.aabb().getPosition().x,
                (long) hr.aabb().getPosition().y,
                (long) hr.aabb().getPosition().z,
                hr.facing()
        );
    }
}
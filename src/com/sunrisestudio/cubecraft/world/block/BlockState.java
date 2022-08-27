package com.sunrisestudio.cubecraft.world.block;

import com.sunrisestudio.cubecraft.client.render.model.RenderType;
import com.sunrisestudio.cubecraft.registery.Registry;
import com.sunrisestudio.cubecraft.world.HittableObject;
import com.sunrisestudio.cubecraft.world.World;
import com.sunrisestudio.cubecraft.world.block.material.Block;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;
import com.sunrisestudio.util.file.nbt.NBTDataIO;
import com.sunrisestudio.util.file.nbt.tag.NBTTagCompound;
import com.sunrisestudio.util.math.AABB;
import com.sunrisestudio.util.math.HitBox;
import com.sunrisestudio.util.math.HitResult;

public class BlockState implements NBTDataIO, HittableObject {
    private byte facing;
    private String id;
    private String biome="cubecraft:plains";
    private NBTTagCompound blockMeta;


    public String getId() {
        return id;
    }

    public BlockState setId(String id) {
        this.id = id;
        return this;
    }

    public BlockState setFacing(BlockFacing f) {
        this.facing = f.numID;
        return this;
    }

    public BlockState setFacing(byte f) {
        this.facing = f;
        return this;
    }


    public BlockFacing getFacing() {
        return BlockFacing.fromId(this.facing);
    }


    public String[] getTags() {
        return this.getBlock().getTags();
    }

    public Block getBlock() {
        return Registry.getBlockMap().get(this.id);
    }


    public BlockState(String id) {
        this.id = id;
    }


    public AABB[] getCollisionBox(long x, long y, long z) {
        return this.getBlock().getCollisionBox(x, y, z);
    }

    public HitBox[] getSelectionBox(long x, long y, long z) {
        return this.getBlock().getSelectionBox( x, y, z, this);
    }

    /**
     * defines a resistance of a block. Entity`s speed will multiply this value.
     *
     * @return value
     */
    public float getResistance() {
        return getBlock().getResistance();
    }

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

    public void render(World world, long x, long y, long z, long renderX, long renderY, long renderZ, IVertexArrayBuilder builder, RenderType renderType) {
        this.getBlock().render(world, x, y, z, renderX, renderY, renderZ, getFacing(), builder);
    }

    //test
    @Override
    public void onHit(Entity from, World world, HitResult hr) {
        this.getBlock().onHit(
                from, world,
                (long) hr.aabb().getPosition().x,
                (long) hr.aabb().getPosition().y,
                (long) hr.aabb().getPosition().z,
                hr.facing()
        );
    }

    @Override
    public void onInteract(Entity from, World world, HitResult hr) {
        this.getBlock().onInteract(
                from, world,
                (long) hr.aabb().getPosition().x,
                (long) hr.aabb().getPosition().y,
                (long) hr.aabb().getPosition().z,
                hr.facing()
        );
    }

    public void setBiome(String id) {
        this.biome=id;
    }

    public String getBiome() {
        return biome;
    }
}
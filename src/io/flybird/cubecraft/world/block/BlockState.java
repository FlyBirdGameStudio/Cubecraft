package io.flybird.cubecraft.world.block;

import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.world.HittableObject;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.util.file.nbt.NBTDataIO;
import io.flybird.util.file.nbt.tag.NBTTagCompound;
import io.flybird.util.math.AABB;
import io.flybird.util.math.HitBox;
import io.flybird.util.math.HitResult;

public class BlockState implements NBTDataIO, HittableObject {
    private byte facing;
    private String id;
    private byte meta;

    public BlockState(String id,byte facing,byte meta) {
        this.id = id;
        this.meta=meta;
        this.facing=facing;
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

    public byte getMeta() {
        return meta;
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
        return compound;
    }

    @Override
    public void setData(NBTTagCompound compound) {
        this.facing = compound.getByte("facing");
        this.id = compound.getString("id");
        this.meta = 0;
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

    public void tick(IWorld world,long x,long y,long z){
        this.getBlock().onBlockUpdate(world, x, y, z);
    }
}
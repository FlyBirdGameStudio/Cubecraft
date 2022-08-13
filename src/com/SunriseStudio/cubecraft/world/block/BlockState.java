package com.sunrisestudio.cubecraft.world.block;

import com.sunrisestudio.cubecraft.registery.Registry;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;
import com.sunrisestudio.util.math.*;
import com.sunrisestudio.util.file.nbt.NBTDataIO;
import com.sunrisestudio.util.file.nbt.tag.NBTTagCompound;
import com.sunrisestudio.cubecraft.world.HittableObject;
import com.sunrisestudio.cubecraft.world.block.material.Block;
import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.cubecraft.world.entity.Entity;

public class BlockState implements NBTDataIO ,HittableObject {
	BlockFacing facing;
	private String id;
	private NBTTagCompound blockMeta;
	private String innerBlockId;
	private boolean needTick;
	private String biomeID;


	public BlockState(String id) {
		this.id=id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Block getMaterial() {
		return Registry.getBlockMap().get(this.id);
	}

	public AABB[] getCollisionBox(long x,long y,long z){
		return this.getMaterial().getCollisionBox(x,y,z);
	}

	public String[] getTags() {
		return this.getMaterial().getTags();
	}

	public void setFacing(BlockFacing f) {
		this.facing=f;
	}

	public BlockFacing getFacing() {
		return facing;
	}

    public HitBox[] getSelectionBox(IWorldAccess world, long x, long y, long z) {
		return this.getMaterial().getSelectionBox(world,x,y,z,this);
    }

	/**
	 * defines a resistance of a block.entity`s speed will multiply this value.
	 * @return value
	 */
	public float getResistance() {
		return 1.0f;
	}

	@Override
	public NBTTagCompound getData(){
		NBTTagCompound compound=new NBTTagCompound();
		compound.setByte("facing",this.facing.getNumID());
		compound.setString("id",this.id);
		compound.setString("innerBlock",this.innerBlockId);
		compound.setCompoundTag("meta",this.blockMeta);
		return compound;
	}

	@Override
	public void setData(NBTTagCompound compound){
		this.facing=BlockFacing.fromId(compound.getByte("facing"));
		this.id=compound.getString("id");
		this.innerBlockId=compound.getString("innerBlock");
		this.blockMeta=compound.getCompoundTag("meta");
	}

    public boolean needTick() {
		return this.needTick;
    }

	public void setTicking(boolean b) {
		this.needTick=b;
	}

	public void render(IWorldAccess world,long x,long y,long z,long renderX, long renderY, long renderZ, IVertexArrayBuilder builder){
		getMaterial().render(world,x,y,z,renderX,renderY,renderZ,facing,builder);
	}

	//test
	@Override
	public void onHit(Entity from, IWorldAccess world, HitResult hr) {
		world.setBlock(
				(long) hr.aabb().getPosition().x,
				(long) hr.aabb().getPosition().y,
				(long) hr.aabb().getPosition().z,
				"cubecraft:air", BlockFacing.Up
		);
	}

	@Override
	public void onInteract(Entity from, IWorldAccess world, HitResult hr) {
		Vector3<Long> pos=BlockFacing.fromId(hr.facing()).findNear(
				(long) hr.aabb().getPosition().x,
				(long) hr.aabb().getPosition().y,
				(long) hr.aabb().getPosition().z,
				1
		);
		world.setBlock(pos.x(),pos.y(),pos.z(),"cubecraft:stone", BlockFacing.Up);
	}
}
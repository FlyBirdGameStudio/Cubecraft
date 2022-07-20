package com.sunrisestudio.cubecraft.world.block;

import com.sunrisestudio.cubecraft.world.Registry;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;
import com.sunrisestudio.util.math.AABB;
import com.sunrisestudio.util.nbt.NBTDataIO;
import com.sunrisestudio.util.nbt.NBTTagCompound;
import com.sunrisestudio.cubecraft.world.HittableObject;
import com.sunrisestudio.cubecraft.world.block.material.IBlockMaterial;
import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.cubecraft.world.entity.Entity;

public class Block implements HittableObject, NBTDataIO {
	final long x,y,z;
	BlockFacing facing;
	private String id;
	private byte blockMeta;
	private String innerBlockId;
	private boolean needTick;

	public Block(long x,long y,long z,String id) {
		this.x=x;
		this.y=y;
		this.z=z;
		this.id=id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public IBlockMaterial getMaterial() {
		return Registry.getBlockMap().get(this.id);
	}

	public AABB[] getCollisionBox(){
		return this.getMaterial().getCollisionBox(this.x,this.y,this.z);
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

    public AABB[] getSelectionBox() {
		return this.getMaterial().getSelectionBox(this.x,this.y,this.z);
    }

	@Override
	public void onHit(Entity from, IWorldAccess world) {
		world.setBlock(x,y,z,"air",BlockFacing.Up);
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
		compound.setByte("meta",this.blockMeta);
		return compound;
	}

	@Override
	public void setData(NBTTagCompound compound){
		this.facing=BlockFacing.fromId(compound.getByte("facing"));
		this.id=compound.getString("id");
		this.innerBlockId=compound.getString("innerBlock");
		this.blockMeta=compound.getByte("meta");
	}

    public boolean needTick() {
		return this.needTick;
    }

	public void setTicking(boolean b) {
		this.needTick=b;
	}

	public void render(IWorldAccess world,long renderX, long renderY, long renderZ, IVertexArrayBuilder builder){
		getMaterial().render(world,this.x,this.y,this.z,renderX,renderY,renderZ,facing,builder);
	}


}
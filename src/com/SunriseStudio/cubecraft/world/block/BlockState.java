package com.sunrisestudio.cubecraft.world.block;

import com.sunrisestudio.cubecraft.Registry;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;
import com.sunrisestudio.util.math.AABB;
import com.sunrisestudio.util.file.nbt.NBTDataIO;
import com.sunrisestudio.util.file.nbt.tag.NBTTagCompound;
import com.sunrisestudio.cubecraft.world.HittableObject;
import com.sunrisestudio.cubecraft.world.block.material.Block;
import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.util.math.HitBox;

public class BlockState implements HittableObject, NBTDataIO {
	BlockFacing facing;
	private String id;
	private byte blockMeta;
	private String innerBlockId;
	private boolean needTick;

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
		return this.getMaterial().getSelectionBox(world,x,y,z);
    }

	@Override
	public void onHit(Entity from, IWorldAccess world,long x,long y,long z) {
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

	public void render(IWorldAccess world,long x,long y,long z,long renderX, long renderY, long renderZ, IVertexArrayBuilder builder){
		getMaterial().render(world,x,y,z,renderX,renderY,renderZ,facing,builder);
	}


	/*
	public void trace(BlockPos pos, Vector3d start, Vector3d end, AxisAlignedBB boundingBox){
		Vector3d vec3d = start.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
		Vector3d vec3d1 = end.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
		RayTraceResult raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d1);
		return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.addVector((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), raytraceresult.sideHit, pos);
	}

	 */
}
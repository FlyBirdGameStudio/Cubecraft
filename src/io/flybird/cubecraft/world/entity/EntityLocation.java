package io.flybird.cubecraft.world.entity;

import io.flybird.util.file.nbt.NBTDataIO;
import io.flybird.util.file.nbt.tag.NBTTagCompound;

public class EntityLocation implements NBTDataIO {
    public double x,y,z,xRot,yRot,zRot;

    public EntityLocation(double x, double y, double z, double xRot, double yRot, double zRot){
        this.x=x;
        this.y=y;
        this.z=z;
        this.xRot=xRot;
        this.yRot=yRot;
        this.zRot=zRot;
    }

    @Override
    public NBTTagCompound getData() {
        NBTTagCompound compound=new NBTTagCompound();
        compound.setDouble("x",x);
        compound.setDouble("y",y);
        compound.setDouble("z",z);
        compound.setDouble("xRot",xRot);
        compound.setDouble("xRot",yRot);
        compound.setDouble("xRot",zRot);

        return compound;
    }

    @Override
    public void setData(NBTTagCompound tag) {
        x=tag.getDouble("x");
        y=tag.getDouble("y");
        z=tag.getDouble("z");
        xRot=tag.getDouble("xRot");
        yRot=tag.getDouble("xRot");
        zRot=tag.getDouble("xRot");
    }
}

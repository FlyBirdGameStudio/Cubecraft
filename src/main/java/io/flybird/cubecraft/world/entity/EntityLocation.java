package io.flybird.cubecraft.world.entity;

import io.flybird.util.file.NBTDataIO;
import io.flybird.util.file.nbt.NBTTagCompound;

public class EntityLocation implements NBTDataIO {
    private double x;
    private double y;
    private double z;
    private double xRot;
    private double yRot;
    private double zRot;
    private String dim;

    public EntityLocation(double x, double y, double z, double xRot, double yRot, double zRot, String dim){
        this.x=x;
        this.y=y;
        this.z=z;
        this.xRot=xRot;
        this.yRot=yRot;
        this.zRot=zRot;
        this.dim=dim;
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

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getXRot() {
        return xRot;
    }

    public void setXRot(double xRot) {
        this.xRot = xRot;
    }

    public double getYRot() {
        return yRot;
    }

    public void setYRot(double yRot) {
        this.yRot = yRot;
    }

    public double getZRot() {
        return zRot;
    }

    public void setZRot(double zRot) {
        this.zRot = zRot;
    }

    public String getDim() {
        return dim;
    }

    public void setDim(String dim) {
        this.dim = dim;
    }
}

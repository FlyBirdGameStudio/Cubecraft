package io.flybird.starfish3d.render;

import io.flybird.starfish3d.platform.Display;
import io.flybird.util.math.AABB;
import org.joml.*;
import org.lwjgl.opengl.GL11;

import java.lang.Math;

public class Camera {
    private float fov=20f;

    private final Vector3d position=new Vector3d();
    private final Vector3d rotation=new Vector3d();
    private final Vector3d relativePosition=new Vector3d();
    private Matrix4f proj= new Matrix4f();

    public void setUpGlobalCamera(){
        
        GLUtil.setupPerspectiveCamera(fov, Display.getWidth(),Display.getHeight());
        GL11.glTranslated(relativePosition.x,relativePosition.y,relativePosition.z);
        GL11.glRotated(rotation.x,1,0,0);
        GL11.glRotated(rotation.y,0,1,0);
        GL11.glRotated(rotation.z,0,0,1);

        proj=new Matrix4f();

        //proj.transform(new Vector4f((float) relativePosition.x,(float)relativePosition.y,(float)relativePosition.z,1.0f));

        proj.rotate(new Quaternionf(rotation.x,rotation.y, rotation.z,1.0f));
        proj.mul(new Matrix4f().perspective(this.fov,Display.getWidth()/(float)Display.getHeight(),0,114514));
    }

    public void setupObjectCamera(Vector3d objPosition){
        GL11.glTranslated(objPosition.x-position.x,objPosition.y-position.y,objPosition.z-position.z);
    }

    public boolean objectDistanceSmallerThan(Vector3d objPosition,double dist){
        double xd = Math.abs(this.position.x - objPosition.x);
        double yd = Math.abs(this.position.y - objPosition.y);
        double zd = Math.abs(this.position.z - objPosition.z);
        return xd*yd*zd<dist*dist*dist&&xd<dist&&yd<dist&&zd<dist;
    }

    public Vector3d getPosition() {
        return position;
    }
    public Vector3d getRelativePosition() {
        return relativePosition;
    }
    public Vector3d getRotation() {
        return rotation;
    }
    public void setPos(double x, double y, double z) {
        this.position.set(x,y,z);
    }
    public void setPosRelative(double x, double y, double z) {
        this.relativePosition.set(x,y,z);
    }
    public void setupRotation(double x,double y,double z){
        this.rotation.set(x,y,z);
    }

    public Matrix4f getCurrentMatrix() {
        return proj;
    }


    private long playerGridX,playerGridY,playerGridZ;
    private double lastRotX,lastRotY,lastRotZ;
    public void updatePosition() {
        this.playerGridX = (long) (this.getPosition().x / 8);
        this.playerGridY = (long) (this.getPosition().y / 8);
        this.playerGridZ = (long) (this.getPosition().z / 8);
    }

    public void updateRotation(){
        this.lastRotX = this.getRotation().x;
        this.lastRotY = this.getRotation().y;
        this.lastRotZ = this.getRotation().z;
    }

    public boolean isPositionChanged(){
       return  (long) (this.getPosition().x/8)!=this.playerGridX||
                (long) (this.getPosition().y/8)!=this.playerGridY||
                (long)(this.getPosition().z/8)!=this.playerGridZ;

    }

    public boolean isRotationChanged(){
        return (this.getRotation().x)!=this.lastRotX||
                (this.getRotation().y)!=this.lastRotY||
                (this.getRotation().z)!=this.lastRotZ;
    }

    public void setupGlobalTranslate() {
        GL11.glTranslated(-this.getPosition().x,-this.getPosition().y,-this.getPosition().z);
    }

    public AABB castAABB(AABB aabb) {
        AABB aabb2=new AABB(aabb);
        aabb2.move(-this.position.x,-this.position.y,-this.position.z);
        return aabb2;
    }
}

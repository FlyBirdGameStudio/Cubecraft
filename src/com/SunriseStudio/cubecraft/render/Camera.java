package com.SunriseStudio.cubecraft.render;

import com.SunriseStudio.cubecraft.util.grass3D.render.GLUtil;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;
import org.lwjglx.opengl.Display;

public class Camera {
    private float fov=70.0f;

    private final Vector3d position=new Vector3d();
    private final Vector3d rotation=new Vector3d();
    private final Vector3d relativePosition=new Vector3d();

    public void setUpGlobalCamera(){
        GLUtil.setupPerspectiveCamera(fov, Display.getWidth(),Display.getHeight());
        GL11.glTranslated(relativePosition.x,relativePosition.y,relativePosition.z);
        GL11.glRotated(rotation.x,1,0,0);
        GL11.glRotated(rotation.y,0,1,0);
        GL11.glRotated(rotation.z,0,0,1);
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
}

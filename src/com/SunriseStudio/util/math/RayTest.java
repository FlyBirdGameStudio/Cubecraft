package com.sunrisestudio.util.math;
import org.joml.Vector3d;
import java.util.ArrayList;
import java.util.Comparator;


public class RayTest {
    /**
     * test if a ray collide with projection from x-axis of an AxisAlignedBB
     * @param aabb AxisAlignedBB
     * @param from ray from
     * @param destination ray destination
     * @return projection collide?
     */
    public static boolean xProjectionCollide(AABB aabb,Vector3d from,Vector3d destination){
        double t0=MathHelper.reverse_interpolate_abs(from.x,destination.x,aabb.x0);
        double t1=MathHelper.reverse_interpolate_abs(from.x,destination.x,aabb.x1);

        double y0=MathHelper.linear_interpolate(destination.y,from.y,t0);
        double y1=MathHelper.linear_interpolate(destination.y,from.y,t1);
        double z0=MathHelper.linear_interpolate(destination.z,from.z,t0);
        double z1=MathHelper.linear_interpolate(destination.z,from.z,t1);

        return aabb.positionInBoundYZ(y0,z0)||aabb.positionInBoundYZ(y1,z1);
    }

    /**
     * test if a ray collide with projection from y-axis of an AxisAlignedBB
     * @param aabb AxisAlignedBB
     * @param from ray from
     * @param destination ray destination
     * @return projection collide?
     */
    public static boolean yProjectionCollide(AABB aabb,Vector3d from,Vector3d destination){
        double t0=MathHelper.reverse_interpolate_abs(from.y,destination.y,aabb.y0);
        double t1=MathHelper.reverse_interpolate_abs(from.y,destination.y,aabb.y1);

        double x0=MathHelper.linear_interpolate(destination.x,from.x,t0);
        double x1=MathHelper.linear_interpolate(destination.x,from.x,t1);
        double z0=MathHelper.linear_interpolate(destination.z,from.z,t0);
        double z1=MathHelper.linear_interpolate(destination.z,from.z,t1);

        return aabb.positionInBoundXZ(x0,z0)||aabb.positionInBoundXZ(x1,z1);
    }

    /**
     * test if a ray collide with projection from y-axis of an AxisAlignedBB
     * @param aabb AxisAlignedBB
     * @param from ray from
     * @param destination ray destination
     * @return projection collide?
     */
    public static boolean zProjectionCollide(AABB aabb,Vector3d from,Vector3d destination){
        double t0=MathHelper.reverse_interpolate_abs(from.z,destination.z,aabb.z0);
        double t1=MathHelper.reverse_interpolate_abs(from.z,destination.z,aabb.z1);

        double x0=MathHelper.linear_interpolate(destination.x,from.x,t0);
        double x1=MathHelper.linear_interpolate(destination.x,from.x,t1);
        double y0=MathHelper.linear_interpolate(destination.y,from.y,t0);
        double y1=MathHelper.linear_interpolate(destination.y,from.y,t1);

        return aabb.positionInBoundXY(x0,y0)||aabb.positionInBoundXY(x1,y1);
    }

    /**
     * test an Axis-AlignedBB intersect with a ray.
     * @param aabb test aabb
     * @param from ray from
     * @param destination ray destination
     * @return collide?
     */
    public static boolean innerSection(AABB aabb,Vector3d from,Vector3d destination){
        return xProjectionCollide(aabb, from, destination)||yProjectionCollide(aabb, from, destination)||zProjectionCollide(aabb, from, destination);
    }

    public static final double STEP=0.001;

    /**
     * get a facing from ray test
     * @param aabb test aabb
     * @param from from
     * @param destination destination
     * @return face of colliding. if not collide return -1.
     */
    public static int getInnerSectionFacing(AABB aabb,Vector3d from,Vector3d destination){
        if(!innerSection(aabb, from, destination)){
            return -1;
        }
        double t_start=aabb.distanceMin(from)/from.distance(destination);
        double t_end=aabb.distanceMax(from)/from.distance(destination);

        Vector3d sample_final=new Vector3d();
        double x=t_start;
        while (x<t_end&&!aabb.isVectorInside(MathHelper.linear_interpolate(from,destination,x+STEP))){
            sample_final=MathHelper.linear_interpolate(from,destination,x);
            x+=STEP;
        }
        if(sample_final.y>=aabb.y1&&aabb.positionInBoundXZ(sample_final.x,sample_final.z)){
            return 0;
        }
        if(sample_final.y<=aabb.y0&&aabb.positionInBoundXZ(sample_final.x,sample_final.z)){
            return 1;
        }
        if(sample_final.x>=aabb.x1&&aabb.positionInBoundYZ(sample_final.y,sample_final.z)){
            return 2;
        }
        if(sample_final.x<=aabb.x0&&aabb.positionInBoundYZ(sample_final.y,sample_final.z)){
            return 3;
        }
        if(sample_final.z>=aabb.z1&&aabb.positionInBoundXY(sample_final.x,sample_final.y)){
            return 4;
        }
        if(sample_final.z<=aabb.z0&&aabb.positionInBoundXY(sample_final.x,sample_final.y)){
            return 5;
        }

        //this will never happen if collide is true.
        return -1;
    }

    /**
     * ray trace an aabb from given aabbs
     * @param aabbs aabbs
     * @param from from
     * @param destination destination
     * @return hit result.Contains face and aabb.If no aabb selected will return null. Remind the NPE.
     */
    public static HitResult rayTrace(ArrayList<AABB> aabbs,Vector3d from, Vector3d destination){
        ArrayList<AABB> intersects=new ArrayList<>();
        for(AABB aabb:aabbs){
            if (innerSection(aabb,from,destination)){
                intersects.add(aabb);
            }
        }
        intersects.sort(Comparator.comparingDouble(o -> o.distanceMin(from)));
        if(intersects.size()>0) {
            AABB aabb = intersects.get(0);
            return new HitResult(aabb, getInnerSectionFacing(aabb, from, destination));
        }else{
            return null;
        }
    }
}

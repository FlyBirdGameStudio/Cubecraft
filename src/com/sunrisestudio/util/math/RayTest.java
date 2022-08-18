package com.sunrisestudio.util.math;

import com.sunrisestudio.util.container.BufferBuilder;
import com.sunrisestudio.util.timer.Timer;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.Comparator;

//todo:修复当玩家射线对准失眠判断错误

public class RayTest {
    /**
     * test if a ray collide with projection from x-axis of an AxisAlignedBB
     *
     * @param aabb        AxisAlignedBB
     * @param from        ray from
     * @param destination ray destination
     * @return projection collide?
     */
    @Deprecated
    public static boolean xProjectionCollide(AABB aabb, Vector3d from, Vector3d destination) {
        double t0 = MathHelper.reverse_interpolate_abs(from.x, destination.x, aabb.x0);
        double t1 = MathHelper.reverse_interpolate_abs(from.x, destination.x, aabb.x1);

        double y0 = MathHelper.linear_interpolate(destination.y, from.y, t0);
        double y1 = MathHelper.linear_interpolate(destination.y, from.y, t1);
        double z0 = MathHelper.linear_interpolate(destination.z, from.z, t0);
        double z1 = MathHelper.linear_interpolate(destination.z, from.z, t1);

        return aabb.positionInBoundYZ(y0, z0) || aabb.positionInBoundYZ(y1, z1);
    }

    /**
     * test if a ray collide with projection from y-axis of an AxisAlignedBB
     *
     * @param aabb        AxisAlignedBB
     * @param from        ray from
     * @param destination ray destination
     * @return projection collide?
     */
    @Deprecated(since = "alpha0.2.6")
    public static boolean yProjectionCollide(AABB aabb, Vector3d from, Vector3d destination) {
        double t0 = MathHelper.reverse_interpolate_abs(from.y, destination.y, aabb.y0);
        double t1 = MathHelper.reverse_interpolate_abs(from.y, destination.y, aabb.y1);

        double x0 = MathHelper.linear_interpolate(destination.x, from.x, t0);
        double x1 = MathHelper.linear_interpolate(destination.x, from.x, t1);
        double z0 = MathHelper.linear_interpolate(destination.z, from.z, t0);
        double z1 = MathHelper.linear_interpolate(destination.z, from.z, t1);

        return aabb.positionInBoundXZ(x0, z0) || aabb.positionInBoundXZ(x1, z1);
    }

    /**
     * test if a ray collide with projection from y-axis of an AxisAlignedBB
     *
     * @param aabb        AxisAlignedBB
     * @param from        ray from
     * @param destination ray destination
     * @return projection collide?
     */
    @Deprecated(since = "alpha0.2.6")
    public static boolean zProjectionCollide(AABB aabb, Vector3d from, Vector3d destination) {
        double t0 = MathHelper.reverse_interpolate_abs(from.z, destination.z, aabb.z0);
        double t1 = MathHelper.reverse_interpolate_abs(from.z, destination.z, aabb.z1);

        double x0 = MathHelper.linear_interpolate(destination.x, from.x, t0);
        double x1 = MathHelper.linear_interpolate(destination.x, from.x, t1);
        double y0 = MathHelper.linear_interpolate(destination.y, from.y, t0);
        double y1 = MathHelper.linear_interpolate(destination.y, from.y, t1);

        return aabb.positionInBoundXY(x0, y0) || aabb.positionInBoundXY(x1, y1);
    }

    /**
     * test an Axis-AlignedBB intersect with a ray.
     *
     * @param aabb        test aabb
     * @param from        ray from
     * @param destination ray destination
     * @return collide?
     */
    @Deprecated(since = "alpha0.2.6")
    public static boolean innerSection(AABB aabb, Vector3d from, Vector3d destination) {
        return xProjectionCollide(aabb, from, destination) || yProjectionCollide(aabb, from, destination) || zProjectionCollide(aabb, from, destination);
    }


    public static final double STEP = 0.002;

    /**
     * get a facing from ray test
     *
     * @param aabb        test aabb
     * @param from        from
     * @param destination destination
     * @return face of colliding. if not collide return -1.
     */
    public static byte getIntersectionFacing(AABB aabb, Vector3d from, Vector3d destination) {
        Vector3d hitPos = test(from, destination, aabb);
        if (hitPos != null) {
            if (hitPos.y >= aabb.y1 && aabb.positionInBoundXZ(hitPos.x, hitPos.z)) {
                return 0;
            }
            if (hitPos.y <= aabb.y0 && aabb.positionInBoundXZ(hitPos.x, hitPos.z)) {
                return 1;
            }
            if (hitPos.z >= aabb.z1 && aabb.positionInBoundXY(hitPos.x, hitPos.y)) {
                return 2;
            }
            if (hitPos.z <= aabb.z0 && aabb.positionInBoundXY(hitPos.x, hitPos.y)) {
                return 3;
            }
            if (hitPos.x >= aabb.x1 && aabb.positionInBoundYZ(hitPos.y, hitPos.z)) {
                return 4;
            }
            if (hitPos.x <= aabb.x0 && aabb.positionInBoundYZ(hitPos.y, hitPos.z)) {
                return 5;
            }

        }
        return -1;
    }

    /**
     * ray trace an aabb from given aabbs
     *
     * @param aabbs       aabbs
     * @param from        from
     * @param destination destination
     * @return hit result.Contains face and aabb.If no aabb selected will return null. Remind the NPE.
     */
    public static HitResult rayTrace(ArrayList<HitBox> aabbs, Vector3d from, Vector3d destination) {
        ArrayList<HitBox> intersects = new ArrayList<>();
        for (HitBox aabb : aabbs) {
            if (test(from,destination,aabb)!=null) {
                intersects.add(aabb);
            }
        }
        intersects.sort(Comparator.comparingDouble(o -> o.distanceMin(from)));
        if (intersects.size() > 0) {
            HitBox aabb = intersects.get(0);
            return new HitResult(aabb,getIntersectionFacing(aabb,from,destination));
        } else {
            return null;
        }

    }

    @Deprecated
    public static Vector3d hitDir(Vector3d origin, Vector3d dir, AABB aabb) {
        double tmin = Double.MIN_VALUE;
        double tmax = Double.MAX_VALUE;


        {
            //Compute the distance of ray to the near plane and far plane
            double ood = 1.0f / dir.x;
            double t1 = (aabb.x0 - origin.x) * ood;
            double t2 = (aabb.x1 - origin.x) * ood;

            //Make t1 be intersecting with the near plane, t2 with the far plane
            if (t1 > t2) {
                double temp = t1;
                t1 = t2;
                t2 = temp;
            }

            //Compute the intersection of slab intersection intervals
            if (t1 > tmin) tmin = t1;
            if (t2 < tmax) tmax = t2;

            //Exit with no collision as soon as slab intersection becomes empty
            if (tmin > tmax) return null;
        }// end for perpendicular to x-axie

        //The plane perpendicular to y-axie

        {
            //Compute the distance of ray to the near plane and far plane
            double ood = 1.0f / dir.y;
            double t1 = (aabb.y0 - origin.y) * ood;
            double t2 = (aabb.y1 - origin.y) * ood;

            //Make t1 be intersecting with the near plane, t2 with the far plane
            if (t1 > t2) {
                double temp = t1;
                t1 = t2;
                t2 = temp;
            }

            //Compute the intersection of slab intersection intervals
            if (t1 > tmin) tmin = t1;
            if (t2 < tmax) tmax = t2;

            //Exit with no collision as soon as slab intersection becomes empty
            if (tmin > tmax) return null;
        }// end for perpendicular to y-axie

        //The plane perpendicular to z-axie
        {
            //Compute the distance of ray to the near plane and far plane
            double ood = 1.0f / dir.z;
            double t1 = (aabb.z0 - origin.z) * ood;
            double t2 = (aabb.z1 - origin.z) * ood;

            //Make t1 be intersecting with the near plane, t2 with the far plane
            if (t1 > t2) {
                double temp = t1;
                t1 = t2;
                t2 = temp;
            }

            //Compute the intersection of slab intersection intervals
            if (t1 > tmin) tmin = t1;
            if (t2 < tmax) tmax = t2;

            //Exit with no collision as soon as slab intersection becomes empty
            if (tmin > tmax) return null;
        }// end for perpendicular to z-axis

        Vector3d vcHit = new Vector3d();
        vcHit.x = origin.x + tmin * dir.x;
        vcHit.y = origin.y + tmin * dir.y;
        vcHit.z = origin.z + tmin * dir.z;
        return vcHit;
    }


    /**
     * set a ray from a to b. test if collide aabb.
     * @param from "a"
     * @param dest "b"
     * @param aabb "aabb"
     * @return collide position,if not collided return null.
     */
    public static Vector3d test(Vector3d from, Vector3d dest, AABB aabb) {

        final double distMin = aabb.distanceMin(from),//min dist to aabb
                distMax = aabb.distanceMax(from),//max dist to aabb
                distAll = MathHelper.dist(from, dest);//dist from a to b

        final double cosXDist = (dest.x - from.x) / distAll,//x cos of distAll
                cosYDist = (dest.y - from.y) / distAll,//y cos of distAll
                cosZDist = (dest.z - from.z) / distAll;//z cos of distAll

        if(distMin>distAll){
            return null;
        }


        //do sample here.
        for (double sampleDist=distMin;sampleDist<distMax;sampleDist+=STEP){
            double t=sampleDist/distAll;
            double t1=(sampleDist+STEP)/distAll;
            if(aabb.isVectorInside(MathHelper.linear_interpolate(from,dest,t1))){
                //collided...return position
                //System.out.println(aabb);
                return MathHelper.linear_interpolate(from,dest,t);
            }
        }
        //no search result...return null :(
        return null;
    }
}

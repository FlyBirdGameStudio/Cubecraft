package io.flybird.util.math;

import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * raw and aabb intersection
 * <p>I can`t explain how it works,but it does written by me.</p>
 *
 * @author GrassBlock2022
 */
public class RayTest {
    public static final double STEP=0.002;

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

        if(distMin>distAll){
            return null;
        }

        //do sample here.
        for (
                double sampleDist=distMin-aabb.getMaxWidth()/1.414/*pow(2)*/;
                sampleDist<distMax+aabb.getMaxWidth()/1.414;
                sampleDist+=STEP
        ){
            double t=sampleDist/distAll;
            double t1=(sampleDist+STEP)/distAll;
            if(aabb.isVectorInside(MathHelper.linearInterpolate(from,dest,t1))){
                //collided..return position
                return MathHelper.linearInterpolate(from,dest,t);
            }
        }
        //no search result...return null :(
        return null;
    }
}

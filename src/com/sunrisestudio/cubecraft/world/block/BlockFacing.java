package com.sunrisestudio.cubecraft.world.block;


import com.sunrisestudio.util.container.Vector3;
import com.sunrisestudio.util.math.RotationMatrixConstants;
import org.joml.Matrix4f;

public enum BlockFacing {
    Up(0),
    Down(1),
    West(2),
    East(3),
    North(4),
    South(5);

    final byte numID;
    BlockFacing(int id){
        this.numID=(byte)id;
    }

    public byte getNumID() {
        return numID;
    }

    public static BlockFacing fromId(int id){
        return switch (id){
            case 0->Up;
            case 1->Down;
            case 2->West;
            case 3->East;
            case 4->North;
            case 5->South;
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    public Vector3<Long> findNear(long x, long y, long z, int radius){
        switch (this){
            case Up:y+=radius;
            case Down:y-=radius;
            case West:z+=radius;
            case East:z-=radius;
            case North:x+=radius;
            case South:x-=radius;
        }
        return new Vector3<>(x, y, z);
    }

    public static BlockFacing[] all(){
        return new BlockFacing[]{
                Up,
                Down,
                West,
                East,
                North,
                South
        };
    }

    public static Matrix4f getMatrix(BlockFacing facing) {
        return switch (facing){
            case Up -> RotationMatrixConstants.FACE_TOP;
            case Down -> RotationMatrixConstants.FACE_BOTTOM;
            case East -> RotationMatrixConstants.FACE_RIGHT;
            case West -> RotationMatrixConstants.FACE_LEFT;
            case North -> RotationMatrixConstants.FACE_BACK;
            case South -> RotationMatrixConstants.FACE_FRONT;
        };
    }


    /**
     * get axis aligned(world space facing)
     * @param facing block face
     * @param face face to query(relative)
     * @return axis aligned block facing
     */
    public static BlockFacing clip(BlockFacing facing,BlockFacing face){
        return fromId(faceMapping[facing.getNumID()][face.getNumID()]);
    }

    /**
     * an alternative method of clip(BlockFacing,BlockFacing)
     * @param facing block face
     * @param face face to query(relative)
     * @return axis aligned block facing
     */
    public static int clip(int facing,int face){
        return faceMapping[facing][face];
    }

    private static final int[][] faceMapping=new int[6][6];

    static {
        faceMapping[0]= new int[]{0, 1, 2, 3, 4, 5};//up(default)
        faceMapping[1]= new int[]{1, 0, 2, 3, 4, 5};//down)
        faceMapping[2]= new int[]{2, 3, 1, 0, 4, 5};//front
        faceMapping[3]= new int[]{3, 2, 1, 0, 4, 5};//back
        faceMapping[4]= new int[]{4, 5, 2, 3, 1, 0};//left
        faceMapping[5]= new int[]{5, 4, 2, 3, 0, 1};//right
    }

}

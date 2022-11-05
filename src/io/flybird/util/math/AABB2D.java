package io.flybird.util.math;

public class AABB2D {
    public double x0;
    public double y0;
    public double x1;
    public double y1;

    public AABB2D(double x0, double y0, double x1, double y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    public AABB2D(AABB2D aabb) {
        this.x0 = aabb.x0;
        this.y0 = aabb.y0;
        this.x1 = aabb.x1;
        this.y1 = aabb.y1;
    }

    public boolean intersect(AABB2D c){
        if (c.x1 <= this.x0 || c.x0 >= this.x1) {
            return false;
        }
        return !(c.y1 <= this.y0 || c.y0 >= this.y1);
    }

    public AABB2D move(double x,double y) {
        this.x0+=x;
        this.y0+=y;
        this.x1+=x;
        this.y1+=y;
        return this;
    }
}

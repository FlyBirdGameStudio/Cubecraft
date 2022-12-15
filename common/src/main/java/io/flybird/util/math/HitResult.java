package io.flybird.util.math;

public record HitResult<F,W>(HitBox<F,W> aabb, byte facing) {
    public void hit(F f,W w) {
        this.aabb.getObject().onHit(f,w,this);
    }

    public void interact(F f,W w) {
        this.aabb.getObject().onInteract(f,w,this);
    }
}

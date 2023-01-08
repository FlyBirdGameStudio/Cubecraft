package io.flybird.util.math;


public interface HittableObject<F,W> {
    void onHit(F from, W world, HitResult<F,W> hr);
    void onInteract(F from, W world, HitResult<F,W> hr);

}

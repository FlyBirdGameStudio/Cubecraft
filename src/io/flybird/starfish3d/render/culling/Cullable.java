package io.flybird.starfish3d.render.culling;

public interface Cullable {
    boolean isVisible(ICuller culler);
}

package com.flybirdstudio.starfish3d.render.culling;

public interface Cullable {
    boolean isVisible(ICuller culler);
}

package com.sunrisestudio.grass3d.render.culling;

public interface Cullable {
    boolean isVisible(ICuller culler);
}

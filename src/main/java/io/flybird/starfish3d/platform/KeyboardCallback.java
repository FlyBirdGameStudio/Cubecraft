package io.flybird.starfish3d.platform;

public interface KeyboardCallback {
    default void onKeyEventNext(){}
    default void onKeyEventPressed(){}
    default void onCharEvent(){};
}

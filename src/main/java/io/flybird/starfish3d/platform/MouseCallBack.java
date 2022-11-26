package io.flybird.starfish3d.platform;

public interface MouseCallBack {
    default void onScroll(int value){}
    default void onButtonClicked(int eventButton){}
}

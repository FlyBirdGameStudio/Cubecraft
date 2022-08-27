package com.sunrisestudio.grass3d.platform.input;

public interface MouseCallBack {
    default void onScroll(int value){}
    default void onButtonClicked(int eventButton){}
}

package com.flybirdstudio.starfish3d.platform;

public record WindowInfo(
        int x,int y,
        int width,int height,
        int fbWidth,int fbHeight,
        long handle
) {}

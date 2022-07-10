package com.sunrisestudio.util;

import org.lwjglx.input.Keyboard;

public class FraggedUtil {
    static boolean keyDownStatus = true;
    static int keyDownCount=0;
    static long lastTime;
    public static boolean isDoubleClicked(int key, float timeElapse) {

        if (Keyboard.isKeyDown(key)) {
            if (!keyDownStatus) {
                keyDownStatus = true;
                if (keyDownCount == 0) {// 如果按住数量为 0
                    lastTime = System.currentTimeMillis();// 记录最后时间
                }
                keyDownCount++;
            }
        }
        if (!Keyboard.isKeyDown(key)) {
            keyDownStatus = false;
        }
        if (keyDownStatus) {
            if (keyDownCount >= 2) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTime < timeElapse) {
                    lastTime = currentTime;
                    keyDownCount = 0;
                    return true;//返回结果，确认双击
                }
                else {
                    lastTime = System.currentTimeMillis();  // 记录最后时间
                    keyDownCount = 1;
                }
            }
        }
        return false;
    }
}

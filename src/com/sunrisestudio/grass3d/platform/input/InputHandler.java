package com.sunrisestudio.grass3d.platform.input;

import com.sunrisestudio.util.LogHandler;
import com.sunrisestudio.util.container.CollectionUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * if you use this thing,remember:
 * do not use while(Mouse.next())and while(Keyboard.next()) anywhere.
 */
public class InputHandler {
    static boolean keyDownStatus = true;
    static int keyDownCount=0;
    static long lastTime;

    private static final LogHandler logHandler=LogHandler.create("input","client");
    private static final HashMap<String, KeyboardCallback> keyboardCallbacks =new HashMap<>();
    private static final Map<String, MouseCallBack> mouseCallbacks=new HashMap<>();


    /**
     * register a callback for keyboard,fail with id conflict.
     * @param id id
     * @param cb callback
     */
    public static void registerGlobalKeyboardCallback(String id, KeyboardCallback cb){
        keyboardCallbacks.put(id,cb);
    }



    /**
     * release a keyboard callback.
     * @param id id
     */
    public static void releaseGlobalKeyboardCallback(String id){
        keyboardCallbacks.remove(id);
    }

    /**
     * register a callback for mouse,fail with id conflict.
     * @param id id
     * @param cb callback
     */
    public static void registerGlobalMouseCallback(String id, MouseCallBack cb){
        mouseCallbacks.put(id,cb);
    }


    /**
     * release a mouse callback.
     * @param id id
     */
    public static void releaseGlobalMouseCallback(String id){
        mouseCallbacks.remove(id);
    }

    /**
     * tick the input and holding all callbacks.
     */
    public static void tick(){

        HashMap<String,KeyboardCallback> kb=keyboardCallbacks;
        while (Keyboard.next()){
            CollectionUtil.iterateMap(kb, (key, item) -> {
                item.onKeyEventNext();
            });
            if(Keyboard.getEventKeyState()){
                CollectionUtil.iterateMap(kb, (key, item) -> {
                    item.onKeyEventPressed();
                });
            }
        }

        HashMap<String,MouseCallBack> m= (HashMap<String, MouseCallBack>) mouseCallbacks;
        //CollectionUtil.iterateMap(mouseCallbacks,((key, item) -> m.put(key,item)));
        while (Mouse.next()){
            CollectionUtil.iterateMap(m, (key, item) -> {
                item.onMouseEventNext();
            });

            if(Mouse.isButtonDown(0)){
                CollectionUtil.iterateMap(m, (key, item) -> {
                    item.onLeftClick();
                });
            }
            if(Mouse.isButtonDown(1)){
                CollectionUtil.iterateMap(m, (key, item) -> {
                    item.onRightClick();
                });
            }
            if(Mouse.isButtonDown(2)){
                CollectionUtil.iterateMap(m, (key, item) -> {
                    item.onMidClick();
                });
            }
        }
        int scroll=Mouse.getDWheel();
        if(scroll!=0){
            CollectionUtil.iterateMap(m, (key, item) -> {
                item.onScroll(scroll);
            });
        }

    }

    /**
     * an util that tells you if a key pressed twice.
     * @param key key id
     * @param timeElapse time elapse from first click to next click
     * @return clicked?
     */
    public static boolean isKeyDoubleClicked(int key, float timeElapse) {
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

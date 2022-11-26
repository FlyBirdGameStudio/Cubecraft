package io.flybird.starfish3d.platform;

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

    private static final Map<String, MouseCallBack> mouseCallbacks=new HashMap<>();


    /**
     * register a callback for mouse,fail with id conflict.
     * @param id id
     * @param cb callback
     */
    public static void registerGlobalMouseCallback(String id, MouseCallBack cb){
        mouseCallbacks.put(id,cb);
    }

    /**
     * tick the input and holding all callbacks.
     */
    public static void tick(){
        /*
        HashMap<String,MouseCallBack> m= (HashMap<String, MouseCallBack>) mouseCallbacks;
        while (Mouse.next()){
            if(Mouse.getEventButtonState()){
                int btn=Mouse.getEventButton();
                CollectionUtil.iterateMap(m,(key,item)->{
                    item.onButtonClicked(btn);
                });
            }
        }

        int scroll=Mouse.getDWheel();
        if(scroll!=0){
            CollectionUtil.iterateMap(m, (key, item) -> {
                item.onScroll(scroll);
            });
        }
         */

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

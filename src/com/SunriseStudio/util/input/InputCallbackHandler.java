package com.sunrisestudio.util.input;

import com.sunrisestudio.util.LogHandler;
import com.sunrisestudio.util.container.CollectionUtil;
import org.lwjglx.input.Keyboard;
import org.lwjglx.input.Mouse;

import java.util.HashMap;
import java.util.Map;

public class InputCallbackHandler {
    private static LogHandler logHandler=LogHandler.create("input","client");
    private static final HashMap<String, KeyboardCallback> keyboardCallbacks =new HashMap<>();
    private static Map<String, MouseCallBack> mouseCallbacks=new HashMap<>();

    /**
     * register a callback for keyboard,fail with id conflict.
     * @param id id
     * @param cb callback
     */
    public static void registerGlobalKeyboardCallback(String id, KeyboardCallback cb){
        if(keyboardCallbacks.containsKey(id)){
            logHandler.error("conflict keyboard callback register");
        }else{
            keyboardCallbacks.put(id,cb);
        }
    }

    public static void releaseGlobalKeyboardCallback(String id){
        keyboardCallbacks.remove(id);
    }

    /**
     * register a callback for mouse,fail with id conflict.
     * @param id id
     * @param cb callback
     */
    public static void registerGlobalMouseCallback(String id, MouseCallBack cb){
        if(mouseCallbacks.containsKey(id)){
            logHandler.error("conflict mouse callback register");
        }else{
            mouseCallbacks.put(id,cb);
        }
    }

    public static void releaseGlobalMouseCallback(String id){
        mouseCallbacks.remove(id);
    }

    public static void tick(){
        while (Keyboard.next()){
            CollectionUtil.iterateMap(keyboardCallbacks, (key, item) -> {
                item.onKeyEventNext();
            });
            if(Keyboard.getEventKeyState()){
                CollectionUtil.iterateMap(keyboardCallbacks, (key, item) -> {
                    item.onKeyEventPressed();
                });
            }
        }
        while (Mouse.next()){
            CollectionUtil.iterateMap(mouseCallbacks, (key, item) -> {
                item.onMouseEventNext();

            });

            if(Mouse.isButtonDown(0)){
                CollectionUtil.iterateMap(mouseCallbacks, (key, item) -> {
                    item.onLeftClick();
                });
            }
            if(Mouse.isButtonDown(1)){
                CollectionUtil.iterateMap(mouseCallbacks, (key, item) -> {
                    item.onRightClick();
                });
            }
            if(Mouse.isButtonDown(2)){
                CollectionUtil.iterateMap(mouseCallbacks, (key, item) -> {
                    item.onMidClick();
                });
            }
        }
        int scroll=Mouse.getDWheel();
        if(scroll!=0){
            CollectionUtil.iterateMap(mouseCallbacks, (key, item) -> {
                item.onScroll(scroll);
            });
        }
    }
}

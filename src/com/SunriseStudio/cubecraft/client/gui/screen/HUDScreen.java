package com.sunrisestudio.cubecraft.client.gui.screen;

import com.sunrisestudio.cubecraft.client.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.GameSetting;
import com.sunrisestudio.cubecraft.client.CubeCraft;
import com.sunrisestudio.cubecraft.client.gui.FontAlignment;
import com.sunrisestudio.grass3d.platform.Display;
import com.sunrisestudio.grass3d.platform.Keyboard;
import com.sunrisestudio.grass3d.platform.Mouse;
import com.sunrisestudio.grass3d.render.GLUtil;
import com.sunrisestudio.util.SystemInfo;
import com.sunrisestudio.grass3d.render.ShapeRenderer;
import com.sunrisestudio.cubecraft.client.render.renderer.ChunkRenderer;
import com.sunrisestudio.cubecraft.client.gui.FontRenderer;
import com.sunrisestudio.grass3d.render.textures.Texture2D;
import com.sunrisestudio.grass3d.platform.input.KeyboardCallback;
import com.sunrisestudio.grass3d.platform.input.MouseCallBack;
import com.sunrisestudio.util.math.HitResult;
import org.joml.Vector3d;


import java.text.DecimalFormat;

public class HUDScreen extends Screen {
    private Texture2D actionBar=new Texture2D(false,false);


    public HUDScreen(){
        super();
        this.actionBar.generateTexture();
        this.actionBar.load("/resource/textures/gui/actionbar.png");
    }

    @Override
    public void init() {
        Mouse.setGrabbed(true);
    }

    private boolean debugScreen;
    private boolean showGUI=true;

    @Override
    public void render(DisplayScreenInfo info) {
        GLUtil.enableBlend();
        this.platform.player.turn(Mouse.getDX(), Mouse.getDY(), 0);
        if(showGUI) {
            DecimalFormat format = new DecimalFormat();
            format.applyPattern("0.000");
            if (this.debugScreen) {
                int r0_l=2;
                FontRenderer.renderShadow("CubeCraft-" + CubeCraft.VERSION,
                        2, r0_l+0, 16777215, 8, FontAlignment.LEFT);

                FontRenderer.renderShadow("帧率:" + this.platform.getTimingInfo().shortTickTPS() + "/单帧时间:" + this.platform.getTimingInfo().shortTickMSPT(),
                        2, r0_l+10, 16777215, 8, FontAlignment.LEFT);

                FontRenderer.renderShadow("本地客户端tps：" + this.platform.getTimingInfo().longTickTPS() + "/MSPT:" + this.platform.getTimingInfo().longTickMSPT(),
                        2, r0_l+20, 0xFFFFFF, 8, FontAlignment.LEFT);

                FontRenderer.renderShadow("本地区块缓存:" + this.platform.clientWorld.getChunkCache().size(),
                        2, r0_l+30, 16777215, 8, FontAlignment.LEFT);


                int r1_l=42;
                ChunkRenderer cr = (ChunkRenderer) this.platform.worldRenderer.renderers.get("cubecraft:chunk_renderer");
                FontRenderer.renderShadow("地形渲染(总数/可见/更新):" + cr.allCount + "/" + cr.visibleCount + "/" + cr.updateCount,
                        2, r1_l+0, 16777215, 8, FontAlignment.LEFT);

                FontRenderer.renderShadow("位置（x/y/z）:" + format.format(this.platform.player.x) + "/" + format.format(this.platform.player.y) + "/" + format.format(this.platform.player.z),
                        2, r1_l+10, 16777215, 8, FontAlignment.LEFT);

                FontRenderer.renderShadow("相机角度（yaw/pitch/roll）:" + format.format(this.platform.player.xRot) + "/" + format.format(this.platform.player.yRot) + "/" + format.format(this.platform.player.zRot),
                        2, r1_l+20, 0xFFFFFF, 8, FontAlignment.LEFT);



                FontRenderer.renderShadow("系统：" + SystemInfo.getOSName() + "/" + SystemInfo.getOSVersion(),
                        info.scrWidth() - 2, 2, 16777215, 8, FontAlignment.RIGHT);

                FontRenderer.renderShadow("内存(已使用/总量)：" + SystemInfo.getUsedMemory() + "/" + SystemInfo.getTotalMemory(),
                        info.scrWidth() - 2, 12, 0xFFFFFF, 8, FontAlignment.RIGHT);

                FontRenderer.renderShadow("JVM：" + SystemInfo.getJavaName() + "/" + SystemInfo.getJavaVersion(),
                        info.scrWidth() - 2, 22, 0xFFFFFF, 8, FontAlignment.RIGHT);


                HitResult hitResult=this.platform.player.hitResult;
                FontRenderer.renderShadow("目标方块：" +(hitResult!=null?hitResult.aabb().x0+"/"+hitResult.aabb().y0+"/"+hitResult.aabb().z0+"="+hitResult.facing():"null"),
                            info.centerX()+3,info.centerY(),0xFFFFFF,8,FontAlignment.LEFT);

                Vector3d v=this.platform.player.getHitTargetPos();
                FontRenderer.renderShadow("目标位置：" +"%f/%f/%f".formatted(v.x,v.y,v.z),
                        info.centerX()+3,info.centerY()+10,0xFFFFFF,8,FontAlignment.LEFT);


            }
            this.actionBar.bind();
            int w = Display.getWidth() / GameSetting.instance.GUIScale, h = Display.getHeight() / GameSetting.instance.GUIScale;
            ShapeRenderer.drawRectUV(w / 2d - 91, w / 2d + 91, h - 22, h, 0, 0, 0, 182 / 256f, 0, 22 / 32f);
            int slotBase = (int) (w / 2d + 20 * (-4.5 + 0));
            ShapeRenderer.drawRectUV((slotBase) - 2, (slotBase + 23), h - (23), h, 0, 0, 232 / 256f, 1, 0, 22 / 32f);
            GLUtil.disableBlend();
        }
    }

    @Override
    public boolean isInGameGUI() {
        return true;
    }

    @Override
    public MouseCallBack getMouseCallback() {
        return new MouseCallBack(){
            @Override
            public void onScroll(int value) {
                int i=value;
                if (i > 0) {
                   i = 1;
                }
                if (i < 0) {
                   i = -1;
                }

                /*
                HUDScreen.this.platform._player.selectSlot+=i;
                if (HUDScreen.this.platform._player.selectSlot>8){
                    HUDScreen.this.platform._player.selectSlot=0;
                }
                if (HUDScreen.this.platform._player.selectSlot<0){
                    HUDScreen.this.platform._player.selectSlot=8;
                }

                 */
            }

            @Override
            public void onLeftClick() {
                if(Display.isActive()){
                    Mouse.setGrabbed(true);
                }

            }

            @Override
            public void onRightClick() {
                super.onRightClick();
            }
        };
    }

    @Override
    public KeyboardCallback getKeyboardCallback() {
        return new KeyboardCallback(){
            @Override
            public void onKeyEventNext() {
                HUDScreen.this.platform.controller.setKey(Keyboard.getEventKey(), Keyboard.getEventKeyState());
            }

            @Override
            public void onKeyEventPressed() {
                if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                    Mouse.setGrabbed(false);
                    HUDScreen.this.platform.controller.releaseAllKeys();
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_LCONTROL) {
                    HUDScreen.this.platform.player.runningMode=!HUDScreen.this.platform.player.runningMode;
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_F3) {
                    HUDScreen.this.debugScreen=!HUDScreen.this.debugScreen;
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_F1) {
                    HUDScreen.this.showGUI=!HUDScreen.this.showGUI;
                }
            }
        };
    }

    @Override
    public Screen getParentScreen() {
        return new PauseScreen();
    }
}

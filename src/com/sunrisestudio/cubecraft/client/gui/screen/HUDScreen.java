package com.sunrisestudio.cubecraft.client.gui.screen;

import com.sunrisestudio.cubecraft.client.Cubecraft;
import com.sunrisestudio.cubecraft.client.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.client.gui.FontAlignment;
import com.sunrisestudio.cubecraft.client.gui.FontRenderer;
import com.sunrisestudio.cubecraft.client.render.renderer.ChunkRenderer;
import com.sunrisestudio.cubecraft.world.HittableObject;
import com.sunrisestudio.cubecraft.world.block.BlockState;
import com.sunrisestudio.cubecraft.world.item.Inventory;
import com.sunrisestudio.grass3d.platform.Display;
import com.sunrisestudio.grass3d.platform.input.Keyboard;
import com.sunrisestudio.grass3d.platform.input.KeyboardCallback;
import com.sunrisestudio.grass3d.platform.input.Mouse;
import com.sunrisestudio.grass3d.platform.input.MouseCallBack;
import com.sunrisestudio.grass3d.render.GLUtil;
import com.sunrisestudio.grass3d.render.ShapeRenderer;
import com.sunrisestudio.grass3d.render.textures.Texture2D;
import com.sunrisestudio.util.SystemInfo;
import com.sunrisestudio.util.math.HitResult;
import com.sunrisestudio.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;

public class HUDScreen extends Screen {
    private final Texture2D actionBar = new Texture2D(false, false);
    private final Texture2D pointer = new Texture2D(false, false);
    private final String[] debugInfoLeft = new String[64];
    private final String[] debugInfoRight = new String[64];
    private int slot;

    private boolean debugScreen;
    private boolean showGUI = true;

    public HUDScreen() {
        super();
        this.actionBar.generateTexture();
        this.actionBar.load("/resource/textures/gui/containers/actionbar.png");
        this.pointer.generateTexture();
        this.pointer.load("/resource/textures/gui/icons/pointer.png");
    }

    @Override
    public void init() {
        Mouse.setGrabbed(true);
        this.initDebugInfo();
    }

    @Override
    public void render(DisplayScreenInfo info, float interpolationTime) {
        super.render(info, interpolationTime);
        GLUtil.enableBlend();
        this.getPlatform().controller.tickFast();
        if (showGUI) {
            if (this.debugScreen) {
                this.renderDebugInfo(info);
            }
            this.renderActionBar(info);
            this.pointer.bind();
            GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_DST_COLOR);
            ShapeRenderer.drawRectUV(
                    info.centerX() - 8, info.centerX() + 8,
                    info.centerY() - 8, info.centerY() + 8,
                    0, 0, 0, 1, 0, 1
            );
            GLUtil.disableBlend();

        }
    }

    @Override
    public boolean isInGameGUI() {
        return true;
    }

    @Override
    public MouseCallBack getMouseCallback() {
        return new MouseCallBack() {
            @Override
            public void onScroll(int value) {
                int i = value;
                if (i > 0) {
                    i = 1;
                }
                if (i < 0) {
                    i = -1;
                }
                HUDScreen.this.slot += i;
                if (HUDScreen.this.slot > 8) {
                    HUDScreen.this.slot = 0;
                }
                if (HUDScreen.this.slot < 0) {
                    HUDScreen.this.slot = 8;
                }
            }

            @Override
            public void onButtonClicked(int eventButton) {
                if(eventButton==0){
                    Mouse.setGrabbed(true);
                    HUDScreen.this.getPlatform().getPlayer().attack();
                }
                if(eventButton==1){
                    HUDScreen.this.getPlatform().getPlayer().interact();
                }
                if(eventButton==2){
                    HittableObject obj=HUDScreen.this.getPlatform().getPlayer().hitResult.aabb().getObject();
                    Inventory inv=HUDScreen.this.getPlatform().getPlayer().getInventory();
                    if(obj instanceof BlockState){
                        //todo:inv.selectItem()
                    }
                }
            }
        };
    }

    @Override
    public KeyboardCallback getKeyboardCallback() {
        return new KeyboardCallback() {
            @Override
            public void onKeyEventPressed() {
                if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                    Mouse.setGrabbed(false);
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_F3) {
                    HUDScreen.this.debugScreen = !HUDScreen.this.debugScreen;
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_F1) {
                    HUDScreen.this.showGUI = !HUDScreen.this.showGUI;
                }
            }
        };
    }

    @Override
    public Screen getParentScreen() {
        return new PauseScreen();
    }

    @Override
    public void tick() {
        getPlatform().controller.tick();
        this.updateDebugInfo();
        super.tick();
    }

    private void updateDebugInfo() {
        this.debugInfoLeft[1] = "帧率:" + this.getPlatform().getTimingInfo().shortTickTPS() + "/单帧时间:" + this.getPlatform().getTimingInfo().shortTickMSPT();
        this.debugInfoLeft[2] = "本地客户端tps：" + this.getPlatform().getTimingInfo().longTickTPS() + "/MSPT:" + this.getPlatform().getTimingInfo().longTickMSPT();
        this.debugInfoLeft[3] = "本地区块缓存:%d".formatted(this.getPlatform().clientWorld.getChunkCache().size());
        ChunkRenderer cr = (ChunkRenderer) this.getPlatform().levelRenderer.renderers.get("cubecraft:chunk_renderer");
        if(cr!=null) {
            this.debugInfoLeft[5] = "地形渲染(总数/可见/更新):%d,%d,%d".formatted(cr.allCount, cr.visibleCount, cr.updateCount);
        }
        this.debugInfoLeft[6] = "实体渲染(总数/可见):%d/%d".formatted(0,0);

        this.debugInfoLeft[8] = "位置（x/y/z）:%f/%f/%f".formatted(this.getPlatform().getPlayer().x, this.getPlatform().getPlayer().y, this.getPlatform().getPlayer().z);
        this.debugInfoLeft[9] = "相机角度（x/y/z）:%f/%f/%f".formatted(this.getPlatform().getPlayer().xRot, this.getPlatform().getPlayer().yRot, this.getPlatform().getPlayer().zRot);
        HitResult hitResult = this.getPlatform().getPlayer().hitResult;
        if(hitResult!=null) {
            Vector3d pos=hitResult.aabb().getPosition();
            this.debugInfoLeft[10] = "目标方块：%f/%f/%f:%d=%s".formatted(
                pos.x,pos.y,pos.z,hitResult.facing(),
                    this.getPlatform().getPlayer().world.getBlockState((long) pos.x, (long) pos.y, (long) pos.z).getId()
            );
        }else{
            this.debugInfoLeft[10] = "目标方块：空";
        }

        this.debugInfoRight[1] = "内存(已使用/总量)：%s/%s(%s)".formatted(SystemInfo.getUsedMemory(),SystemInfo.getTotalMemory(),SystemInfo.getUsage());
    }

    private void initDebugInfo(){
        this.debugInfoLeft[0] = "CubeCraft-" + Cubecraft.VERSION;
        this.debugInfoRight[0] = "系统:%s-%s".formatted(SystemInfo.getOSName(), SystemInfo.getOSVersion());
        this.debugInfoRight[2] = "JVM：" + SystemInfo.getJavaName() + "/" + SystemInfo.getJavaVersion();
    }

    private void renderDebugInfo(DisplayScreenInfo info){
        int pos = 2;
        for (String s : this.debugInfoLeft) {
            FontRenderer.renderShadow(s, 2, pos, 16777215, 8, FontAlignment.LEFT);
            pos += 10;
        }
        pos=2;
        for (String s : this.debugInfoRight) {
            FontRenderer.renderShadow(s, info.scrWidth()-2, pos, 16777215, 8, FontAlignment.RIGHT);
            pos += 10;
        }
    }

    private void renderActionBar(DisplayScreenInfo info){
        final float scale=1.263f;
        GL11.glPushMatrix();
        GL11.glTranslated(info.centerX()-91*scale,info.scrHeight()-22*scale,0);
        this.actionBar.bind();
        ShapeRenderer.drawRectUV(
                0, 182*scale, 0, 22*scale, 0, 0,
                0, 182 / 256f, 0, 22 / 32f
        );
        float slotBase = slot * 20*scale;
        ShapeRenderer.drawRectUV(slotBase - 1*scale, slotBase + 23*scale, -1*scale,23*scale, 0, 0, 232 / 256f, 1, 0, 24 / 32f);
        GL11.glPopMatrix();
    }
}
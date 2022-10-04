package com.flybirdstudio.cubecraft.client.gui.screen;

import com.flybirdstudio.cubecraft.client.Cubecraft;
import com.flybirdstudio.cubecraft.client.gui.DisplayScreenInfo;
import com.flybirdstudio.cubecraft.client.gui.FontAlignment;
import com.flybirdstudio.cubecraft.client.gui.FontRenderer;
import com.flybirdstudio.cubecraft.client.gui.ScreenLoader;
import com.flybirdstudio.cubecraft.client.render.renderer.ChunkRenderer;
import com.flybirdstudio.cubecraft.world.HittableObject;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.cubecraft.world.item.Inventory;
import com.flybirdstudio.starfish3d.platform.input.Keyboard;
import com.flybirdstudio.starfish3d.platform.input.KeyboardCallback;
import com.flybirdstudio.starfish3d.platform.input.Mouse;
import com.flybirdstudio.starfish3d.platform.input.MouseCallBack;
import com.flybirdstudio.starfish3d.render.GLUtil;
import com.flybirdstudio.starfish3d.render.ShapeRenderer;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayUploader;
import com.flybirdstudio.starfish3d.render.textures.Texture2D;
import com.flybirdstudio.util.JVMInfo;
import com.flybirdstudio.util.SystemInfoHelper;
import com.flybirdstudio.util.math.HitResult;
import org.joml.Vector3d;
import org.lwjgl.openal.AL11;
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
        super("cubecraft:hud_screen", ScreenType.EMPTY);
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
                    HitResult hitResult=HUDScreen.this.getPlatform().getPlayer().hitResult;
                    if(hitResult!=null) {
                        HittableObject obj = HUDScreen.this.getPlatform().getPlayer().hitResult.aabb().getObject();
                        Inventory inv = HUDScreen.this.getPlatform().getPlayer().getInventory();
                        if (obj instanceof BlockState) {
                            inv.selectItem();
                        }
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
        return ScreenLoader.loadByExtName("/resource/ui/pause_screen.xml");
    }

    @Override
    public void tick() {
        getPlatform().controller.tick();
        getPlatform().controller.setSelectedSlot(slot);
        this.updateDebugInfo();
        super.tick();
    }

    private void updateDebugInfo() {
        this.debugInfoLeft[1] = "帧率:%d/单帧时间:%d(%d顶点提交)".formatted(
                this.getPlatform().getTimingInfo().shortTickTPS(),
                this.getPlatform().getTimingInfo().shortTickMSPT(),
                VertexArrayUploader.getUploadedCount()
        );
        this.debugInfoLeft[2] = "本地客户端tps：" + this.getPlatform().getTimingInfo().longTickTPS() + "/MSPT:" + this.getPlatform().getTimingInfo().longTickMSPT();
        this.debugInfoLeft[3] = "本地区块缓存:%d".formatted(this.getPlatform().getClientWorld().getChunkCache().size());
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

        this.debugInfoRight[1] = "内存(jvm)(已使用/总量)：%s/%s(%s)".formatted(JVMInfo.getUsedMemory(), JVMInfo.getTotalMemory(), JVMInfo.getUsage());
    }

    private void initDebugInfo(){
        this.debugInfoLeft[0] = "Cubecraft-" + Cubecraft.VERSION;


        this.debugInfoRight[0] = "JVM：" + JVMInfo.getJavaName() + "/" + JVMInfo.getJavaVersion();
        this.debugInfoRight[1]="openGL:"+ GL11.glGetString(GL11.GL_VERSION);
        this.debugInfoRight[2]="openAL:"+ AL11.alGetString(AL11.AL_VERSION);
        this.debugInfoRight[3]="内存(系统):"+SystemInfoHelper.getMemInstalled()/1024/1024/1024+"GB";
        this.debugInfoRight[4] = "系统:%s-%s".formatted(JVMInfo.getOSName(), JVMInfo.getOSVersion());
        this.debugInfoRight[5]="CPU:%s/%d核心/%fGHZ".formatted(SystemInfoHelper.getCpuName(),SystemInfoHelper.getCpuCores(),SystemInfoHelper.getCpu().getCurrentFreq()[0]/1024f/1024f/1024f);
        this.debugInfoRight[6]="GPU:%s/%sGB,%s".formatted(SystemInfoHelper.getGpuName(), SystemInfoHelper.getGpuVRam()/1024/1024/1024, SystemInfoHelper.getGpu().getVersionInfo());
        this.debugInfoRight[7]="声卡:%s,%s".formatted(SystemInfoHelper.getSoundCardName(),SystemInfoHelper.getSoundCard().getDriverVersion());
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
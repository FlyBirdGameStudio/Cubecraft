package io.flybird.cubecraft.client.gui.screen;

import io.flybird.cubecraft.client.gui.DisplayScreenInfo;
import io.flybird.cubecraft.client.gui.ScreenLoader;
import io.flybird.cubecraft.client.render.renderer.ChunkRenderer;
import io.flybird.cubecraft.client.render.renderer.EntityRenderer;
import io.flybird.cubecraft.client.render.renderer.EnvironmentRenderer;
import io.flybird.cubecraft.resources.ResourceLocation;
import io.flybird.cubecraft.resources.ResourceManager;
import io.flybird.cubecraft.world.HittableObject;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.cubecraft.world.item.Inventory;
import io.flybird.starfish3d.platform.KeyPressEvent;
import io.flybird.starfish3d.platform.input.Keyboard;
import io.flybird.starfish3d.platform.input.KeyboardCallback;
import io.flybird.starfish3d.platform.input.Mouse;
import io.flybird.starfish3d.platform.input.MouseCallBack;
import io.flybird.starfish3d.render.GLUtil;
import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.starfish3d.render.textures.Texture2D;
import io.flybird.util.JVMInfo;
import io.flybird.util.event.EventHandler;
import io.flybird.util.math.HitResult;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;

public class HUDScreen extends Screen {
    private final Texture2D actionBar = new Texture2D(false, false);
    private final Texture2D pointer = new Texture2D(false, false);

    private int slot;

    private boolean showGUI = true;

    public HUDScreen() {
        super("cubecraft:hud_screen", ScreenType.EMPTY);
        this.actionBar.generateTexture();
        this.actionBar.load(ResourceManager.instance.getResource(ResourceLocation.uiTexture("cubecraft","containers/actionbar.png")));
        this.pointer.generateTexture();
        this.pointer.load(ResourceManager.instance.getResource(ResourceLocation.uiTexture("cubecraft","icons/pointer.png")));
    }

    @Override
    public void init() {
        Mouse.setGrabbed(true);
    }

    @Override
    public void render(DisplayScreenInfo info, float interpolationTime) {
        super.render(info, interpolationTime);
        GLUtil.enableBlend();
        this.getPlatform().controller.tickFast();
        if (showGUI) {
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

    @EventHandler
    public void onKeyEventPressed(KeyPressEvent event) {
        if (event.key() == Keyboard.KEY_ESCAPE) {
            Mouse.setGrabbed(false);
        }
        if (event.key() == Keyboard.KEY_F1) {
            HUDScreen.this.showGUI = !HUDScreen.this.showGUI;
        }
    }

    @Override
    public Screen getParentScreen() {
        return ScreenLoader.loadByExtName("cubecraft","pause_screen.xml");
    }

    @Override
    public void tick() {
        getPlatform().controller.tick();
        getPlatform().controller.setSelectedSlot(slot);
        super.tick();
    }

    @Override
    public void getDebugInfoTick() {
        this.debugInfoLeft[2] = "ClientTPS：" + this.getPlatform().getTimingInfo().longTickTPS() + "/MSPT:" + this.getPlatform().getTimingInfo().longTickMSPT();
        this.debugInfoLeft[3] = "LocalChunkCache:%d".formatted(this.getPlatform().getClientWorld().getChunkCache().size());
        ChunkRenderer cr = (ChunkRenderer) this.getPlatform().levelRenderer.renderers.get("cubecraft:chunk_renderer");
        if(cr!=null) {
            this.debugInfoLeft[5] = "TerrainRender(All/Visible/Update):%d,%d,%d".formatted(cr.allCount, cr.visibleCount, cr.updateCount);
        }

        EntityRenderer er = (EntityRenderer) this.getPlatform().levelRenderer.renderers.get("cubecraft:entity_renderer");
        this.debugInfoLeft[6] = "EntityRender(All/Visible):%d/%d".formatted(er.allCount,er.visibleCount);

        EnvironmentRenderer evr = this.getPlatform().levelRenderer.environmentRenderer;
        this.debugInfoLeft[7] = "CloudRender(All/Visible):%d/%d".formatted(evr.allCloudCount,evr.visibleCloudCount);

        this.debugInfoLeft[8] = "Position(X/Y/Z):%f/%f/%f".formatted(this.getPlatform().getPlayer().x, this.getPlatform().getPlayer().y, this.getPlatform().getPlayer().z);
        this.debugInfoLeft[9] = "CameraAngle(X/Y/Z):%f/%f/%f".formatted(this.getPlatform().getPlayer().xRot, this.getPlatform().getPlayer().yRot, this.getPlatform().getPlayer().zRot);
        HitResult hitResult = this.getPlatform().getPlayer().hitResult;
        if(hitResult!=null) {
            Vector3d pos=hitResult.aabb().getPosition();
            this.debugInfoLeft[10] = "TargetBlock:%f/%f/%f:%d=%s".formatted(
                pos.x,pos.y,pos.z,hitResult.facing(),
                    this.getPlatform().getPlayer().world.getBlockState((long) pos.x, (long) pos.y, (long) pos.z).getId()
            );
        }else{
            this.debugInfoLeft[10] = "TargetBlock:empty";
        }
        this.debugInfoRight[1] = "Memory(jvm)(Used/All)：%s/%s(%s)".formatted(JVMInfo.getUsedMemory(), JVMInfo.getTotalMemory(), JVMInfo.getUsage());
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
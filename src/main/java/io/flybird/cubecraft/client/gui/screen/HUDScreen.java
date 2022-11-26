package io.flybird.cubecraft.client.gui.screen;

import io.flybird.cubecraft.client.gui.DisplayScreenInfo;
import io.flybird.cubecraft.client.gui.ScreenLoader;
import io.flybird.cubecraft.internal.renderer.ChunkRenderer;
import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.client.resources.ResourceManager;
import io.flybird.cubecraft.world.HittableObject;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.cubecraft.world.item.Inventory;
import io.flybird.starfish3d.event.KeyPressEvent;
import io.flybird.starfish3d.event.MouseClickEvent;
import io.flybird.starfish3d.event.MouseScrollEvent;
import io.flybird.starfish3d.platform.Keyboard;
import io.flybird.starfish3d.platform.Mouse;
import io.flybird.starfish3d.render.GLUtil;
import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.starfish3d.render.textures.Texture2D;
import io.flybird.util.DebugInfoHandler;
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
        super(true, "cubecraft:hud_screen", ScreenType.EMPTY);
        this.actionBar.generateTexture();
        this.actionBar.load(ResourceManager.instance.getResource(ResourceLocation.uiTexture("cubecraft", "containers/actionbar.png")));
        this.pointer.generateTexture();
        this.pointer.load(ResourceManager.instance.getResource(ResourceLocation.uiTexture("cubecraft", "icons/pointer.png")));
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

    @EventHandler
    public void onScroll(MouseScrollEvent e) {
        int i = e.v();
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

    @EventHandler
    public void onClicked(MouseClickEvent e) {
        if (e.button() == 0) {
            Mouse.setGrabbed(true);
            HUDScreen.this.getPlatform().getPlayer().attack();
        }
        if (e.button() == 1) {
            HUDScreen.this.getPlatform().getPlayer().interact();
        }
        if (e.button() == 2) {
            HitResult hitResult = HUDScreen.this.getPlatform().getPlayer().hitResult;
            if (hitResult != null) {
                HittableObject obj = HUDScreen.this.getPlatform().getPlayer().hitResult.aabb().getObject();
                Inventory inv = HUDScreen.this.getPlatform().getPlayer().getInventory();
                if (obj instanceof BlockState) {
                    inv.selectItem();
                }
            }
        }
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
        return ScreenLoader.loadByExtName("cubecraft", "pause_screen.xml");
    }

    @Override
    public void tick() {
        getPlatform().controller.tick();
        getPlatform().controller.setSelectedSlot(slot);
        super.tick();
    }

    @Override
    public void getDebugInfoTick() {
        DebugInfoHandler handler=Registry.getDebugInfoHandler();

        handler.putD("cubecraft:client_player/x",this.getPlatform().getPlayer().x);
        handler.putD("cubecraft:client_player/y",this.getPlatform().getPlayer().y);
        handler.putD("cubecraft:client_player/z",this.getPlatform().getPlayer().z);

        handler.putD("cubecraft:client_player/xr",this.getPlatform().getPlayer().xRot);
        handler.putD("cubecraft:client_player/yr",this.getPlatform().getPlayer().yRot);
        handler.putD("cubecraft:client_player/zr",this.getPlatform().getPlayer().zRot);

        this.debugInfoLeft[2] = "ClientTPS：" + this.getPlatform().getTimingInfo().longTickTPS() + "/MSPT:" + this.getPlatform().getTimingInfo().longTickMSPT();
        this.debugInfoLeft[3] = "LocalChunkCache:%d".formatted(this.getPlatform().getClientWorld().getChunkCache().size());
        ChunkRenderer cr = (ChunkRenderer) this.getPlatform().levelRenderer.renderers.get("cubecraft:chunk_renderer");
        if (cr != null) {
            this.debugInfoLeft[5] = "TerrainRender(All/Visible/Update):%d,%d,%d".formatted(cr.allCount, cr.visibleCount, cr.updateCount);
        }


        this.debugInfoLeft[6] = "EntityRender(All/Visible):%d/%d".formatted(
                handler.getI("cubecraft:entity_renderer/all_entities"),
                handler.getI("cubecraft:entity_renderer/visible_entities")
        );
        this.debugInfoLeft[7] = "CloudRender(All/Visible):%d/%d".formatted(
                handler.getI("cubecraft:environment_renderer/all_clouds"),
                handler.getI("cubecraft:environment_renderer/visible_clouds")
        );
        this.debugInfoLeft[8] = "Position(X/Y/Z):%f/%f/%f".formatted(
                handler.getD("cubecraft:client_player/x"),
                handler.getD("cubecraft:client_player/y"),
                handler.getD("cubecraft:client_player/z")
        );
        this.debugInfoLeft[9] = "Facing(X/Y/Z):%f/%f/%f".formatted(
                handler.getD("cubecraft:client_player/xr"),
                handler.getD("cubecraft:client_player/yr"),
                handler.getD("cubecraft:client_player/zr")
        );


        HitResult hitResult = this.getPlatform().getPlayer().hitResult;
        if (hitResult != null) {
            Vector3d pos = hitResult.aabb().getPosition();
            this.debugInfoLeft[10] = "TargetBlock:%f/%f/%f:%d=%s".formatted(
                    pos.x, pos.y, pos.z, hitResult.facing(),
                    this.getPlatform().getPlayer().world.getBlockState((long) pos.x, (long) pos.y, (long) pos.z).getId()
            );
        } else {
            this.debugInfoLeft[10] = "TargetBlock:empty";
        }
        this.debugInfoRight[1] = "Memory(jvm)(Used/All)：%s/%s(%s)".formatted(JVMInfo.getUsedMemory(), JVMInfo.getTotalMemory(), JVMInfo.getUsage());
    }

    private void renderActionBar(DisplayScreenInfo info) {
        final float scale = 1.263f;
        GL11.glPushMatrix();
        GL11.glTranslated(info.centerX() - 91 * scale, info.scrHeight() - 22 * scale, 0);
        this.actionBar.bind();
        ShapeRenderer.drawRectUV(
                0, 182 * scale, 0, 22 * scale, 0, 0,
                0, 182 / 256f, 0, 22 / 32f
        );
        float slotBase = slot * 20 * scale;
        ShapeRenderer.drawRectUV(slotBase - 1 * scale, slotBase + 23 * scale, -1 * scale, 23 * scale, 0, 0, 232 / 256f, 1, 0, 24 / 32f);
        GL11.glPopMatrix();
    }
}
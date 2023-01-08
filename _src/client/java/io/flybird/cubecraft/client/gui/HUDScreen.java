package io.flybird.cubecraft.client.gui;

import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.gui.base.DisplayScreenInfo;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.client.gui.component.Screen;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.item.Inventory;
import io.flybird.quantum3d.BufferAllocation;
import io.flybird.quantum3d.event.KeyPressEvent;
import io.flybird.quantum3d.event.MouseClickEvent;
import io.flybird.quantum3d.event.MouseScrollEvent;
import io.flybird.quantum3d.platform.Keyboard;
import io.flybird.quantum3d.GLUtil;
import io.flybird.quantum3d.ShapeRenderer;
import io.flybird.quantum3d.textures.Texture2D;
import io.flybird.util.DebugInfoHandler;
import io.flybird.util.JVMInfo;
import io.flybird.util.event.EventHandler;
import io.flybird.util.math.HitResult;
import io.flybird.util.math.HittableObject;
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
        this.actionBar.load(ClientRegistries.RESOURCE_MANAGER.getResource(ResourceLocation.uiTexture("cubecraft", "containers/actionbar.png")));
        this.pointer.generateTexture();
        this.pointer.load(ClientRegistries.RESOURCE_MANAGER.getResource(ResourceLocation.uiTexture("cubecraft", "icons/pointer.png")));
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
                    0, 0, 1, 0, 1
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
            this.getPlatform().getWindow().setMouseGrabbed(true);
            this.getPlatform().getPlayer().attack();
        }
        if (e.button() == 1) {
            this.getPlatform().getPlayer().interact();
        }
        if (e.button() == 2) {
            HitResult<Entity, IWorld> hitResult = Registries.CLIENT.getPlayer().hitResult;
            if (hitResult != null) {
                HittableObject<Entity, IWorld> obj = Registries.CLIENT.getPlayer().hitResult.aabb().getObject();
                Inventory inv = HUDScreen.this.getPlatform().getPlayer().getInventory();
                inv.selectItem(obj, this.slot);
            }
        }
    }

    @EventHandler
    public void onKeyEventPressed(KeyPressEvent event) {
        if (event.key() == Keyboard.KEY_ESCAPE) {
            this.getPlatform().getWindow().setMouseGrabbed(false);
        }
        if (event.key() == Keyboard.KEY_F1) {
            HUDScreen.this.showGUI = !HUDScreen.this.showGUI;
        }
    }

    @Override
    public Screen getParentScreen() {
        return new GUIManager().loadFAML("cubecraft", "pause_screen.xml");
    }

    @Override
    public void tick() {
        getPlatform().controller.tick();
        getPlatform().controller.setSelectedSlot(slot);
        super.tick();
    }

    @Override
    public void getDebugInfoTick() {
        DebugInfoHandler handler = Registries.DEBUG_INFO;

        handler.putD("cubecraft:client_player/x", this.getPlatform().getPlayer().x);
        handler.putD("cubecraft:client_player/y", this.getPlatform().getPlayer().y);
        handler.putD("cubecraft:client_player/z", this.getPlatform().getPlayer().z);

        handler.putD("cubecraft:client_player/xr", this.getPlatform().getPlayer().xRot);
        handler.putD("cubecraft:client_player/yr", this.getPlatform().getPlayer().yRot);
        handler.putD("cubecraft:client_player/zr", this.getPlatform().getPlayer().zRot);

        this.debugInfoLeft[2] = "ClientTPS：" + this.getPlatform().getTimingInfo().longTickTPS() + "/MSPT:" + this.getPlatform().getTimingInfo().longTickMSPT();
        this.debugInfoLeft[3] = "LocalChunkCache:%d".formatted(this.getPlatform().getClientWorld().getChunkCache().size());

        this.debugInfoLeft[5] = "Terrain:(all/visible[a/t]/u):%d,[%d,%d],%d(c-time:%d)".formatted(
                Registries.DEBUG_INFO.getI("cubecraft:chunk_render/all"),
                Registries.DEBUG_INFO.getI("cubecraft:chunk_render/visible_alpha"),
                Registries.DEBUG_INFO.getI("cubecraft:chunk_render/visible_transparent"),
                Registries.DEBUG_INFO.getI("cubecraft:chunk_render/update"),
                Registries.DEBUG_INFO.getI("cubecraft:chunk_render/compile_time")
        );

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


        HitResult<Entity, IWorld> hitResult = this.getPlatform().getPlayer().hitResult;
        if (hitResult != null) {
            Vector3d pos = hitResult.aabb().getPosition();
            this.debugInfoLeft[10] = "TargetBlock:%d/%d/%d:%d=%s".formatted(
                    ((long) pos.x), ((long) pos.y), ((long) pos.z), hitResult.facing(),
                    this.getPlatform().getPlayer().world.getBlockState((long) pos.x, (long) pos.y, (long) pos.z).getId()
            );
        } else {
            this.debugInfoLeft[10] = "TargetBlock:empty";
        }
        this.debugInfoRight[1] = "Memory(jvm)(Used/All)：%s/%s(%s)[OffHeap:%dMB]".formatted(JVMInfo.getUsedMemory(), JVMInfo.getTotalMemory(), JVMInfo.getUsage(), BufferAllocation.getAllocSize() / 1024 / 1024);
    }

    private void renderActionBar(DisplayScreenInfo info) {
        final float scale = 1.263f;
        GL11.glPushMatrix();
        GL11.glTranslated(info.centerX() - 91 * scale, info.scrHeight() - 22 * scale, 0);
        this.actionBar.bind();
        ShapeRenderer.drawRectUV(
                0, 182 * scale, 0, 22 * scale, 0,
                0, 182 / 256f, 0, 22 / 32f
        );
        float slotBase = slot * 20 * scale;
        ShapeRenderer.drawRectUV(slotBase - 1 * scale, slotBase + 23 * scale, -1 * scale, 23 * scale, 0, 232 / 256f, 1, 0, 24 / 32f);
        GL11.glPopMatrix();
    }
}
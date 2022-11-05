package io.flybird.cubecraft.client.render.renderer;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.

public class LevelRenderer {
    private FloatBuffer fogColor0 = BufferUtils.createFloatBuffer(4);
    private FloatBuffer fogColor1 = BufferUtils.createFloatBuffer(4);
    private IntBuffer viewportBuffer = BufferUtils.createIntBuffer(16);
    private IntBuffer selectBuffer = BufferUtils.createIntBuffer(2000);
    public ParticleEngine particleEngine;
    private Level world;
    private final _Entity target;

    public LevelRenderer(Level world, _Entity target) {
        this.world = world;
        this.target = target;
        this.particleEngine = new ParticleEngine(this.world, null);
    }


    @Deprecated
    public void pick() {
        VertexArrayBuilder t = new VertexArrayBuilder(128);
        AABB box = target.collisionBox.grow(target.getReachDistance());
        int x0 = (int) box.x0;
        int x1 = (int) (box.x1 + 1.0f);
        int y0 = (int) box.y0;
        int y1 = (int) (box.y1 + 1.0f);
        int z0 = (int) box.z0;
        int z1 = (int) (box.z1 + 1.0f);
        GL11.glInitNames();
        GL11.glPushName(0);
        GL11.glPushName(0);
        for (int x = x0; x < x1; ++x) {
            GL11.glLoadName(x);
            GL11.glPushName(0);
            for (int y = y0; y < y1; ++y) {
                GL11.glLoadName(y);
                GL11.glPushName(0);
                for (int z = z0; z < z1; ++z) {
                    Tile tile = null; //Tile.tiles[this.world.getTile(x, y, z)];
                    if (tile == null || !tile.mayPick()) continue;
                    GL11.glLoadName(z);
                    GL11.glPushName(0);
                    for (int i = 0; i < 6; ++i) {
                        GL11.glLoadName(i);
                        t.begin();
                        tile.renderFaceNoTexture((Player) target, t, x, y, z, i);
                        t.end();
                        VertexArrayUploader.upload(t);
                    }
                    GL11.glPopName();
                }
                GL11.glPopName();
            }
            GL11.glPopName();
        }
        GL11.glPopName();
        GL11.glPopName();
    }

    public void renderHit() {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.4f);
        GL11.glBegin(3);
        GL11.glVertex3f((float) this.target.hitResult.x, (float) this.target.hitResult.y, (float) this.target.hitResult.z);
        GL11.glVertex3f((float) this.target.hitResult.x + 1.0f, (float) this.target.hitResult.y, (float) this.target.hitResult.z);
        GL11.glVertex3f((float) this.target.hitResult.x + 1.0f, (float) this.target.hitResult.y, (float) this.target.hitResult.z + 1.0f);
        GL11.glVertex3f((float) this.target.hitResult.x, (float) this.target.hitResult.y, (float) this.target.hitResult.z + 1.0f);
        GL11.glVertex3f((float) this.target.hitResult.x, (float) this.target.hitResult.y, (float) this.target.hitResult.z);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex3f((float) this.target.hitResult.x, (float) this.target.hitResult.y + 1.0f, (float) this.target.hitResult.z);
        GL11.glVertex3f((float) this.target.hitResult.x + 1.0f, (float) this.target.hitResult.y + 1.0f, (float) this.target.hitResult.z);
        GL11.glVertex3f((float) this.target.hitResult.x + 1.0f, (float) this.target.hitResult.y + 1.0f, (float) this.target.hitResult.z + 1.0f);
        GL11.glVertex3f((float) this.target.hitResult.x, (float) this.target.hitResult.y + 1.0f, (float) this.target.hitResult.z + 1.0f);
        GL11.glVertex3f((float) this.target.hitResult.x, (float) this.target.hitResult.y + 1.0f, (float) this.target.hitResult.z);
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex3f((float) this.target.hitResult.x, (float) this.target.hitResult.y, (float) this.target.hitResult.z);
        GL11.glVertex3f((float) this.target.hitResult.x, (float) this.target.hitResult.y + 1.0f, (float) this.target.hitResult.z);
        GL11.glVertex3f((float) this.target.hitResult.x + 1.0f, (float) this.target.hitResult.y, (float) this.target.hitResult.z);
        GL11.glVertex3f((float) this.target.hitResult.x + 1.0f, (float) this.target.hitResult.y + 1.0f, (float) this.target.hitResult.z);
        GL11.glVertex3f((float) this.target.hitResult.x + 1.0f, (float) this.target.hitResult.y, (float) this.target.hitResult.z + 1.0f);
        GL11.glVertex3f((float) this.target.hitResult.x + 1.0f, (float) this.target.hitResult.y + 1.0f, (float) this.target.hitResult.z + 1.0f);
        GL11.glVertex3f((float) this.target.hitResult.x, (float) this.target.hitResult.y, (float) this.target.hitResult.z + 1.0f);
        GL11.glVertex3f((float) this.target.hitResult.x, (float) this.target.hitResult.y + 1.0f, (float) this.target.hitResult.z + 1.0f);
        GL11.glEnd();
        GL11.glDisable(3042);
    }



    private void setupFog(int i) {
        if (i == 0) {
            GL11.glFogi(2917, 2048);
            GL11.glFogf(2914, 0.001f);
            GL11.glFog(2918, this.fogColor0);
            GL11.glLightModel(2899, BufferBuilder.getF(1.0f, 1.0f, 1.0f, 1.0f));
        } else {
            GL11.glFogi(2917, 2048);
            GL11.glFogf(2914, 0.01f);
            GL11.glFog(2918, this.fogColor1);
            float br = 0.6f;
            GL11.glLightModel(2899, BufferBuilder.getF(br, br, br, 1.0f));
        }
        GL11.glEnable(2903);
        GL11.glColorMaterial(1028, 4608);
        GL11.glEnable(2896);
    }
}


 */


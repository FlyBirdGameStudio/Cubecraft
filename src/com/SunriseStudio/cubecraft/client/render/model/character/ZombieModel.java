package com.sunrisestudio.cubecraft.client.render.model.character;

public class ZombieModel {
    public Cube head = new Cube(0, 0);
    public Cube body;
    public Cube arm0;
    public Cube arm1;
    public Cube leg0;
    public Cube leg1;

    public ZombieModel() {
        this.head.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8);
        this.body = new Cube(16, 16);
        this.body.addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4);
        this.arm0 = new Cube(40, 16);
        this.arm0.addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4);
        this.arm0.setPos(-5.0f, 2.0f, 0.0f);
        this.arm1 = new Cube(40, 16);
        this.arm1.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4);
        this.arm1.setPos(5.0f, 2.0f, 0.0f);
        this.leg0 = new Cube(0, 16);
        this.leg0.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4);
        this.leg0.setPos(-2.0f, 12.0f, 0.0f);
        this.leg1 = new Cube(0, 16);
        this.leg1.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4);
        this.leg1.setPos(2.0f, 12.0f, 0.0f);
    }

    public void render(float time) {
        this.head.yRot = (float)Math.sin((double)time * 0.83) * 1.0f;
        this.head.xRot = (float)Math.sin(time) * 0.8f;
        this.arm0.xRot = (float)Math.sin((double)time * 0.6662 + Math.PI) * 2.0f;
        this.arm0.zRot = (float)(Math.sin((double)time * 0.2312) + 1.0) * 1.0f;
        this.arm1.xRot = (float)Math.sin((double)time * 0.6662) * 2.0f;
        this.arm1.zRot = (float)(Math.sin((double)time * 0.2812) - 1.0) * 1.0f;
        this.leg0.xRot = (float)Math.sin((double)time * 0.6662) * 1.4f;
        this.leg1.xRot = (float)Math.sin((double)time * 0.6662 + Math.PI) * 1.4f;
        this.head.render();
        this.body.render();
        this.arm0.render();
        this.arm1.render();
        this.leg0.render();
        this.leg1.render();
    }
}

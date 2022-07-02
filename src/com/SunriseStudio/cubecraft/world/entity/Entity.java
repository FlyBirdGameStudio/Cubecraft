package com.SunriseStudio.cubecraft.world.entity;

import com.SunriseStudio.cubecraft.util.math.AABB;
import com.SunriseStudio.cubecraft.util.math.HitBox;
import com.SunriseStudio.cubecraft.util.math.HitResult;
import com.SunriseStudio.cubecraft.util.nbt.NBTDataIO;
import com.SunriseStudio.cubecraft.util.nbt.NBTTagCompound;
import com.SunriseStudio.cubecraft.world.HittableObject;
import com.SunriseStudio.cubecraft.world.block.BlockFacing;
import com.SunriseStudio.cubecraft.world.IDimensionAccess;
import com.SunriseStudio.cubecraft.world.entity.item.Item;
import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Entity implements HittableObject, NBTDataIO {
    private String uuid;
    public HitResult hitResult;
    protected IDimensionAccess world;
    public boolean runningMode;
    public boolean flyingMode = true;

    //position
    public double xo;
    public double yo;
    public double zo;
    public double x;
    public double y;
    public double z;

    //movement
    public double xd;
    public double yd;
    public double zd;

    public double xa = 0.0;
    public double za = 0.0;

    //rotation
    public float yRot;
    public float xRot;
    public float zRot;

    //physic
    public AABB collisionBox;
    public boolean onGround = false;
    public boolean horizontalCollision = false;

    public Entity(IDimensionAccess world) {
        this.world = world;
        this.resetPos();
        this.uuid = UUID.nameUUIDFromBytes(String.valueOf(System.currentTimeMillis()+this.hashCode()).getBytes(StandardCharsets.UTF_8)).toString();
    }

    protected void resetPos() {
        this.setPos(x, y, z);
    }

//  ------ moving ------

    /**
     * set position of entity
     *
     * @param x x
     * @param y y
     * @param z z
     */
    public void setPos(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.collisionBox = getCollisionBoxSize();
        this.collisionBox.move(x, y, z);
    }

    /**
     * this is offset but not set.yaw value will clamp to -90~90（degree）
     *
     * @param xo yaw
     * @param yo pitch
     * @param zo roll
     */
    public void turn(float xo, float yo, float zo) {
        this.yRot = (this.yRot + xo * 0.15f);
        this.xRot = (this.xRot - yo * 0.15f);
        this.zRot = (this.zRot - zo * 0.15f);
        if (this.xRot < -90.0f) {
            this.xRot = -90.0f;
        }
        if (this.xRot > 90.0f) {
            this.xRot = 90.0f;
        }
    }

    /**
     * test collision and move in 3 axis
     *
     * @param xa x momentum
     * @param ya y momentum
     * @param za z momentum
     */
    public void move(double xa, double ya, double za) {
        int i;
        double xaOrg = xa;
        double yaOrg = ya;
        double zaOrg = za;
        ArrayList<AABB> aABBs = this.world.getCollisionBox(this.collisionBox.expand(xa, ya, za));
        for (i = 0; i < aABBs.size(); ++i) {
            if (aABBs.get(i) != null)
                ya = ((AABB) aABBs.get(i)).clipYCollide(this.collisionBox, ya);
        }
        this.collisionBox.move(0.0f, ya, 0.0f);
        for (i = 0; i < aABBs.size(); ++i) {
            if (aABBs.get(i) != null)
                xa = ((AABB) aABBs.get(i)).clipXCollide(this.collisionBox, xa);
        }
        this.collisionBox.move(xa, 0.0f, 0.0f);
        for (i = 0; i < aABBs.size(); ++i) {
            if (aABBs.get(i) != null)
                za = ((AABB) aABBs.get(i)).clipZCollide(this.collisionBox, za);
        }
        this.collisionBox.move(0.0f, 0.0f, za);
        this.horizontalCollision = xaOrg != xa || zaOrg != za;
        boolean bl = this.onGround = yaOrg != ya && yaOrg < 0.0f;
        if (xaOrg != xa) {
            this.xd = 0.0f;
        }
        if (yaOrg != ya) {
            this.yd = 0.0f;
        }
        if (zaOrg != za) {
            this.zd = 0.0f;
        }
        this.x = (this.collisionBox.x0 + this.collisionBox.x1) / 2.0f;
        this.y = this.collisionBox.y0;
        this.z = (this.collisionBox.z0 + this.collisionBox.z1) / 2.0f;
    }

    /**
     * simulate relative move and turn them into momentum in 3 axis
     *
     * @param xa    front and back
     * @param za    left and right
     * @param speed IDK what is that
     */
    public void moveRelative(double xa, double za, float speed) {
        double dist = xa * xa + za * za;
        if (dist < 0.01f) {
            return;
        }
        dist = speed / (float) Math.sqrt(dist);
        float sin = (float) Math.sin((double) this.yRot * Math.PI / 180.0);
        float cos = (float) Math.cos((double) this.yRot * Math.PI / 180.0);
        this.xd += (xa *= dist) * cos - (za *= dist) * sin;
        this.zd += za * cos + xa * sin;
    }


//  ------ entity meta ------

    /**
     * each entity should only contain one collision box.
     *
     * @return collision box
     */
    public abstract AABB getCollisionBoxSize();

    /**
     * get All hit able boxes
     *
     * @return boxes
     */
    public abstract HitBox[] getSelectionBoxes();

    /**
     * define where the camera is from entity.
     *
     * @return relative position
     */
    public abstract Vector3d getCameraPosition();

    /**
     * define reach distance of an entity
     *
     * @return distance(blocks)
     */
    public double getReachDistance() {
        return 2.5;
    }

    /**
     * id of an entity
     *
     * @return id
     */
    public abstract String getID();

    public abstract int getHealth();

    public abstract Item[] getDrop();

    /**
     * default method updates entity position and also process moving.
     * also do hit process.
     * any sub entity should call "super.tick()"
     */
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.clipSelectionBox();

        float speed;

        if (flyingMode) {
            if (runningMode) {
                speed = 2.77f;
            } else {
                speed = 2f;
            }
        } else {
            if (runningMode) {
                speed = 1.58f;
            } else {
                speed = 1.0f;
            }
        }

        speed *= this.world.getBlock((long) x, (long) y, (long) z).getResistance();

        if (true) {//water
            this.moveRelative(xa, za, 0.027f * speed);
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.8f;
            this.yd *= 0.8f;
            this.zd *= 0.8f;
            if (!flyingMode) {
                this.yd -= 0.08;
            }
            if (this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6f - this.y + yo, this.zd)) {
                this.yd = 0.3f;
            }

        } else if (true) {//in lava
            this.moveRelative(xa, za, 0.02f * speed);
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.5f;
            this.yd *= 0.5f;
            this.zd *= 0.5f;
            if (!flyingMode) {
                this.yd -= 0.02;
            }
            if (this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6f - this.y + yo, this.zd)) {
                this.yd = 0.3f;
            }
        } else {
            this.moveRelative(xa, za, this.onGround ? 0.1f * speed : 0.02f * speed);
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.91f;
            this.yd *= 0.98f;
            this.zd *= 0.91f;
            if (!flyingMode) {
                this.yd -= 0.08;
            } else {
                this.yd *= 0.5f;
            }
            if (this.onGround) {
                this.xd *= 0.6f;
                this.zd *= 0.6f;
            }
        }
    }


    /**
     * calculate where the hit result of entity based on coord and rotation
     */
    public void clipSelectionBox() {
        HitBox hitbox = null;
        int f = 114514;
        List<HitBox> aABBs = this.world.getSelectionBox(this);
        Vector3d to = new Vector3d(getCameraPosition().add(getReachDistance(), 0, 0)).rotate(new Quaterniond(this.xRot, this.yRot, this.zRot, 1.0f));

        double xaOrg = to.x;
        double yaOrg = to.y;
        double zaOrg = to.z;

        AABB rayCollider = new AABB(to.x - 0.001, to.y - 0.001, to.z - 0.001, to.x + 0.001, to.y + 0.001, to.z + 0.001);

        //check which box collides ray.

        //IDK what but i`d like to check x axis first
        for (HitBox aabb : aABBs) {
            if (aabb != null) {
                to.y = aabb.clipXCollide(rayCollider, to.y);
                //if hit any box then it is the target(x<orgX means dist is clipped)
                if (Math.abs(to.y) < Math.abs(yaOrg)) {
                    hitbox = aabb;
                    break;
                }
            }
        }
        for (HitBox aabb : aABBs) {
            if (aabb != null) {
                to.x = aabb.clipXCollide(rayCollider, to.x);
                if (Math.abs(to.x) < Math.abs(xaOrg)) {
                    hitbox = aabb;
                    break;
                }
            }
        }
        for (HitBox aabb : aABBs) {
            if (aabb != null) {
                to.z = aabb.clipXCollide(rayCollider, to.z);
                if (Math.abs(to.z) < Math.abs(zaOrg)) {
                    hitbox = aabb;
                    break;
                }
            }
        }

        //check facing of hit
        if (to.y >= hitbox.y1) {
            f = 0;
        }
        if (to.y <= hitbox.y0) {
            f = 1;
        }
        if (to.z >= hitbox.z1) {
            f = 2;
        }
        if (to.z <= hitbox.z0) {
            f = 3;
        }
        if (to.x >= hitbox.x1) {
            f = 4;
        }
        if (to.x <= hitbox.x0) {
            f = 5;
        }
        this.hitResult = new HitResult(BlockFacing.fromId(f), hitbox);
    }

    public boolean isFree(double xa, double ya, double za) {
        AABB box = this.collisionBox.cloneMove(xa, ya, za);
        ArrayList<AABB> aABBs = this.world.getCollisionBox(box);
        if (aABBs.size() > 0) {
            return false;
        }
        return true;//!this.world.containsAnyLiquid(box);
    }

    /*
    public boolean isInWater() {
        return this.world.containsLiquid(this.collisionBox.grow(0.0f, -0.4f, 0.0f), 1);
    }

    public boolean isInLava() {
        return this.world.containsLiquid(this.collisionBox, 2);
    }
     */

    public abstract void render(float interpolationTime);


//  ------ data ------

    public boolean shouldSave() {
        return true;
    }

    public String getUID() {
        return uuid;
    }

    @Override
    public NBTTagCompound getData() {
        NBTTagCompound compound = new NBTTagCompound();

        //basic
        compound.setString("uuid", this.uuid);
        compound.setBoolean("flying", this.flyingMode);

        //physics
        NBTTagCompound physics = new NBTTagCompound();
        physics.setDouble("motion-x", this.xd);
        physics.setDouble("motion-y", this.yd);
        physics.setDouble("motion-z", this.zd);
        physics.setDouble("x", this.x);
        physics.setDouble("y", this.y);
        physics.setDouble("z", this.z);
        physics.setFloat("yaw", this.xRot);
        physics.setFloat("pitch", this.yRot);
        physics.setFloat("roll", this.zRot);
        compound.setCompoundTag("physics", physics);

        return compound;
    }

    @Override
    public void setData(NBTTagCompound tag) {
        //basic
        this.uuid = tag.getString("uuid");
        this.flyingMode = tag.getBoolean("flying");

        //physics
        NBTTagCompound physics = tag.getCompoundTag("physics");
        this.xd = physics.getDouble("motion-x");
        this.yd = physics.getDouble("motion-y");
        this.zd = physics.getDouble("motion-z");
        this.x = physics.getDouble("x");
        this.y = physics.getDouble("y");
        this.z = physics.getDouble("z");
        this.xRot = physics.getFloat("yaw");
        this.yRot = physics.getFloat("pitch");
        this.zRot = physics.getFloat("roll");
    }

    public void die() {
    }
}

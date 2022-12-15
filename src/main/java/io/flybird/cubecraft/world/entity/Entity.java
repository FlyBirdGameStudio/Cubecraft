package io.flybird.cubecraft.world.entity;

import io.flybird.cubecraft.register.Registries;
import io.flybird.util.math.HitResult;
import io.flybird.util.math.HittableObject;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.item.Inventory;
import io.flybird.cubecraft.world.entity.item.Item;
import io.flybird.util.file.NBTDataIO;
import io.flybird.util.file.nbt.NBTTagCompound;
import io.flybird.util.math.*;
import org.joml.Vector3d;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class Entity implements HittableObject<Entity,IWorld>, NBTDataIO {
    public static final String AUTO_REGISTER_SPAWN_EGG_ID = "_spawn_egg";
    private final Inventory inventory;
    public boolean sneak = false;
    protected String uuid;


    public HitResult<Entity, IWorld> hitResult;
    public IWorld world;
    public boolean runningMode;
    public boolean flyingMode = false;

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

    //rotation
    public float yRot;
    public float xRot;
    public float zRot;

    //physic
    public AABB collisionBox;
    public boolean onGround = false;
    public boolean horizontalCollision = false;

    public Entity(IWorld world) {
        this.inventory= Registries.INVENTORY.create(this.getID());
        this.world = world;
        this.resetPos();
        this.uuid = UUID.nameUUIDFromBytes(String.valueOf(System.currentTimeMillis() ^ this.hashCode()).getBytes(StandardCharsets.UTF_8)).toString();
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
     * this is offset but not set. Yaw value will clamp to -90~90（degree）
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

        if (this.yRot < -180.0f) {
            this.yRot = 180.0f;
        }
        if (this.yRot > 180.0f) {
            this.yRot = -180.0f;
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
                ya = aABBs.get(i).clipYCollide(this.collisionBox, ya);
        }
        this.collisionBox.move(0.0f, ya, 0.0f);
        for (i = 0; i < aABBs.size(); ++i) {
            if (aABBs.get(i) != null)
                xa = aABBs.get(i).clipXCollide(this.collisionBox, xa);
        }
        this.collisionBox.move(xa, 0.0f, 0.0f);
        for (i = 0; i < aABBs.size(); ++i) {
            if (aABBs.get(i) != null)
                za = aABBs.get(i).clipZCollide(this.collisionBox, za);
        }
        this.collisionBox.move(0.0f, 0.0f, za);
        this.horizontalCollision = xaOrg != xa || zaOrg != za;
        this.onGround = yaOrg != ya && yaOrg < 0.0f;
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
    public abstract HitBox<Entity,IWorld>[] getSelectionBoxes();

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
        return 10;
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


        if (this.inLiquid()) {//water

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
        } else {
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
        this.hitResult = null;
        Vector3d from = new Vector3d(x, y + 1.62, z);

        this.hitResult = RayTest.<Entity,IWorld>rayTrace(world.getSelectionBox(this, from, getHitTargetPos()), from, getHitTargetPos());
    }

    public Vector3d getHitTargetPos() {
        Vector3d from = new Vector3d(x, y + 1.62, z);
        Vector3d to = MathHelper.getVectorForRotation(xRot, yRot - 180);
        return to.mul(getReachDistance()).add(from);
    }

    public boolean isFree(double xa, double ya, double za) {
        AABB box = this.collisionBox.cloneMove(xa, ya, za);
        ArrayList<AABB> aABBs = this.world.getCollisionBox(box);
        return aABBs.size() <= 0;//!this.world.containsAnyLiquid(box);
    }

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

        compound.setCompoundTag("inventory",this.inventory.getData());

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

    public void setWorld(IWorld world) {
        if(this.world!=null){
            this.world.removeEntity(this);
        }
        this.world = world;
    }

    //event
    public void onInteract(Entity from) {
    }

    public boolean inLiquid() {
        //return this.world.getBlock((long) x, (long) y, (long) z) instanceof LiquidBlockState;
        return false;
    }


    public void attack() {
        if (this.hitResult != null) {
            this.hitResult.hit(this,this.world);
        }
    }

    public void interact() {
        if (this.hitResult != null) {
            this.hitResult.interact(this,world);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }


    public String selectedBlockID = "cubecraft:air";

    @Deprecated
    public String getSelectBlock() {
        return selectedBlockID;
    }

    public void setLocation(EntityLocation location, HashMap<String,IWorld> worlds) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.xRot = (float) location.getXRot();
        this.yRot = (float) location.getYRot();
        this.zRot = (float) location.getZRot();
        this.setWorld(worlds.get(location.getDim()));
    }

    @Override
    public void onHit(Entity from, IWorld world, HitResult<Entity,IWorld> hr) {
        //todo
    }

    @Override
    public void onInteract(Entity from, IWorld world, HitResult<Entity,IWorld> hr) {
        //todo
    }
}

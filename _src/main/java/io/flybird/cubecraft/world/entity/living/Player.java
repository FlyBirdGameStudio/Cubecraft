package io.flybird.cubecraft.world.entity.living;

import io.flybird.cubecraft.auth.Session;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.item.Item;

public class Player extends EntityLiving {
    private final Session session;

    public Player(IWorld world, Session session) {
        super(world);
        this.session = session;
        this.uuid= Registries.SESSION_SERVICE.get(session.getType()).genUUID(session);
        this.flyingMode=true;
    }

    @Override
    public Item[] getDrop() {
        return new Item[0];
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public double getReachDistance() {
        return 5;
    }

    public Session getSession() {
        return session;
    }
}

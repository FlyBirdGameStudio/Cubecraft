package io.flybird.cubecraft.internal.registry;

import io.flybird.cubecraft.internal.type.EntityType;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.entity.living.Player;
import io.flybird.util.container.namespace.ItemRegisterFunc;
import io.flybird.util.container.namespace.NameSpacedConstructingMap;

public class EntityRegistry {
    @ItemRegisterFunc
    public void reg(NameSpacedConstructingMap<Entity> map){
        map.registerItem(EntityType.PLAYER, Player.class);
    }
}

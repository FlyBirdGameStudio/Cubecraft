package io.flybird.cubecraft.internal.registry;

import io.flybird.cubecraft.internal.inventory.PlayerInventory;
import io.flybird.cubecraft.internal.type.InventoryType;
import io.flybird.cubecraft.world.item.Inventory;
import io.flybird.util.container.namespace.ItemRegisterFunc;
import io.flybird.util.container.namespace.NameSpacedConstructingMap;

public class InventoryRegistry {
    @ItemRegisterFunc
    public void reg(NameSpacedConstructingMap<Inventory> map){
        map.registerItem(InventoryType.PLAYER, PlayerInventory.class);
    }
}

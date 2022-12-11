package io.flybird.cubecraft.internal.block;

import io.flybird.cubecraft.world.block.EnumFacing;
import io.flybird.cubecraft.world.block.Block;
import io.flybird.cubecraft.world.block.SimpleBlock;
import io.flybird.util.container.namespace.FieldRegistry;
import io.flybird.util.container.namespace.FieldRegistryHolder;
import io.flybird.util.container.namespace.ItemGetter;
import io.flybird.util.math.AABB;

@FieldRegistryHolder(namespace = "cubecraft")
public class BlockBehaviorRegistry {
    @FieldRegistry(id = "air")
    public static final Block AIR = new SimpleBlock(
            EnumFacing.all(), new AABB[]{}, new AABB[]{},
            0, 0, 0, 0, false, "cubecraft:_air", new String[]{}, 0
    );

    @FieldRegistry(id = "stone")
    public static final Block STONE = new SimpleBlock(
            EnumFacing.all(),
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            0, 0, 3, 0, true, "cubecraft:stone", new String[]{}, 0
    );

    @FieldRegistry(id = "log")
    public static final Block LOG = new SimpleBlock(
            EnumFacing.all(),
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            0, 0, 0, 0, true, "cubecraft:_log", new String[]{}, 0
    );

    @FieldRegistry(id = "log")
    public static final Block LEAVES = new SimpleBlock(
            EnumFacing.all(),
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            0, 0, 0, 110, false, "cubecraft:_leaves", new String[]{}, 0
    );





    @ItemGetter(id = "block", namespace = "cubecraft")
    public Block block() {
        return new SimpleBlock(
                EnumFacing.all(),
                new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
                new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
                0, 0, 0, 0, true, "cubecraft:block", new String[]{}, 0
        );
    }

    @ItemGetter(id = "glass", namespace = "cubecraft")
    public Block glass() {
        return new SimpleBlock(
                EnumFacing.all(),
                new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
                new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
                0, 0, 0, 0, false, "cubecraft:glass", new String[]{}, 0
        );
    }

    @ItemGetter(id = "stone", namespace = "cubecraft")
    public Block stone() {
        return new SimpleBlock(
                EnumFacing.all(),
                new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
                new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
                0, 0, 3, 0, true, "cubecraft:stone", new String[]{}, 0
        );
    }


    @ItemGetter(id = "leaves", namespace = "cubecraft")
    public Block leaves() {
        return new SimpleBlock(
                EnumFacing.all(),
                new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
                new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
                0, 0, 0, 110, false, "cubecraft:_leaves", new String[]{}, 0
        );
    }


    @ItemGetter(id = "dirt", namespace = "cubecraft")
    public Block dirt() {
        return new SimpleBlock(
                EnumFacing.all(),
                new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
                new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
                0, 1, 1, 0, true, "cubecraft:_dirt", new String[]{}, 0
        );
    }

    @ItemGetter(id = "grass_block", namespace = "cubecraft")
    public Block grass_block() {
        return new SimpleBlock(
                EnumFacing.all(),
                new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
                new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
                0, 1, 1, 0, true, "cubecraft:_grass_block", new String[]{}, 0
        );
    }
}

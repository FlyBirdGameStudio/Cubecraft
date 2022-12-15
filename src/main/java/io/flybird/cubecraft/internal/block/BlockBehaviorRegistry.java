package io.flybird.cubecraft.internal.block;

import io.flybird.cubecraft.world.block.EnumFacing;
import io.flybird.cubecraft.world.block.Block;
import io.flybird.cubecraft.world.block.SimpleBlock;
import io.flybird.util.container.namespace.FieldRegistry;
import io.flybird.util.container.namespace.FieldRegistryHolder;
import io.flybird.util.container.namespace.ItemGetter;
import io.flybird.util.math.AABB;

//todo:implement block tag

/**
 * block behavior registry,auto register with field registry,
 * direct constant calls are allowed.
 *
 * @author GrassBlock2022
 */
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
            1, 22, 3, 0, true, "cubecraft:stone", new String[]{}, 0
    );

    @FieldRegistry(id = "log")
    public static final Block LOG = new SimpleBlock(
            EnumFacing.all(),
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            1, 20, 0, 0, true, "cubecraft:_log", new String[]{}, 0
    );

    @FieldRegistry(id = "log")
    public static final Block LEAVES = new SimpleBlock(
            EnumFacing.all(),
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            1, 12, 0, 110, false, "cubecraft:_leaves", new String[]{}, 0
    );

    @FieldRegistry(id="dirt")
    public static final Block DIRT = new SimpleBlock(
            EnumFacing.all(),
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            1, 16, 1, 0, true, "cubecraft:_dirt", new String[]{}, 0
    );

    @FieldRegistry(id="grass_block")
    public static final Block GRASS_BLOCK = new SimpleBlock(
            EnumFacing.all(),
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            1, 16, 1, 0, true, "cubecraft:_grass_block", new String[]{}, 0
    );

    @FieldRegistry(id="glass")
    public static final Block GLASS = new SimpleBlock(
            EnumFacing.all(),
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            1, 3, 2, 127, false, "cubecraft:_glass", new String[]{}, 0
    );

    @FieldRegistry(id="wool")
    public static final Block WOOL = new SimpleBlock(
            EnumFacing.all(),
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
            1, 0, 1, 0, true, "cubecraft:_wool", new String[]{}, 0
            );

    @ItemGetter(id = "block", namespace = "cubecraft")
    public Block block() {
        return new SimpleBlock(
                EnumFacing.all(),
                new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
                new AABB[]{new AABB(0, 0, 0, 1, 1, 1)},
                1, 0, 0, 0, true, "cubecraft:block", new String[]{}, 0
        );
    }
}

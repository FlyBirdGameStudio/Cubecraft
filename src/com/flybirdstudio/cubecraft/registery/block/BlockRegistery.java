package com.flybirdstudio.cubecraft.registery.block;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.material.Block;
import com.flybirdstudio.cubecraft.world.block.material.OverwrittenBlock;
import com.flybirdstudio.util.container.namespace.GetterDepend;
import com.flybirdstudio.util.container.namespace.NameSpaceItemGetter;

public class BlockRegistery {


    //environment
    @GetterDepend(id = "air",namespace = "cubecraft")
    @NameSpaceItemGetter(id = "air",namespace="cubecraft")
    public Block air(Block behavior){
        return new OverwrittenBlock(behavior);
    }

    //environment/stone
    @GetterDepend(id = "stone",namespace = "cubecraft")
    @NameSpaceItemGetter(id = "stone",namespace="cubecraft")
    public Block stone(Block behavior){
        return new OverwrittenBlock(behavior);
    }

    //environment/surface
    @GetterDepend(id="dirt",namespace = "cubecraft")
    @NameSpaceItemGetter(id = "dirt",namespace="cubecraft")
    public Block dirt(Block behavior){
        return new OverwrittenBlock(behavior);
    }

    @GetterDepend(id="block",namespace = "cubecraft")
    @NameSpaceItemGetter(id = "grass_block",namespace="cubecraft")
    public Block grassBlock(Block behavior){
        return new OverwrittenBlock(behavior);
    }

}

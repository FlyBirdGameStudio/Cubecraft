package com.flybirdstudio.cubecraft.registery;
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
        return new OverwrittenBlock(behavior){
            @Override
            public boolean shouldRender(IWorld world, long x, long y, long z) {
                return false;
            }
        };
    }

    //environment/stone
    @GetterDepend(id = "stone",namespace = "cubecraft")
    @NameSpaceItemGetter(id = "stone",namespace="cubecraft")
    public Block stone(Block behavior){
        return new OverwrittenBlock(behavior){
            @Override
            public int getTexture(int face) {
                return 1;
            }
        };
    }

    //environment/surface
    @GetterDepend(id="dirt",namespace = "cubecraft")
    @NameSpaceItemGetter(id = "dirt",namespace="cubecraft")
    public Block dirt(Block behavior){
        return new OverwrittenBlock(behavior){
            @Override
            public int getTexture(int face) {
                return 3;
            }
        };
    }

    @GetterDepend(id="block",namespace = "cubecraft")
    @NameSpaceItemGetter(id = "grass_block",namespace="cubecraft")
    public Block grassBlock(Block behavior){
        return new OverwrittenBlock(behavior){
            @Override
            public int getTexture(int face) {
                if(face==1){
                    return 0;
                }else if(face==0){
                    return 15;
                }else{
                    return 3;
                }
            }
        };
    }
}

package com.sunrisestudio.cubecraft.registery;
import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.cubecraft.world.block.material.Block;
import com.sunrisestudio.cubecraft.world.block.material.OverwrittenBlock;
import com.sunrisestudio.util.container.namespace.GetterDepend;
import com.sunrisestudio.util.container.namespace.NameSpaceItemGetter;

public class Blocks{


    //environment
    @GetterDepend(id = "air",namespace = "cubecraft")
    @NameSpaceItemGetter(id = "air",namespace="cubecraft")
    public Block air(Block behavior){
        return new OverwrittenBlock(behavior){
            @Override
            public boolean shouldRender(IWorldAccess world, long x, long y, long z) {
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
                return 2;
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
                    return 2;
                }else{
                    return 3;
                }
            }
        };
    }
}

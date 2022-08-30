package com.flybirdstudio.cubecraft.registery;

import com.flybirdstudio.cubecraft.client.render.model.RenderType;
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.LegacyBlockRenderer;
import com.flybirdstudio.util.container.namespace.NameSpaceItemGetter;

public class BlockRendererRegistry {
    @NameSpaceItemGetter(id = "grass_block", namespace = "cubecraft")
    public IBlockRenderer grass_block() {
        return new LegacyBlockRenderer(RenderType.ALPHA) {
            @Override
            public int getTexture(int face) {
                if (face == 1) {
                    return 0;
                } else if (face == 0) {
                    return 2;
                } else {
                    return 3;
                }
            }
        };
    }

    @NameSpaceItemGetter(id = "stone", namespace = "cubecraft")
    public IBlockRenderer stone() {
        return new LegacyBlockRenderer(RenderType.ALPHA) {
            @Override
            public int getTexture(int face) {
                return 1;
            }
        };
    }

    @NameSpaceItemGetter(id = "dirt", namespace = "cubecraft")
    public IBlockRenderer dirt() {
        return new LegacyBlockRenderer(RenderType.ALPHA) {
            @Override
            public int getTexture(int face) {
                return 2;
            }
        };
    }

    @NameSpaceItemGetter(id = "air", namespace = "cubecraft")
    public IBlockRenderer air() {
        return null;
    }
}

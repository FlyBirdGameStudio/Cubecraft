package io.flybird.cubecraft.internal;

import io.flybird.cubecraft.client.render.model.block.IColorMap;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.util.container.namespace.ItemGetter;

public class ColorMapRegistry {
    @ItemGetter(id="foliage",namespace = "cubecraft")
    public IColorMap foliage(){
        return new IColorMap(ResourceLocation.blockColorMap("cubecraft:foliage.png")) {
            @Override
            public int sample(IWorld w, BlockState bs, long x, long y, long z) {
                double t=w.getTemperature(x,y,z);
                double h=w.getHumidity(x,y,z);
                return this.image.getRGB((int) (h*this.image.getWidth()), (int) (t/this.image.getHeight()));
            }
        };
    }

    @ItemGetter(id="default",namespace = "cubecraft")
    public IColorMap _default(){
        return new IColorMap(ResourceLocation.blockColorMap("cubecraft:foliage.png")) {
            @Override
            public int sample(IWorld w, BlockState bs, long x, long y, long z) {
                return 0xFFFFFF;
            }
        };
    }

    @ItemGetter(id="grass",namespace = "cubecraft")
    public IColorMap grass(){
        return new IColorMap(ResourceLocation.blockColorMap("cubecraft:grass.png")) {
            @Override
            public int sample(IWorld w, BlockState bs, long x, long y, long z) {
                double t=w.getTemperature(x,y,z);
                double h=w.getHumidity(x,y,z);
                return this.image.getRGB((int) (h*this.image.getWidth()), (int) (t/this.image.getHeight()));
            }
        };
    }
}

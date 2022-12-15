package io.flybird.cubecraft.world.item;

import io.flybird.cubecraft.register.Registries;
import io.flybird.util.file.NBTDataIO;
import io.flybird.util.file.nbt.NBTTagCompound;
import io.flybird.util.math.MathHelper;

import java.util.Objects;

public class ItemStack implements NBTDataIO {
    private String type;
    private int counts;
    private NBTTagCompound nbt;

    public void merge(ItemStack another){
        Item item= Registries.ITEM.get(this.type);
        if(Objects.equals(another.getType(), this.type) &&another.nbt!=null&&this.nbt!=null){//物品id一致，且双方均无nbt，且该物品可堆叠
            int i= (int) MathHelper.clamp(another.getCounts(),Integer.MAX_VALUE,0);
            another.counts-=(item.getMaxStackCount()-i);
            this.counts+= item.getMaxStackCount()-i;
        }
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public String getType() {
        return type;
    }

    public NBTTagCompound getNbt() {
        return nbt;
    }

    @Override
    public NBTTagCompound getData() {
        NBTTagCompound tag=new NBTTagCompound();
        tag.setString("type",this.type);
        tag.setInteger("count",this.counts);
        tag.setCompoundTag("nbt",this.nbt);
        return tag;
    }

    @Override
    public void setData(NBTTagCompound tag) {
        this.nbt=tag.getCompoundTag("nbt");
        this.counts=tag.getInteger("count");
        this.type=tag.getString("type");
    }
}

package com.sunrisestudio.cubecraft.world.item;

import com.sunrisestudio.cubecraft.registery.Registry;
import com.sunrisestudio.util.file.nbt.tag.NBTTagCompound;
import com.sunrisestudio.util.math.MathHelper;

public class ItemStack{
    private String type;
    private int counts;
    private NBTTagCompound nbt;

    public void merge(ItemStack another){
        Item item= Registry.getItemMap().get(this.type);
        if(another.getType()==this.type&&another.nbt!=null&&this.nbt!=null){//物品id一致，且双方均无nbt，且该物品可堆叠
            this.counts+= item.getMaxStackCount()- MathHelper.clamp(another.getCounts(),Integer.MAX_VALUE,0);

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
}

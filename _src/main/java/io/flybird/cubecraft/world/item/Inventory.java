package io.flybird.cubecraft.world.item;

import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.util.file.NBTDataIO;
import io.flybird.util.file.nbt.NBTTagCompound;
import io.flybird.util.math.HittableObject;

import java.util.Objects;

public abstract class Inventory implements NBTDataIO {
    private final ItemSlot[] slots;

    public Inventory(int nSlots){
        this.slots=new ItemSlot[nSlots];
        for (int i=0;i<nSlots;i++){
            this.slots[i]=new ItemSlot();
        }
    }

    public void selectItem(HittableObject<Entity, IWorld> obj,int targetSlot) {
        String id;
        if(obj instanceof BlockState){
            id= ((BlockState) obj).getId();
        }else{
            id= ((Entity) obj).getID()+Entity.AUTO_REGISTER_SPAWN_EGG_ID;
        }
        if(this.slots[targetSlot].getStack()!=null) {
            if (Objects.equals(this.slots[targetSlot].getStack().getType(), id)) {
                return;
            }
        }
        int i = 0;
        for (ItemSlot slot:this.slots){
            if(!slot.isEmpty()&& Objects.equals(slot.getStack().getType(), id)){
                exchangeSlot(i,targetSlot);
                return;
            }
            i++;
        }
    }

    public void exchangeSlot(int from,int dest){
        ItemStack i=this.slots[from].getStack();
        this.slots[from].setStack(this.slots[dest].getStack());
        this.slots[dest].setStack(i);
    }

    @Override
    public NBTTagCompound getData() {
        NBTTagCompound tag=new NBTTagCompound();
        for (int i=0;i<this.slots.length;i++){
            ItemSlot slot=this.slots[i];
            if(!slot.isEmpty()) {
                tag.setCompoundTag(String.valueOf(i),slot.getStack().getData());
            }
        }
        return tag;
    }

    @Override
    public void setData(NBTTagCompound tag) {
        for (int i=0;i<this.slots.length;i++){
            if(tag.hasKey(String.valueOf(i))){
                ItemStack stack=new ItemStack();
                stack.setData(tag.getCompoundTag(String.valueOf(i)));
                this.slots[i].setStack(stack);
            }
        }
    }
}




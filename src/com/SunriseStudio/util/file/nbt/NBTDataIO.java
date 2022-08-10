package com.sunrisestudio.util.file.nbt;

import com.sunrisestudio.util.file.nbt.tag.NBTTagCompound;

public interface NBTDataIO {
    NBTTagCompound getData();
    void setData(NBTTagCompound tag);
}

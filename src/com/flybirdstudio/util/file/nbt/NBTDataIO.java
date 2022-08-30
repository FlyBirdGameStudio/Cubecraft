package com.flybirdstudio.util.file.nbt;

import com.flybirdstudio.util.file.nbt.tag.NBTTagCompound;

public interface NBTDataIO {
    NBTTagCompound getData();
    void setData(NBTTagCompound tag);
}

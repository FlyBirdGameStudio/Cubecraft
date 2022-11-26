package io.flybird.util.file.nbt;

import io.flybird.util.file.nbt.tag.NBTTagCompound;

public interface NBTDataIO {
    NBTTagCompound getData();
    void setData(NBTTagCompound tag);
}

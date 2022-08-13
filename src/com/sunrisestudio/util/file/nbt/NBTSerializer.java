package com.sunrisestudio.util.file.nbt;

import com.sunrisestudio.util.file.nbt.tag.NBTTagCompound;

public interface NBTSerializer<T> {
    NBTTagCompound serialize(T object);
}

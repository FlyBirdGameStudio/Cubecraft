package com.sunrisestudio.util.file.nbt;

import com.sunrisestudio.util.file.nbt.tag.NBTTagCompound;

public interface NBTDeserializer<T> {
    T deserialize(NBTTagCompound tag);
}
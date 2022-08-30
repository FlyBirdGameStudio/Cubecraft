package com.flybirdstudio.util.file.nbt;

import com.flybirdstudio.util.file.nbt.tag.NBTTagCompound;

public interface NBTDeserializer<T> {
    T deserialize(NBTTagCompound tag);
}

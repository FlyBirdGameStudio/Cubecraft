package com.flybirdstudio.util.file.nbt;

import com.flybirdstudio.util.file.nbt.tag.NBTTagCompound;

public interface NBTSerializer<T> {
    NBTTagCompound serialize(T object);
}
